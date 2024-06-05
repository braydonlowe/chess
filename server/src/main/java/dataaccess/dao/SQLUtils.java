package dataaccess.dao;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import server.JsonUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int placement = 0;
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
            statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static <T> T getObject(Class<T> objToGet, PreparedStatement statement, String colName) {
        try (var response = statement.executeQuery()) {
            return resultHandler(response, objToGet,colName);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T resultHandler(ResultSet resp, Class<T> returnClass, String name ) {
        //Check to make sure that the response isn't null.
        if (resp == null) {
            return null;
        }
        try {
            String gson = resp.getString(name);
            return JsonUtil.fromJson(gson, returnClass);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    }
