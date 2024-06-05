package dataaccess.dao;

import model.Auth;
import model.User;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

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

    }

    //Create
    public void create(String id, Auth auth) {
        String createUser = "INSERT INTO auth(authToken, username) VALUES(?, ?)";
        String[] params = {auth.authToken(), auth.username()};
        PreparedStatement statement = SQLUtils.prepareParameterizedQuery(createUser, params);
        SQLUtils.executeParameterizedQuery(statement);
        authCount++;
    }


    //Read
    public Auth read(String id) {
        String query = "SELECT * FROM auth WHERE `authToken` = ?";
        String[] param = {id};
        String[] columnID = {"authToken","username"};
        PreparedStatement statement = SQLUtils.prepareParameterizedQuery(query, param);
        //We need to edit our query to return something.
        Auth auth = SQLUtils.getObject(Auth.class, statement, columnID);
        return auth;
    }


    //Update
    //This isn't right yet but we'll come back to this one on each DB.
    public void update(String id, Auth newAuth) {
        create(id, newAuth);
    }

    //Delete
    public void delete(String id) {
        AUTHTABLE.remove(id);
    }


    public String createAuth() {
        authNumber++;
        return Integer.toString(authNumber);
    }

    public int size() {
        return authCount;
    }
}
