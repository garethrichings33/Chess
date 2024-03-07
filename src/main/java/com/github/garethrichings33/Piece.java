package com.github.garethrichings33;

import javax.swing.*;

public abstract class Piece {
    Icon pieceIcon;

    public abstract boolean moveAllowed(int[] initialSquare, int[] finalSquare);
}
