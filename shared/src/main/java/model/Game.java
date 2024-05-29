package model;
import chess.ChessGame;
public record Game(String gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {}
