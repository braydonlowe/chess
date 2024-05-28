package Services;

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
        //Check to see if the user is in the database if not throw a 403 error
        User currentUser = userData.read(oneUsersData.username());
        if (currentUser == null) {
            throw new DataAccessException("User not found");
        }
        //Check to see if passwords match
        if (currentUser.password() != oneUsersData.password()) {
            throw new DataAccessException("Incorrect password");
        }
        else {
            Auth newAuth = new Auth("replaceMeString", currentUser.username());
            //In this case we have our unique identifier as our auth string
            authData.create(newAuth.authToken(), newAuth);
            return newAuth;
        }
    }
}
