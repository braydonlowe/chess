package dataaccess;

import dataaccess.dao.SQLAuthDataAccess;
import dataaccess.dao.SQLGameDataAccess;
import dataaccess.dao.SQLUserDataAccess;
import model.Auth;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {
    private SQLAuthDataAccess authData;
    private SQLGameDataAccess gameData;
    private SQLUserDataAccess userData;

    @BeforeEach
    void startNewDAO() {
        authData = new SQLAuthDataAccess();
        gameData = new SQLGameDataAccess();
        userData = new SQLUserDataAccess();
    }

    @AfterEach
    void clearAllDAO() {
        authData.clear();
        gameData.clear();
        userData.clear();
    }


    //Auth stuff
    @Test
    void authCreatePos() throws DataAccessException{
        String authToken = authData.createAuth();
        authData.create(authToken, new Auth(authToken, "goodUsername"));
        assertEquals(1, authData.size());
    }

    @Test
    void authCreateNeg() throws DataAccessException {
        String authToken = authData.createAuth();
        authData.create(null, new Auth(authToken, "goodUsername"));
        assertEquals(0, authData.size());
    }

    @Test
    void authDataClear() throws DataAccessException{
        String authToken = authData.createAuth();
        authData.create(authToken, new Auth(authToken, "goodUsername"));
        assertEquals(1, authData.size());
        authData.clear();
        assertEquals(0, authData.size());
    }


    @Test
    void authTokenReadPos() throws DataAccessException{
        String authToken = authData.createAuth();
        authData.create(authToken, new Auth(authToken, "goodUsername"));
        Auth auth = authData.read(authToken);
        assertEquals("goodUsername", auth.username());
    }

    @Test
    void authTokenReadNeg() throws DataAccessException {
        String authToken = authData.createAuth();
        String authToken2 = authData.createAuth();
        authData.create(authToken, new Auth(authToken, "thisUserName"));
        authData.create(authToken, new Auth(authToken2, "otherUsername"));
        Auth auth = authData.read(authToken2);
        assertNotEquals("thisUserName", auth.username());
    }

    @Test
    void authTokenDeletePos() throws DataAccessException{
        String authToken = authData.createAuth();
        authData.create(authToken, new Auth(authToken, "goodUsername"));
        assertEquals(1, authData.size());
        authData.delete(authToken);
        assertEquals(0, authData.size());
    }

    @Test
    void authTokenDeleteNeg() throws DataAccessException {
        authData.delete("Doesn'tExist");
        assertEquals(0, authData.size());
    }
    //Users stuff

    //Game stuff

}
