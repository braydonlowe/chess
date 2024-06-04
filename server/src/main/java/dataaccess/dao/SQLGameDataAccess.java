package dataaccess.dao;

import model.Game;

import java.util.HashMap;
import java.util.Map;

public class SQLGameDataAccess {
    private static final Map<String, Game> GAMETABLE = new HashMap<>();


    public void clear() {
        GAMETABLE.clear();
    }

    //Create
    public void create(String id, Game game) {
        GAMETABLE.put(id, game);
    }

    //Read
    public Game read(String id) {
        return GAMETABLE.get(id);
    }

    //Update
    public void update(String id, Game game) {
        create(id, game);
    }

    //Delete
    public void delete(String id) {
        GAMETABLE.remove(id);
    }

    public Game[] listGames() {
        Game [] gameList = new Game[GAMETABLE.size()];
        int index = 0;
        for(Map.Entry<String, Game> entry : GAMETABLE.entrySet()) {
            gameList[index] = entry.getValue();
            index++;
        }
        return gameList;
    }

    public int size() {
        return GAMETABLE.size();
    }
}
