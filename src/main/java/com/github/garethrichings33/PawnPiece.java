package com.github.garethrichings33;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PawnPiece extends Piece{
    int maxMove;
    private static final HashMap<PieceColour, String> iconResourceNames = new HashMap<>()
        {{put(PieceColour.BLACK, "/Chess_pdt60.png");
        put(PieceColour.WHITE, "/Chess_plt60.png");}};
    private final int direction;

    public PawnPiece(PieceColour pieceColour) {
        super(pieceColour, iconResourceNames.get(pieceColour));
        setCanJump(false);
        this.maxMove = 2;
        if(pieceColour == PieceColour.BLACK) {
            this.direction = 1;
        }
        else
            this.direction = -1;
    }

    @Override
    public boolean moveAllowed(int[] initialSquare, int[] finalSquare) {
        var allowedFinalSquares = getAllowedFinalSquares(initialSquare);
        boolean validMove = targetSquareValid(finalSquare, allowedFinalSquares);

        if(validMove) {
            setPromotionMove(finalSquare[0] == 0 || finalSquare[0] == 7);
            setTakingOnlyMove(finalSquare[1] != initialSquare[1]);
            setCastlingMove(false);
            maxMove = 1;
        }

        return validMove;
    }

    @Override
    protected ArrayList<int[]> getAllowedFinalSquares(int[] initialSquare) {
        ArrayList<int[]> allowedSquares = new ArrayList<>();

        int[] allowedSquare;
        for(int i = -1; i <= 1; i++) {
            allowedSquare = new int[2];
            allowedSquare[0] = initialSquare[0] + direction;
            allowedSquare[1] = initialSquare[1] + i;
            if(allowedSquare[0] >= 0 && allowedSquare[0] <= 7)
                allowedSquares.add(allowedSquare);
        }

        if(maxMove == 2){
            allowedSquare = new int[2];
            allowedSquare[0] = initialSquare[0] + 2 * direction;
            allowedSquare[1] = initialSquare[1];
            allowedSquares.add(allowedSquare);
        }

        return allowedSquares;
    }

}
