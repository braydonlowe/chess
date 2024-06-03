package dataaccess.dao;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLUtils {
    public static void executeSQL(String executionString) {
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(executionString);
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeParameterizedQuery(String query, String[] parameters) {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int placement = 0;
            for (String param : parameters) {
                preparedStatement.setString(placement, param);
                placement++;
            }
            preparedStatement.executeUpdate();
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Object executeParameterizedQueryWithReturn() {
        return null;
    }


    }
