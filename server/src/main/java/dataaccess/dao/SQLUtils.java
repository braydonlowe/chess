package dataaccess.dao;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.Auth;
import model.Game;
import model.User;
import server.JsonUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;


public class SQLUtils {


    public static void executeSQL(String executionString) {
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(executionString);
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static PreparedStatement prepareParameterizedQuery(String query, String[] parameters, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            int placement = 1;
            for (String param : parameters) {
                preparedStatement.setString(placement, param);
                placement++;
            }
            return preparedStatement; // returns .executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void executeParameterizedQuery(PreparedStatement statement) {
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Ensure resources are closed to prevent leaks
            closeQuietly(statement);
        }
    }

    public static <T> T getObject(Class<T> objToGet, PreparedStatement statement, String[] colName) throws DataAccessException {
        try (var response = statement.executeQuery()) {
            return resultHandler(response, objToGet, colName);
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T resultHandler(ResultSet resp, Class<T> returnClass, String[] name ) throws DataAccessException {
        //Check to make sure that the response isn't null.
        if (resp == null) {
            return null;
        }
        try {
            if (resp.next()) {
                if (returnClass == User.class) {
                    String username = resp.getString(name[0]);
                    String password = resp.getString(name[1]);
                    String email = resp.getString(name[2]);
                    return returnClass.cast(new User(username, password, email));
                }
                if (returnClass == Auth.class) {
                    String authToken = resp.getString(name[0]);
                    String username = resp.getString(name[1]);
                    return returnClass.cast(new Auth(authToken, username));
                }
                if (returnClass == Game.class) {
                    String id = resp.getString(name[0]);
                    String white = resp.getString(name[1]);
                    String black = resp.getString(name[2]);
                    String gameName = resp.getString(name[3]);
                    ChessGame game = JsonUtil.fromJson(resp.getString(name[4]), ChessGame.class);
                    return returnClass.cast(new Game(id,white,black,gameName,game));
                }
                else {
                    throw new DataAccessException("StupidFace the great");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Stupid face");
        }
        return null;
    }

    public static void closeQuietly(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                // Log this if necessary
                e.printStackTrace();
            }
        }
    }
    }
