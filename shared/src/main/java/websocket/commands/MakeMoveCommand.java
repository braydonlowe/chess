package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private ChessMove move;

    public MakeMoveCommand(String auth, String gameID, ChessMove move) {
        super(auth);
        setGameID(gameID);
        this.move = move;
    }

    public ChessMove getMove(){
        return this.move;
    }

}
