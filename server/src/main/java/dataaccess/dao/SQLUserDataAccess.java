package dataaccess.dao;

//Imports
import dataaccess.DataAccessException;
import model.User;
import java.util.HashMap;
import java.util.Map;
import dataaccess.DatabaseManager;

//SQLImports
import dataaccess.dao.SQLUtils;
import java.sql.Connection;
import java.sql.SQLException;
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
    public void create(String id, User user) {
        String createUser = "INSERT INTO user(username, password, email) VALUES(?, ?, ?)";
        String[] params = {user.username(), user.password(), user.email()};
        PreparedStatement statement = SQLUtils.prepareParameterizedQuery(createUser, params);
        SQLUtils.executeParameterizedQuery(statement);
        userCount++;
    }

    @Override
    public User read(String id) {
        String query = "SELECT * FROM user WHERE `username` = ?";
        String[] param = {id};
        String[] columnID = {"username","password","email"};
        PreparedStatement statement = SQLUtils.prepareParameterizedQuery(query, param);
        //We need to edit our query to return something.
        User user = SQLUtils.getObject(User.class, statement, columnID);
        return user;
    }


    @Override
    public void update(String id, User user) {
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
