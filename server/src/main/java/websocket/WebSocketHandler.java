package websocket;

import chess.ChessGame;
import chess.ChessMove;
import dataaccess.DataAccessException;
import dataaccess.dao.AuthDataAccess;
import dataaccess.dao.GameDataAccess;
import dataaccess.sql.SQLAuthDataAccess;
import dataaccess.sql.SQLGameDataAccess;
import model.Auth;
import model.Game;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.JsonUtil;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connectionManager = new ConnectionManager();
    private final SQLAuthDataAccess authDataAccess;
    private final SQLGameDataAccess gameDataAccess;

    public WebSocketHandler(SQLAuthDataAccess auth, SQLGameDataAccess game) {
        this.authDataAccess = auth;
        this.gameDataAccess = game;
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String clientJson) throws IOException {
        try {
            UserGameCommand command = JsonUtil.fromJson(clientJson, UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> connect(session, command);
                case MAKE_MOVE -> makeMove(session, JsonUtil.fromJson(clientJson, MakeMoveCommand.class));
                case LEAVE -> leaveGame(session, command);
                case RESIGN -> resign(session, command);
            }
        } catch (Exception e ) {
            session.getRemote().sendString(JsonUtil.toJson(new ErrorMessage(e.getMessage())));
        }
    }

    private void connect(Session session, UserGameCommand command) throws Exception {
        String auth = command.getAuthString();
        String gameID = command.getGameID();
        Game thisGame = gameDataAccess.read(gameID);
        Auth thisAuth = authDataAccess.read(auth);
        if (thisGame == null) {
            session.getRemote().sendString(JsonUtil.toJson(new ErrorMessage("Incorrect GameID")));
            return;
        }
        String username = thisAuth.username();
        connectionManager.add(thisGame.gameID(), thisAuth.authToken(), session);
        String message = formatJoinMessage(thisGame, username);
        session.getRemote().sendString(JsonUtil.toJson(new LoadGameMessage(thisGame)));
        Notification notification = new Notification(message);
        connectionManager.broadcast(thisGame.gameID(), thisAuth.authToken(), notification);
    }

    private void leaveGame(Session session, UserGameCommand command) throws DataAccessException, IOException {
        String token = command.getAuthString();
        String gameID = command.getGameID();

        Auth currentUser = authDataAccess.read(token);
        Game currentGame = gameDataAccess.read(gameID);

        if (Objects.equals(currentGame.whiteUsername(), currentUser.username())) {
            currentGame = new Game(currentGame.gameID(), null, currentGame.blackUsername(), currentGame.gameName(), currentGame.game());
            gameDataAccess.update(gameID, currentGame);
        } else if (Objects.equals(currentGame.blackUsername(), currentUser.username())) {
            currentGame = new Game(currentGame.gameID(), currentGame.whiteUsername(), null, currentGame.gameName(), currentGame.game());
            gameDataAccess.update(gameID, currentGame);
        }

        connectionManager.remove(gameID, token);
        String message = String.format("%s has left the Game", currentUser.username());
        Notification notification = new Notification(message);
        connectionManager.broadcast(gameID, token, notification);
    }

    private void makeMove(Session session, MakeMoveCommand command) throws Exception {
        String auth = command.getAuthString();
        String gameID = command.getGameID();
        Game thisGame = gameDataAccess.read(gameID);
        Auth thisAuth = authDataAccess.read(auth);
        ChessMove move = command.getMove();
        if (!isPlayer(thisGame, thisAuth.username())) {
            session.getRemote().sendString(JsonUtil.toJson(new ErrorMessage("Observers cannot move. They are objects.")));
            return;
        }
        try {
            validateMove(thisGame, thisAuth, move);
        } catch (Exception e) {
            session.getRemote().sendString(JsonUtil.toJson(new ErrorMessage(e.getMessage())));
            return;
        }
        thisGame.game().makeMove(move);
        if (thisGame.whiteUsername().equals(thisAuth.username())) {
            thisGame.game().switchTeamColor(ChessGame.TeamColor.WHITE);
        }
        else {
            thisGame.game().switchTeamColor(ChessGame.TeamColor.BLACK);
        }

        gameDataAccess.update(thisGame.gameID(), thisGame);
        moveUpdatesBroadcast(thisGame.gameID(), thisGame, thisAuth);
    }

    private void resign(Session session, UserGameCommand command) throws DataAccessException, IOException {
        String auth = command.getAuthString();
        String gameID = command.getGameID();
        Game thisGame = gameDataAccess.read(gameID);
        Auth thisAuth = authDataAccess.read(auth);
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
        String message = String.format("%s has resigned", thisAuth.username());
        Notification notification = new Notification(message);
        connectionManager.broadcastAll(thisGame.gameID(), notification);
    }

    private void validateMove(Game thisGame, Auth currentUser, ChessMove move) throws Exception {
        String userName = currentUser.username();
        ChessGame.TeamColor turn = thisGame.game().getTeamTurn();
        ChessGame.TeamColor currentUsersTeam = getUserTeam(thisGame, userName);
        if (thisGame.game().isGameOver()) {
            throw new Exception("Game is over");
        }
        if (!thisGame.game().validMoves(move.getStartPosition()).contains(move)) {
            throw new Exception("Not a valid move");
        }
        if (turn != currentUsersTeam) {
            throw new Exception("Not your turn.");
        }

    }

    private void moveUpdatesBroadcast(String gameID, Game currentGame, Auth currentUser) throws DataAccessException, IOException {
        connectionManager.broadcastAll(gameID, new LoadGameMessage(currentGame));
        String message = String.format("%s has made a move", currentUser.username());
        Notification notification = new Notification(message);
        connectionManager.broadcast(gameID, currentUser.authToken(), notification);
        postMoveUpdate(currentGame, gameID);
    }

    private void postMoveUpdate(Game currentGame, String gameID) throws DataAccessException, IOException {
        if (currentGame.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
            announceGameResult(currentGame, gameID, String.format("%s has won the game! White is in checkmate", currentGame.blackUsername()));
            currentGame.game().setGameOver(true);
            gameDataAccess.update(gameID, currentGame);
        } else if (currentGame.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
            announceGameResult(currentGame, gameID, String.format("%s has won the game! Black is in checkmate", currentGame.whiteUsername()));
            currentGame.game().setGameOver(true);
            gameDataAccess.update(gameID, currentGame);
        } else if (currentGame.game().isInStalemate(ChessGame.TeamColor.WHITE) || currentGame.game().isInStalemate(ChessGame.TeamColor.BLACK)) {
            announceGameResult(currentGame, gameID, "Tie! Stalemate!");
            currentGame.game().setGameOver(true);
            gameDataAccess.update(gameID, currentGame);
        } else {
            if (currentGame.game().isInCheck(ChessGame.TeamColor.WHITE)) {
                connectionManager.broadcastAll(gameID, new Notification("White is in check!"));
            }
            if (currentGame.game().isInCheck(ChessGame.TeamColor.BLACK)) {
                connectionManager.broadcastAll(gameID, new Notification("Black is in check!"));
            }
        }
    }

    private void announceGameResult(Game currentGame, String gameID, String message) throws DataAccessException, IOException {
        Notification notification = new Notification(message);
        currentGame.game().setGameOver(true);
        gameDataAccess.update(gameID, currentGame);
        connectionManager.broadcastAll(gameID, notification);
    }

    private String formatJoinMessage(Game game, String username) {
        if (Objects.equals(game.blackUsername(), username)) {
            return username + " is now playing as Black";

        } else if (Objects.equals(game.whiteUsername(), username)) {
            return username + " is now playing as White";
        } else {
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

}
