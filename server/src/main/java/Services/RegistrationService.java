package Services;

//Imports
import Model.Auth;
import Model.User;
import dataaccess.DataAccessException;
import dataaccess.DAO.*;

public class RegistrationService {
    private final AuthDataAccess authData;
    //I don't think it needs game data
    //private final GameDataAccess gameData;
    private final UserDataAccess userData;


    public RegistrationService(AuthDataAccess authData, GameDataAccess gameData, UserDataAccess userData) {
        this.authData = authData;
        //this.gameData = gameData;
        this.userData = userData;
    }
    public Auth createUser(User oneUsersData) throws DataAccessException {
        //Check to see if the user is in the database if not throw a 403 error
        User currentUser = userData.read(oneUsersData.username());
        //If an input feild is missing throw a 400 error.

        if (currentUser == null) {
            if (oneUsersData.username() == null || oneUsersData.password() == null || oneUsersData.email() == null) {
                throw new DataAccessException("Missing information");
            }
            userData.create(oneUsersData.username(), oneUsersData);
            //Create an auth token
            //Login
        }
        else {
            throw new DataAccessException("Username taken");
        }
        return null;
    }
}
