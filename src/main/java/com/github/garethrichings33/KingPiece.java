package com.github.garethrichings33;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class KingPiece extends Piece{

    private static final HashMap<PieceColour, String> iconResourceNames = new HashMap<>()
        {{put(PieceColour.BLACK, "/Chess_kdt60.png");
        put(PieceColour.WHITE, "/Chess_klt60.png");}};

    public KingPiece(PieceColour pieceColour, int[] currentSquare, String pieceName) {
        super(pieceColour,iconResourceNames.get(pieceColour), currentSquare, pieceName);
        setCanJump(false);
        setPromotionMove(false);
        setTakingOnlyMove(false);
        setTakingMove(true);
    }

    @Override
    public boolean moveAllowed(int[] initialSquare, int[] finalSquare) {
        var allowedFinalSquares = getAllowedFinalSquares(initialSquare);
        boolean validMove = targetSquareValid(finalSquare, allowedFinalSquares);

        setCastlingMove(Math.abs(finalSquare[1] - initialSquare[1]) == 2);
        return validMove;
    }

    @Override
    protected ArrayList<int[]> getAllowedFinalSquares(int[] initialSquare) {
        ArrayList<int[]> allowedSquares = new ArrayList<>();
        int[] square;
        for(int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                square = new int[2];
                square[0] = initialSquare[0] + i;
                square[1] = initialSquare[1] + j;
                if(onGrid(square) && !Arrays.equals(square, initialSquare)) {
                    allowedSquares.add(square);
                }
            }
        }

        if(getNumberOfMoves() == 0){
            for(int i : new int[]{-2, 2}){
                square = new int[2];
                square[0] = initialSquare[0];
                square[1] = initialSquare[1] + i;
                if(onGrid(square))
                    allowedSquares.add(square);
            }
        }

        return allowedSquares;
    }

}
