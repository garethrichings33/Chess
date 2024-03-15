package com.github.garethrichings33;

import java.util.ArrayList;
import java.util.HashMap;

public class RookPiece extends Piece{
    private static final HashMap<PieceColour, String> iconResourceNames = new HashMap<>()
     {{put(PieceColour.BLACK, "/Chess_rdt60.png");
        put(PieceColour.WHITE, "/Chess_rlt60.png");}};
    public RookPiece(PieceColour pieceColour, int[] currentSquare, String pieceName) {
        super(pieceColour, iconResourceNames.get(pieceColour), currentSquare, pieceName);
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
        int[][] steps = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        return checkFinalSquares(initialSquare, steps);
    }
}
