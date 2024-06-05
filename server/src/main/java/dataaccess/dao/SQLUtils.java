package dataaccess.dao;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.User;
import server.JsonUtil;

import javax.xml.crypto.Data;
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

    public static PreparedStatement prepareParameterizedQuery(String query, String[] parameters) {
        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            int placement = 1;
            for (String param : parameters) {
                preparedStatement.setString(placement, param);
                placement++;
            }
            return preparedStatement; // returns .executeQuery();
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void executeParameterizedQuery(PreparedStatement statement) {
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static <T> T getObject(Class<T> objToGet, PreparedStatement statement, String[] colName) {
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
                } else {
                    throw new DataAccessException("StupidFace the great");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Stupid face");
        }
        throw new DataAccessException("Stupid face 2");
    }
    }
