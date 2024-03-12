package com.github.garethrichings33;

import java.util.HashMap;

public class QueenPiece extends Piece{
    private static HashMap<Enum, String> iconResourceNames = new HashMap<>()
    {{put(PieceColour.BLACK, "/Chess_qdt60.png");
        put(PieceColour.WHITE, "/Chess_qlt60.png");}};
    public QueenPiece(PieceColour colour) {
        super(colour, iconResourceNames.get(colour));
    }

    @Override
    public boolean moveAllowed(int[] initialSquare, int[] finalSquare) {
        return false;
    }
}
