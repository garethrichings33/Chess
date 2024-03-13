package com.github.garethrichings33;

import java.util.ArrayList;
import java.util.HashMap;

public class KingPiece extends Piece{

    private static final HashMap<PieceColour, String> iconResourceNames = new HashMap<>()
        {{put(PieceColour.BLACK, "/Chess_kdt60.png");
        put(PieceColour.WHITE, "/Chess_klt60.png");}};

    public KingPiece(PieceColour pieceColour) {
        super(pieceColour,iconResourceNames.get(pieceColour));
        setCanJump(false);
    }

    @Override
    public boolean moveAllowed(int[] initialSquare, int[] finalSquare) {
        setCastlingMove(true);
        setPromotionMove(false);
        setTakingOnlyMove(false);
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
