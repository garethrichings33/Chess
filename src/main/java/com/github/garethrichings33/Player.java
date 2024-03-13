package com.github.garethrichings33;

public class Player {
    private String name;
    private PieceColour pieceColour;
    private boolean inCheck;

    public Player(String name, PieceColour pieceColour) {
        this.name = name;
        this.pieceColour = pieceColour;
        this.inCheck = false;
    }

    public PieceColour getPieceColour() {
        return pieceColour;
    }

    public void setPieceColour(PieceColour pieceColour) {
        this.pieceColour = pieceColour;
    }
}
