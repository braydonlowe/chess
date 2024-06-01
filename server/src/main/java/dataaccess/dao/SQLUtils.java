package dataaccess.dao;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLUtils {
    public static void executeSQL(String executionString) {
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(executionString);
        } catch (DataAccessException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }
}
