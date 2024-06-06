package dataaccess;

import dataaccess.dao.SQLUtils;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    private static final String createGameSQL = """
            CREATE TABLE IF NOT EXISTS game (
            `id` VARCHAR(200) NOT NULL UNIQUE PRIMARY KEY,
            `whiteUsername` VARCHAR(200),
            `blackUsername` VARCHAR(200),
            `gameName` VARCHAR(200) NOT NULL,
            `chessGame` TEXT NOT NULL
            )""";

    private static final String createUserSQL = """
            CREATE TABLE IF NOT EXISTS user (
            `username` VARCHAR(200) UNIQUE PRIMARY KEY,
            `password` VARCHAR(500) NOT NULL,
            `email` VARCHAR(200) NOT NULL
            )""";

    private static final String createAuthSQL = """
            CREATE TABLE IF NOT EXISTS auth (
            `authToken` VARCHAR(200) UNIQUE PRIMARY KEY,
            `username` VARCHAR(200) NOT NULL
            )""";




    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    public static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
                //Tables have to be created here not in the SQLDAO's
                createTables();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    /**
     * Create tables creates the tables for the DB.
     * It has to be created here so that persistance can be achieved.
     */

    private static void createTables() {
        SQLUtils.executeSQL(createGameSQL);
        SQLUtils.executeSQL(createAuthSQL);
        SQLUtils.executeSQL(createUserSQL);

    }
}
