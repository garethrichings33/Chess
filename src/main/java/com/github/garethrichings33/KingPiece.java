package com.github.garethrichings33;

import javax.swing.*;
import java.io.File;

public class KingPiece extends Piece{

    public KingPiece(PieceColour colour) {
        super(new ImageIcon("/resources/Chess_klt.png"), colour);
        var fileName = new File("/src/main/resources/Chess_klt60.png");
        System.out.println(fileName.exists());
    }

    @Override
    public boolean moveAllowed(int[] initialSquare, int[] finalSquare) {
        return false;
    }
}
