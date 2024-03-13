package com.github.garethrichings33;

import java.util.ArrayList;
import java.util.HashMap;

public class KnightPiece extends Piece{
    private static final HashMap<PieceColour, String> iconResourceNames = new HashMap<>()
        {{put(PieceColour.BLACK, "/Chess_ndt60.png");
        put(PieceColour.WHITE, "/Chess_nlt60.png");}};
    public KnightPiece(PieceColour pieceColour) {
        super(pieceColour, iconResourceNames.get(pieceColour));
        setCanJump(true);
        setTakingOnlyMove(false);
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

    @Override
    public ArrayList<int[]> getVisitedSquares(int[] initialSquare, int[] finalSquare) {
        return null;
    }
}
