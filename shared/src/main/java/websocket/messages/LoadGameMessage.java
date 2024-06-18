package websocket.messages;

import model.Game;

public class LoadGameMessage extends ServerMessage{
    private Game game;
    private String team;
    public LoadGameMessage(Game game) {
        super(ServerMessageType.LOAD_GAME);
        this.game=game;
    }
    public Game getGameData(){
        return this.game;
    }

    public void setTeam(String setTeam) {
        team = setTeam;
    }

    public String getTeam() {
        return team;
    }
}
