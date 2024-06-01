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
import java.sql.Statement;

public class SQLUserDataAccess implements DataAccessInterface<User> {
    private static final Map<String, User> USERTABLE = new HashMap<>();

    private final String createSQL = """
            CREATE TABLE IF NOT EXISTS user (
            `userID` int NOT NULL AUTO_INCREMENT,
            `username` VARCHAR(200) NOT NULL,
            `password` VARCHAR(200) NOT NULL,
            `email` VARCHAR(200) NOT NULL,
            PRIMARY KEY (`userID`),
            UNIQUE KEY `username_UNIQUE` (`username`)
            )""";

    public void createTable() {
        SQLUtils.executeSQL(createSQL);
    }

    @Override
    public void clear() {
        USERTABLE.clear();
    }

    @Override
    public void create(String id, User user) {
        USERTABLE.put(id, user);
    }

    @Override
    public User read(String id) {
        return USERTABLE.get(id);
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
        return USERTABLE.size();
    }

}