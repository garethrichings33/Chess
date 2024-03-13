package com.github.garethrichings33;

import java.util.ArrayList;
import java.util.HashMap;

public class BishopPiece extends Piece{
    private static final HashMap<PieceColour, String> iconResourceNames = new HashMap<>()
        {{put(PieceColour.BLACK, "/Chess_bdt60.png");
        put(PieceColour.WHITE, "/Chess_blt60.png");}};

    SquareColour squareColour;
    public BishopPiece(PieceColour pieceColour, SquareColour squareColour) {
        super(pieceColour, iconResourceNames.get(pieceColour));
        this.squareColour = squareColour;
        setCanJump(false);
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
