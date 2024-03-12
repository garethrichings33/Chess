package com.github.garethrichings33;

import java.util.HashMap;

public class RookPiece extends Piece{
    private static HashMap<Enum, String> iconResourceNames = new HashMap<>()
    {{put(PieceColour.BLACK, "/Chess_rdt60.png");
        put(PieceColour.WHITE, "/Chess_rlt60.png");}};
    public RookPiece(PieceColour colour) {
        super(colour, iconResourceNames.get(colour));
    }

    @Override
    public boolean moveAllowed(int[] initialSquare, int[] finalSquare) {
        return false;
    }
}
