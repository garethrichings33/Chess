package com.github.garethrichings33;

import java.util.ArrayList;
import java.util.Arrays;
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
        setCastlingMove(false);
        setPromotionMove(false);
        setTakingOnlyMove(false);
    }

    @Override
    public boolean moveAllowed(int[] initialSquare, int[] finalSquare) {
        var allowedFinalSquares = getAllowedFinalSquares(initialSquare);
        boolean validMove = targetSquareValid(finalSquare, allowedFinalSquares);

        return validMove;
    }

    @Override
    protected ArrayList<int[]> getAllowedFinalSquares(int[] initialSquare) {
        ArrayList<int[]> allowedSquares = new ArrayList<>();

        int[][] steps = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        int[] square;
        for(int i = 0; i < 4; i++) {
            square = Arrays.copyOf(initialSquare, initialSquare.length);
            while (onGrid(Vectors.sum(square, steps[i]))) {
                square = Vectors.sum(square, steps[i]);
                allowedSquares.add(square);
            }
        }

        return allowedSquares;
    }
}
