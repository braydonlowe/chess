package service;

//Imports
import dataaccess.sql.SQLAuthDataAccess;
import dataaccess.sql.SQLGameDataAccess;
import dataaccess.sql.SQLUserDataAccess;
import model.Auth;
import model.User;
import dataaccess.DataAccessException;
import org.mindrot.jbcrypt.BCrypt;

public class LoginService{
    private final SQLAuthDataAccess authData;
    private final SQLUserDataAccess userData;


    public LoginService(SQLAuthDataAccess authData, SQLGameDataAccess gameData, SQLUserDataAccess userData) {
        this.authData = authData;
        this.userData = userData;
    }
    public Auth loginUser(User oneUsersData) throws DataAccessException {
        User currentUser = userData.read(oneUsersData.username());
        if (currentUser == null) {
            throw new DataAccessException("unauthorized");
        }
        if (!BCrypt.checkpw(oneUsersData.password(), currentUser.password())) {
            throw new DataAccessException("unauthorized");
        }
        else {
            Auth newAuth = new Auth(authData.createAuth(), currentUser.username());
            authData.create(newAuth.authToken(), newAuth);
            return newAuth;
        }
    }
}
