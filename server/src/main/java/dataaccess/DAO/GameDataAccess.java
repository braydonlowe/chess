package dataaccess.DAO;

import Model.Game;

import java.util.HashMap;
import java.util.Map;

public class GameDataAccess {
    private static final Map<String, Game> gameTable = new HashMap<>();

    public static void clearGameData() {
        gameTable.clear();
    }

    //Create
    public static void createGame(String ID, Game game) {
        gameTable.put(ID, game);
    }

    //Read
    public static Game readGame(String ID) {
        return gameTable.get(ID);
    }

    //Update
    public static void updateGame(String ID, Game game) {
        createGame(ID, game);
    }

    //Delete
    public static void deleteGame(String ID) {
        gameTable.remove(ID);
    }

}
