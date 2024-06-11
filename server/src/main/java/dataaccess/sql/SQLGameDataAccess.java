package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.GameName;
import model.Game;
import server.JsonUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLGameDataAccess {
    private static final Map<String, Game> GAMETABLE = new HashMap<>();



    private int gameTableCount = 0;

    public void clear() {
        String clearSQL = "DELETE from game";
        SQLUtils.executeSQL(clearSQL);
        gameTableCount = 0;
    }

    //Create
    public void create(String id, Game game) throws DataAccessException {
        Connection connection = DatabaseManager.getConnection();
        String createUser = "INSERT INTO game(id, whiteUsername, blackUsername, gameName, chessGame) VALUES(?, ?, ?, ?, ?)";
        String serialKillerGame = JsonUtil.toJson(game);
        String name = "";

        //This is working for the other tests. However it's not working for a regular string.
        boolean jsonRequest = JsonUtil.isValidJsonString(game.gameName());
        if (jsonRequest) {
            //I have no idea why this is happening but here's the band-aid
            String gameNameJson = game.gameName();
            GameName gameWithName = JsonUtil.fromJson(gameNameJson, GameName.class);
            //Naming has gotten in the way. We have two different requests gameID and gameName
            name = gameWithName.gameName();
            if (gameWithName.gameName() == null) {
                name = gameWithName.gameID();
            }
        } else {
            name = game.gameName();
        }
        String[] params = {game.gameID(), game.whiteUsername(), game.blackUsername(), name, serialKillerGame};
        PreparedStatement statement = SQLUtils.prepareParameterizedQuery(createUser, params, connection);
        SQLUtils.executeParameterizedQuery(statement);
        SQLUtils.closeQuietly(statement);
        SQLUtils.closeQuietly(connection);
        if (id != null) {
            gameTableCount++;
        }
    }

    //Read
    public Game read(String id) throws DataAccessException {
        Connection connection = DatabaseManager.getConnection();
        String query = "SELECT * FROM game WHERE `id` = ?";
        String[] param = {id};
        String[] columnID = {"id", "whiteUsername", "blackUsername", "gameName", "chessGame"};


        PreparedStatement statement = SQLUtils.prepareParameterizedQuery(query, param, connection);
        //We need to edit our query to return something.
        Game game = SQLUtils.getObject(Game.class, statement, columnID);
        SQLUtils.closeQuietly(statement);
        SQLUtils.closeQuietly(connection);
        return game;
    }

    //Update
    public void update(String id, Game game) throws DataAccessException {
        Connection connection = DatabaseManager.getConnection();
        String query = """
        UPDATE game SET `whiteUsername` = ?, `blackUsername` = ?, `chessGame` = ?
        WHERE `id` = ?
        """;
        String gameData = JsonUtil.toJson(game);
        String[] params = {game.whiteUsername(), game.blackUsername(), gameData, id};
        PreparedStatement statement = SQLUtils.prepareParameterizedQuery(query, params, connection);
        SQLUtils.executeParameterizedQuery(statement);
        SQLUtils.closeQuietly(statement);
        SQLUtils.closeQuietly(connection);
    }

    //Delete
    public void delete(String id) {
        return;
    }

    public Game[] listGames() {
        String query = "SELECT * FROM game";
        List<Game> gameList = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.prepareStatement(query)) {
            try (var nuggies = statement.executeQuery(query)) {
                while (nuggies.next()) {
                    String gameID = nuggies.getString("id");
                    Game game = read(gameID);
                    gameList.add(game);
                }
                return gameList.toArray(new Game[0]);
            }

        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            return new Game[0];
        }
    }

    public int size() {
        return gameTableCount;
    }
}
