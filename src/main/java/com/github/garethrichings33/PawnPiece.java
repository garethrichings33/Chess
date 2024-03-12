package com.github.garethrichings33;

import java.util.HashMap;

public class PawnPiece extends Piece{
    private static HashMap<Enum, String> iconResourceNames = new HashMap<>()
    {{put(PieceColour.BLACK, "/Chess_pdt60.png");
        put(PieceColour.WHITE, "/Chess_plt60.png");}};

    public PawnPiece(PieceColour colour) {
        super(colour, iconResourceNames.get(colour));
    }

    @Override
    public boolean moveAllowed(int[] initialSquare, int[] finalSquare) {
        return false;
    }
}
