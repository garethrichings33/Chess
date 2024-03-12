package com.github.garethrichings33;

import java.util.HashMap;

public class KnightPiece extends Piece{
    private static HashMap<Enum, String> iconResourceNames = new HashMap<>()
    {{put(PieceColour.BLACK, "/Chess_ndt60.png");
        put(PieceColour.WHITE, "/Chess_nlt60.png");}};
    public KnightPiece(PieceColour colour) {
        super(colour, iconResourceNames.get(colour));
    }

    @Override
    public boolean moveAllowed(int[] initialSquare, int[] finalSquare) {
        return false;
    }
}
