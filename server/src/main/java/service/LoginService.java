package service;

//Imports
import Model.Auth;
import Model.User;
import dataaccess.DataAccessException;
import dataaccess.DAO.*;

public class LoginService{
    private final AuthDataAccess authData;
    private final UserDataAccess userData;


    public LoginService(AuthDataAccess authData, GameDataAccess gameData, UserDataAccess userData) {
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
