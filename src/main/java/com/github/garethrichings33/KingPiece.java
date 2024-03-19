package com.github.garethrichings33;

import java.util.HashMap;

public class KingPiece extends Piece{
    private static final HashMap<PieceColour, String> iconResourceNames = new HashMap<>()
        {{put(PieceColour.BLACK, "/Chess_kdt60.png");
        put(PieceColour.WHITE, "/Chess_klt60.png");}};

    public KingPiece(PieceColour pieceColour, int[] currentSquare, String pieceName) {
        super(pieceColour,iconResourceNames.get(pieceColour), currentSquare, pieceName);
        setCanJump(false);
        createMoveList();
    }
    @Override
    public Move getMove(int[] initialSquare, int[] finalSquare) {
        if(notOnGrid(finalSquare))
            return new Move();

        int[] moveCoordinates = getRequestedMoveCoordinates(initialSquare, finalSquare);
        PossibleMove possibleMove = getRequestedMove(moveCoordinates);

        if(possibleMove != null && !(possibleMove.isFirstTurnOnly() && getNumberOfMovesCompleted() != 0)) {
            return new Move(this, initialSquare, finalSquare, possibleMove.isCastlingOnly(),
                    possibleMove.isPromotionPossible(), possibleMove.isTakingOnly(),
                    possibleMove.isCanTake(), getCanJump(), true);
        }
        else
            return new Move();
    }
    @Override
    protected void createMoveList() {
        int[] coordinateChange;
        final boolean takeOnly = false;
        final boolean promotionPossible = false;
        boolean castling;
        boolean takingMove;
        boolean firstTurnOnly;
        for(int i = -1; i <= 1; i++) {
            castling = false;
            takingMove = true;
            firstTurnOnly = false;
            for (int j = -1; j <= 1; j++) {
                coordinateChange = new int[2];
                coordinateChange[0] = i;
                coordinateChange[1] = j;
                addPossibleMoveToList(new PossibleMove(takingMove, takeOnly, castling, promotionPossible,
                        firstTurnOnly, coordinateChange));
            }
        }

        for(int i : new int[]{-2, 2}){
            castling = true;
            takingMove = false;
            firstTurnOnly = true;
            coordinateChange = new int[2];
            coordinateChange[0] = 0;
            coordinateChange[1] = i;
            addPossibleMoveToList(new PossibleMove(takingMove, takeOnly, castling, promotionPossible,
                    firstTurnOnly, coordinateChange));
        }
    }
}
