package dataaccess.dao;

//Imports
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.User;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

//SQLImports
import java.sql.PreparedStatement;

public class SQLUserDataAccess implements DataAccessInterface<User> {
    private static final Map<String, User> USERTABLE = new HashMap<>();
    private int userCount = 0;


    private final String createSQL = """
            CREATE TABLE IF NOT EXISTS user (
            `username` VARCHAR(200) UNIQUE PRIMARY KEY,
            `password` VARCHAR(200) NOT NULL,
            `email` VARCHAR(200) NOT NULL
            )""";

    public void createTable() {
        SQLUtils.executeSQL(createSQL);
    }

    @Override
    public void clear() {
        //Excecute a query that will end up getting this baby reset.
        String clearSQL = "DELETE from user";
        SQLUtils.executeSQL(clearSQL);
        userCount = 0;
    }

    @Override
    public void create(String id, User user) throws DataAccessException {
        Connection connection = DatabaseManager.getConnection();
        String createUser = "INSERT INTO user(username, password, email) VALUES(?, ?, ?)";
        String[] params = {user.username(), user.password(), user.email()};
        PreparedStatement statement = SQLUtils.prepareParameterizedQuery(createUser, params, connection);
        SQLUtils.executeParameterizedQuery(statement);
        SQLUtils.closeQuietly(statement);
        SQLUtils.closeQuietly(connection);
        userCount++;
    }

    @Override
    public User read(String id) throws DataAccessException {
        Connection connection = DatabaseManager.getConnection();
        String query = "SELECT * FROM user WHERE `username` = ?";
        String[] param = {id};
        String[] columnID = {"username","password","email"};
        PreparedStatement statement = SQLUtils.prepareParameterizedQuery(query, param, connection);
        //We need to edit our query to return something.
        User user = SQLUtils.getObject(User.class, statement, columnID);
        SQLUtils.closeQuietly(statement);
        SQLUtils.closeQuietly(connection);
        return user;
    }


    @Override
    public void update(String id, User user) throws DataAccessException {
    //I created this in two tables just in case we need to add some validatio here.
        create(id, user);
    }


    @Override
    public void delete(String id) {
        USERTABLE.remove(id);
    }

    public int size() {
        return userCount;
    }

}
