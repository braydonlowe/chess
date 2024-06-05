package service;

//Imports
import model.Auth;
import model.User;
import dataaccess.DataAccessException;
import dataaccess.dao.*;

public class LoginService{
    private final SQLAuthDataAccess authData;
    private final SQLUserDataAccess userData;


    public LoginService(SQLAuthDataAccess authData, GameDataAccess gameData, SQLUserDataAccess userData) {
        this.authData = authData;
        this.userData = userData;
    }
    public Auth loginUser(User oneUsersData) throws DataAccessException {
        User currentUser = userData.read(oneUsersData.username());
        if (currentUser == null) {
            throw new DataAccessException("unauthorized");
        }
        //Check to see if passwords match
        if (!currentUser.password().equals(oneUsersData.password())) {
            throw new DataAccessException("unauthorized");
        }
        else {
            Auth newAuth = new Auth(authData.createAuth(), currentUser.username());
            authData.create(newAuth.authToken(), newAuth);
            return newAuth;
        }
    }
}
