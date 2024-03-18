package com.github.garethrichings33;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RookPiece extends Piece{
    private static final HashMap<PieceColour, String> iconResourceNames = new HashMap<>()
     {{put(PieceColour.BLACK, "/Chess_rdt60.png");
        put(PieceColour.WHITE, "/Chess_rlt60.png");}};
    public RookPiece(PieceColour pieceColour, int[] currentSquare, String pieceName) {
        super(pieceColour, iconResourceNames.get(pieceColour), currentSquare, pieceName);
        setCanJump(false);
        createMoveList();
    }

    @Override
    protected void createMoveList() {
        final boolean canTake = true;
        final boolean takeOnly = false;
        final boolean promotionPossible = false;
        final boolean castlingPossible = false;
        final boolean firstTurnDifferentMove = false;
        final int[][] steps = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        int[] coordinateChange;
        for(int i = 0; i < 4; i++)
            for(int j = 1; j <= 8; j++){
                coordinateChange = Vectors.multiplyByInteger(j, Arrays.copyOf(steps[i], 2));
                possibleMoveList.add(new PossibleMove(canTake, takeOnly, castlingPossible,
                        promotionPossible, firstTurnDifferentMove, coordinateChange));
            }
    }

}
