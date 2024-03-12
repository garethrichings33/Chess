package com.github.garethrichings33;

import java.util.HashMap;

public class KingPiece extends Piece{

    private static HashMap<Enum, String> iconResourceNames = new HashMap<>()
        {{put(PieceColour.BLACK, "/Chess_kdt60.png");
        put(PieceColour.WHITE, "/Chess_klt60.png");}};

    public KingPiece(PieceColour colour) {
            super(colour,iconResourceNames.get(colour));
    }

    @Override
    public boolean moveAllowed(int[] initialSquare, int[] finalSquare) {
        return false;
    }
}
