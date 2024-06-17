package websocket.messages;

import model.Game;

public class LoadGameMessage extends ServerMessage{
    private Game game;
    public LoadGameMessage(Game game) {
        super(ServerMessageType.LOAD_GAME);
        this.game=game;
    }
    public Game getGameData(){
        return this.game;
    }
}
