package dataaccess.DAO;

import Model.Game;

import java.util.HashMap;
import java.util.Map;

public class GameDataAccess implements DataAccessInterface<Game> {
    private static final Map<String, Game> gameTable = new HashMap<>();


    public void clear() {
        gameTable.clear();
    }

    //Create
    public void create(String ID, Game game) {
        gameTable.put(ID, game);
    }

    //Read
    public Game read(String ID) {
        return gameTable.get(ID);
    }

    //Update
    public void update(String ID, Game game) {
        create(ID, game);
    }

    //Delete
    public void delete(String ID) {
        gameTable.remove(ID);
    }

}
