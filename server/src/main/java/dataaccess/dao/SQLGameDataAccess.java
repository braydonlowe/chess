package dataaccess.dao;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.Game;
import model.User;
import server.JsonUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SQLGameDataAccess {
    private static final Map<String, Game> GAMETABLE = new HashMap<>();

    public final String createSQL = """
            CREATE TABLE IF NOT EXISTS game (
            `id` VARCHAR(200) NOT NULL UNIQUE PRIMARY KEY,
            `whiteUsername` VARCHAR(200),
            `blackUsername` VARCHAR(200),
            `gameName` VARCHAR(200) NOT NULL,
            `chessGame` TEXT NOT NULL
            )""";

    private int gameTableCount = 0;

    public void createTable() {
        SQLUtils.executeSQL(createSQL);
    }


    public void clear() {
        String clearSQL = "DELETE from game";
        SQLUtils.executeSQL(clearSQL);
        gameTableCount = 0;
    }

    //Create
    public void create(String id, Game game) {
        String createUser = "INSERT INTO game(id, whiteUsername, blackUsername, gameName, chessGame) VALUES(?, ?, ?, ?, ?)";
        String serialKillerGame = JsonUtil.toJson(game);
        String[] params = {game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), serialKillerGame};
        PreparedStatement statement = SQLUtils.prepareParameterizedQuery(createUser, params);
        SQLUtils.executeParameterizedQuery(statement);
        gameTableCount++;
    }

    //Read
    public Game read(String id) throws DataAccessException {
        String query = "SELECT * FROM game WHERE `id` = ?";
        String[] param = {id};
        String[] columnID = {"id", "whiteUsername", "blackUsername", "gameName", "chessGame"};
        PreparedStatement statement = SQLUtils.prepareParameterizedQuery(query, param);
        //We need to edit our query to return something.
        Game game = SQLUtils.getObject(Game.class, statement, columnID);
        return game;
    }

    //Update
    public void update(String id, Game game) {
        String query = """
        UPDATE game SET `whiteUsername` = ?, `blackUsername` = ?, `chessGame` = ?
        WHERE `id` = ?
        """;
        String gameData = JsonUtil.toJson(game);
        String[] params = {game.whiteUsername(), game.blackUsername(), gameData, id};
        PreparedStatement statement = SQLUtils.prepareParameterizedQuery(query, params);
        SQLUtils.executeParameterizedQuery(statement);
    }

    //Delete
    public void delete(String id) {
        gameTableCount--;
    }

    public Game[] listGames() {
        String query = "SELECT * FROM game";
        Game[] gameList = new Game[gameTableCount];
        int curentIndex = 0;
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.prepareStatement(query)) {
            try (var nuggies = statement.executeQuery(query)) {
                while (nuggies.next()) {
                    String gameID = nuggies.getString(1);
                    Game game = read(gameID);
                    gameList[curentIndex] = game;
                    curentIndex++;
                }
                return gameList;
            }

        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int size() {
        return gameTableCount;
    }
}
