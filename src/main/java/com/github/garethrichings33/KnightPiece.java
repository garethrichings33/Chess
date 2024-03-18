package com.github.garethrichings33;

import java.util.ArrayList;
import java.util.HashMap;

public class KnightPiece extends Piece{
    private static final HashMap<PieceColour, String> iconResourceNames = new HashMap<>()
        {{put(PieceColour.BLACK, "/Chess_ndt60.png");
        put(PieceColour.WHITE, "/Chess_nlt60.png");}};
    public KnightPiece(PieceColour pieceColour, int[] currentSquare, String pieceName) {
        super(pieceColour, iconResourceNames.get(pieceColour), currentSquare, pieceName);
        setCanJump(true);
        createMoveList();
    }

    @Override
    protected void createMoveList() {
        final boolean canTake = true;
        final boolean takeOnly = false;
        final boolean promotionPossible = false;
        final boolean castlingPossible = false;
        final boolean firstTurnDifferentMove = false;
        int[] coordinateChange;
        for(int i = -2; i <= 2; i++)
            for(int j = -2; j <= 2; j++){
                if(i == j || i == 0 || j == 0)
                    continue;
                coordinateChange = new int[2];
                coordinateChange[0] = i;
                coordinateChange[1] = j;
                addPossibleMoveToList(new PossibleMove(canTake, takeOnly, castlingPossible, promotionPossible,
                        firstTurnDifferentMove, coordinateChange));
            }
    }
}
