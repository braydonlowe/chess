package websocket;

import dataaccess.DataAccessException;
import dataaccess.sql.SQLAuthDataAccess;
import dataaccess.sql.SQLGameDataAccess;
import chess.ChessGame;
import chess.ChessMove;
import model.Auth;
import model.Game;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.JsonUtil;
import websocket.commands.MakeMove;
import websocket.commands.UserGameCommand;
import websocket.messages.*;
import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connectionManager = new ConnectionManager();
    private SQLAuthDataAccess authDataAccess = null;
    private SQLGameDataAccess gameDataAccess = null;

    private String team;

    private Game thisGame;
    private Auth thisAuth;

    public WebSocketHandler(SQLAuthDataAccess auth, SQLGameDataAccess game) {
        this.authDataAccess = auth;
        this.gameDataAccess = game;
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String clientJson) throws IOException {
        try {
            UserGameCommand command = JsonUtil.fromJson(clientJson, UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT:
                    connect(session, command);
                    break;
                case MAKE_MOVE:
                    makeMove(session, JsonUtil.fromJson(clientJson, MakeMove.class));
                    break;
                case LEAVE:
                    leaveGame(command);
                    break;
                case RESIGN:
                    resign(session, command);
                    break;
            }
        } catch (Exception e ) {
            session.getRemote().sendString(JsonUtil.toJson(new ErrorMessage(e.getMessage())));
        }
    }

    private void connect(Session session, UserGameCommand command) throws Exception {
        setAuthGame(command);
        if (thisGame == null) {
            session.getRemote().sendString(JsonUtil.toJson(new ErrorMessage("Incorrect GameID")));
            return;
        }
        String username = thisAuth.username();
        connectionManager.add(thisGame.gameID(), thisAuth.authToken(), session);
        String message = formatJoinMessage(thisGame, username);
        LoadGameMessage loadGame = new LoadGameMessage(thisGame);
        loadGame.setTeam(team);
        session.getRemote().sendString(JsonUtil.toJson(loadGame));
        Notification notification = new Notification(message);
        connectionManager.broadcast(thisGame.gameID(), thisAuth.authToken(), notification);
    }

    private void resign(Session session, UserGameCommand command) throws DataAccessException, IOException {
        setAuthGame(command);
        if (!isPlayer(thisGame, thisAuth.username())) {
            session.getRemote().sendString(JsonUtil.toJson(new ErrorMessage("You're an observer and cannot resign")));
            return;
        }
        if (thisGame.game().isGameOver()) {
            session.getRemote().sendString(JsonUtil.toJson(new ErrorMessage("Game is already over")));
            return;
        }
        thisGame.game().setGameOver(true);
        gameDataAccess.update(thisGame.gameID(), thisGame);
        String message = thisAuth.username() + " has resigned";
        Notification notification = new Notification(message);
        connectionManager.broadcastAll(thisGame.gameID(), notification);
    }

    private void leaveGame(UserGameCommand command) throws DataAccessException, IOException {
        setAuthGame(command);
        if (thisGame.whiteUsername() == thisAuth.username()) {
            thisGame = new Game(thisGame.gameID(), null, thisGame.blackUsername(), thisGame.gameName(), thisGame.game());
            gameDataAccess.update(thisGame.gameID(), thisGame);
        } else if (thisGame.blackUsername() == thisAuth.username()) {
            thisGame = new Game(thisGame.gameID(), thisGame.whiteUsername(), null, thisGame.gameName(), thisGame.game());
            gameDataAccess.update(thisGame.gameID(), thisGame);
        }
        connectionManager.remove(thisGame.gameID(), thisAuth.authToken());
        String message = thisAuth.username() +" is no longer playing";
        Notification notification = new Notification(message);
        connectionManager.broadcast(thisGame.gameID(), thisAuth.authToken(), notification);
    }

    private void makeMove(Session session, MakeMove command) throws Exception {
        setAuthGame(command);
        ChessMove move = command.getMove();
        if (!isPlayer(thisGame, thisAuth.username())) {
            session.getRemote().sendString(JsonUtil.toJson(new ErrorMessage("Observers cannot move. They are objects.")));
            return;
        }
        try {
            validateMove(move);
        } catch (Exception e) {
            session.getRemote().sendString(JsonUtil.toJson(new ErrorMessage(e.getMessage())));
            return;
        }
        thisGame.game().makeMove(move);
        if (thisGame.whiteUsername().equals(thisAuth.username())) {
            thisGame.game().setTeamTurn(ChessGame.TeamColor.BLACK);
        }
        else {
            thisGame.game().setTeamTurn(ChessGame.TeamColor.WHITE);
        }
        gameDataAccess.update(thisGame.gameID(), thisGame);
        thisGame = gameDataAccess.read(thisGame.gameID());
        moveUpdatesBroadcast(thisGame.gameID(), thisGame, thisAuth);
    }

    private void validateMove(ChessMove move) throws Exception {
        String userName = thisAuth.username();
        ChessGame.TeamColor turn = thisGame.game().getTeamTurn();
        ChessGame.TeamColor currentUsersTeam = getUserTeam(thisGame, userName);
        if (thisGame.game().isGameOver()) {
            throw new Exception("Game is over");
        }
        if (!thisGame.game().validMoves(move.getStartPosition()).contains(move)) {
            throw new Exception("Illegal Move");
        }
        if (turn != currentUsersTeam) {
            throw new Exception("Not your turn.");
        }

    }

    private void moveUpdatesBroadcast(String gameID, Game game, Auth currentUser) throws DataAccessException, IOException {
        connectionManager.broadcastAll(gameID, new LoadGameMessage(game));
        String message = currentUser.username() + " has made a move";
        Notification notification = new Notification(message);
        connectionManager.broadcast(gameID, currentUser.authToken(), notification);
        moveUpdate(game, gameID);
    }

    private void moveUpdate(Game game, String gameID) throws DataAccessException, IOException {
        if (game.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
            gameResult(game, gameID, (game.blackUsername() + " has won!"));
            game.game().setGameOver(true);
            gameDataAccess.update(gameID, game);
        } else if (game.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
            gameResult(game, gameID, (game.whiteUsername() + " has won!"));
            game.game().setGameOver(true);
            gameDataAccess.update(gameID, game);
        } else if (game.game().isInStalemate(ChessGame.TeamColor.WHITE) || game.game().isInStalemate(ChessGame.TeamColor.BLACK)) {
            gameResult(game, gameID, "Stalemate");
            game.game().setGameOver(true);
            gameDataAccess.update(gameID, game);
        } else {
            if (game.game().isInCheck(ChessGame.TeamColor.WHITE)) {
                connectionManager.broadcastAll(gameID, new Notification("White is in check"));
            }
            if (game.game().isInCheck(ChessGame.TeamColor.BLACK)) {
                connectionManager.broadcastAll(gameID, new Notification("Black is in check"));
            }
        }
    }

    private void gameResult(Game currentGame, String gameID, String message) throws DataAccessException, IOException {
        Notification notification = new Notification(message);
        currentGame.game().setGameOver(true);
        gameDataAccess.update(gameID, currentGame);
        connectionManager.broadcastAll(gameID, notification);
    }

    private String formatJoinMessage(Game game, String username) {
        if (Objects.equals(game.blackUsername(), username)) {
            team = "BLACK";
            return username + " is now playing as Black";

        } else if (Objects.equals(game.whiteUsername(), username)) {
            team = "WHITE";
            return username + " is now playing as White";
        } else {
            team = "WHITE";
            return username + " is now an observer";
        }
    }

    private boolean isPlayer(Game game, String userName) {
        return Objects.equals(game.whiteUsername(), userName) || Objects.equals(game.blackUsername(), userName);
    }

    private ChessGame.TeamColor getUserTeam(Game game, String userName) throws Exception {
        if (Objects.equals(game.whiteUsername(), userName)) {
            return ChessGame.TeamColor.WHITE;
        } else if (Objects.equals(game.blackUsername(), userName)) {
            return ChessGame.TeamColor.BLACK;
        } else {
            throw new Exception("Not a player in the game");
        }
    }


    private void setAuthGame(UserGameCommand command) throws DataAccessException {
        String auth = command.getAuthString();
        String gameID = command.getGameID();
        thisGame = gameDataAccess.read(gameID);
        thisAuth = authDataAccess.read(auth);
    }

}
