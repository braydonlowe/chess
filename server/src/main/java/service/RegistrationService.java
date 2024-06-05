package service;

//Imports
import model.Auth;
import model.User;
import dataaccess.DataAccessException;
import dataaccess.dao.*;

public class RegistrationService {
    private final SQLAuthDataAccess authData;
    //I don't think it needs game data
    //private final GameDataAccess gameData;
    private final SQLUserDataAccess userData;


    public RegistrationService(SQLAuthDataAccess authData, SQLGameDataAccess gameData, SQLUserDataAccess userData) {
        this.authData = authData;
        //this.gameData = gameData;
        this.userData = userData;
    }
    public Auth createUser(User oneUsersData) throws DataAccessException {
        //Check to see if the user is in the database if not throw a 403 error
        User currentUser = userData.read(oneUsersData.username());

        //If an input field is missing throw a 400 error.
        if (currentUser == null) {
            if (oneUsersData.username() == null || oneUsersData.password() == null || oneUsersData.email() == null) {
                throw new DataAccessException("bad request");
            }
            userData.create(oneUsersData.username(), oneUsersData);
            //Create an auth token
            String authToken = authData.createAuth();
            Auth newAuth = new Auth(authToken, oneUsersData.username());
            authData.create(authToken, newAuth);
            return newAuth;
            //Login
        }
        else {
            throw new DataAccessException("already taken");
        }
    }
}
