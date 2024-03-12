package com.github.garethrichings33;

import java.util.HashMap;

public class BishopPiece extends Piece{
    private static HashMap<Enum, String> iconResourceNames = new HashMap<>()
    {{put(PieceColour.BLACK, "/Chess_bdt60.png");
        put(PieceColour.WHITE, "/Chess_blt60.png");}};
    public BishopPiece(PieceColour colour) {
        super(colour, iconResourceNames.get(colour));
    }

    @Override
    public boolean moveAllowed(int[] initialSquare, int[] finalSquare) {
        return false;
    }
}
