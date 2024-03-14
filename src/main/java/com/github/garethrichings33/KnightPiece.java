package com.github.garethrichings33;

import java.util.ArrayList;
import java.util.HashMap;

public class KnightPiece extends Piece{
    private static final HashMap<PieceColour, String> iconResourceNames = new HashMap<>()
        {{put(PieceColour.BLACK, "/Chess_ndt60.png");
        put(PieceColour.WHITE, "/Chess_nlt60.png");}};
    public KnightPiece(PieceColour pieceColour, String currentSquare) {
        super(pieceColour, iconResourceNames.get(pieceColour), currentSquare);
        setCanJump(true);
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
        ArrayList<int[]> allowedSquares = new ArrayList<>();
        int[][] steps = {{2, 1}, {-2, 1}, {1, 2}, {-1, 2}, {-2, -1}, {2, -1}, {-1, -2}, {1, -2}};
        int[] square;
        for(int i = 0; i < steps.length; i++){
            square = Vectors.sum(initialSquare, steps[i]);
            if(onGrid(square))
                allowedSquares.add(square);
        }

        return allowedSquares;
    }

}
