package com.github.garethrichings33;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class QueenPiece extends Piece{
    private static final HashMap<PieceColour, String> iconResourceNames = new HashMap<>()
        {{put(PieceColour.BLACK, "/Chess_qdt60.png");
        put(PieceColour.WHITE, "/Chess_qlt60.png");}};
    public QueenPiece(PieceColour pieceColour) {
        super(pieceColour, iconResourceNames.get(pieceColour));
        setCanJump(false);
        setTakingOnlyMove(false);
        setCastlingMove(false);
        setPromotionMove(false);
    }

    @Override
    public boolean moveAllowed(int[] initialSquare, int[] finalSquare) {
        var allowedFinalSquares = getAllowedFinalSquares(initialSquare);
        return targetSquareValid(finalSquare, allowedFinalSquares);
    }

    @Override
    protected ArrayList<int[]> getAllowedFinalSquares(int[] initialSquare) {
        int[][] steps = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        return checkFinalSquares(initialSquare, steps);
    }
}
