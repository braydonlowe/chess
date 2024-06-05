package dataaccess.dao;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.Auth;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SQLAuthDataAccess {

    private final String createSQL = """
            CREATE TABLE IF NOT EXISTS auth (
            `authToken` VARCHAR(200) UNIQUE PRIMARY KEY,
            `username` VARCHAR(200) NOT NULL
            )""";

    private int authNumber = 0;
    private int authCount = 0;

    public void createTable() {
        SQLUtils.executeSQL(createSQL);
    }
    public void clear() {
        String clearSQL = "DELETE from auth";
        SQLUtils.executeSQL(clearSQL);
        authCount = 0;
    }

    //Create
    public void create(String id, Auth auth) throws DataAccessException {
        Connection connection = DatabaseManager.getConnection();
        String createUser = "INSERT INTO auth(authToken, username) VALUES(?, ?)";
        String[] params = {auth.authToken(), auth.username()};
        PreparedStatement statement = SQLUtils.prepareParameterizedQuery(createUser, params, connection);
        SQLUtils.executeParameterizedQuery(statement);
        SQLUtils.closeQuietly(statement);
        SQLUtils.closeQuietly(connection);
        authCount++;
    }


    //Read
    public Auth read(String id) throws DataAccessException {
        Connection connection = DatabaseManager.getConnection();
        String query = "SELECT * FROM auth WHERE `authToken` = ?";
        String[] param = {id};
        String[] columnID = {"authToken","username"};
        PreparedStatement statement = SQLUtils.prepareParameterizedQuery(query, param, connection);
        //We need to edit our query to return something.
        Auth auth = SQLUtils.getObject(Auth.class, statement, columnID);
        SQLUtils.closeQuietly(statement);
        SQLUtils.closeQuietly(connection);
        return auth;
    }


    //Update
    //This isn't right yet but we'll come back to this one on each DB.
    public void update(String id, Auth newAuth) throws DataAccessException {
        create(id, newAuth);
    }

    //Delete
    public void delete(String id) throws DataAccessException {
        Connection connection = DatabaseManager.getConnection();
        String query = "DELETE FROM auth WHERE `authToken` = ?";
        String[] param = {id};
        PreparedStatement statement = SQLUtils.prepareParameterizedQuery(query, param, connection);
        SQLUtils.executeParameterizedQuery(statement);
        SQLUtils.closeQuietly(statement);
        SQLUtils.closeQuietly(connection);
        authCount--;
    }


    public String createAuth() {
        authNumber++;
        return Integer.toString(authNumber);
    }

    public int size() {
        return authCount;
    }
}
