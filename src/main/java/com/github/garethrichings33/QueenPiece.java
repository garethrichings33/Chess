package com.github.garethrichings33;

import java.util.ArrayList;
import java.util.HashMap;

public class QueenPiece extends Piece{
    private static final HashMap<PieceColour, String> iconResourceNames = new HashMap<>()
        {{put(PieceColour.BLACK, "/Chess_qdt60.png");
        put(PieceColour.WHITE, "/Chess_qlt60.png");}};
    public QueenPiece(PieceColour pieceColour) {
        super(pieceColour, iconResourceNames.get(pieceColour));
        setCanJump(false);
        setTakingMove(true);
    }

    @Override
    public boolean moveAllowed(int[] initialSquare, int[] finalSquare) {
        setCastlingMove(false);
        setPromotionMove(false);
        return false;
    }

    @Override
    protected ArrayList<int[]> getAllowedFinalSquares(int[] initialSquare) {
        return null;
    }
}
