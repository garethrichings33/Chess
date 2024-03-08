package com.github.garethrichings33;

import javax.swing.*;

public abstract class Piece {
    protected final Icon pieceIcon;

    protected final PieceColour colour;
    public Piece(Icon pieceIcon, PieceColour colour) {
        this.pieceIcon = pieceIcon;
        this.colour = colour;
    }

    public abstract boolean moveAllowed(int[] initialSquare, int[] finalSquare);

    public Icon getPieceIcon() {
        return pieceIcon;
    }

    public PieceColour getColour() {
        return colour;
    }
}
