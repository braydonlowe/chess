package dataaccess.DAO;

import Model.User;

import java.util.HashMap;
import java.util.Map;

public class GameDataAccess {
    private static final Map<String, User> gameTable = new HashMap<>();

    public static void clearGameData() {
        gameTable.clear();
    }
}
