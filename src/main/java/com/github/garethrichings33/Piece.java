package com.github.garethrichings33;

import javax.swing.*;

public abstract class Piece {
    Icon pieceIcon;
    PieceColour colour;

    public abstract boolean moveAllowed(int[] initialSquare, int[] finalSquare);

    public PieceColour getColour() {
        return colour;
    }
}
