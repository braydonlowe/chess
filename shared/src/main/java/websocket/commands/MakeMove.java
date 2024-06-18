package websocket.commands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    private ChessMove move;

    public MakeMove(String auth, String gameID, ChessMove move) {
        super(auth);
        setGameID(gameID);
        this.move = move;
    }

    public ChessMove getMove(){
        return this.move;
    }

}
