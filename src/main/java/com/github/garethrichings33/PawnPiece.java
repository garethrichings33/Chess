package com.github.garethrichings33;

import java.util.HashMap;

public class PawnPiece extends Piece{
    private static final HashMap<PieceColour, String> iconResourceNames = new HashMap<>()
        {{put(PieceColour.BLACK, "/Chess_pdt60.png");
        put(PieceColour.WHITE, "/Chess_plt60.png");}};
    private final int direction;

    public PawnPiece(PieceColour pieceColour, int[] currentSquare, String pieceName) {
        super(pieceColour, iconResourceNames.get(pieceColour), currentSquare, pieceName);
        setCanJump(false);
        if(pieceColour == PieceColour.BLACK) {
            this.direction = 1;
        }
        else
            this.direction = -1;
        createMoveList();
    }

    @Override
    protected void createMoveList() {
        int[] coordinateChange;
        final boolean castlingPossible = false;
        boolean firstTurnOnly = false;
        boolean promotionPossible = true;
        boolean canTake;
        boolean takeOnly;
        for(int i = -1; i <= 1; i++) {
            coordinateChange = new int[2];
            coordinateChange[0] = direction;
            coordinateChange[1] = i;
            canTake = coordinateChange[1] != 0;
            takeOnly = canTake;
            addPossibleMoveToList(new PossibleMove(canTake, takeOnly, castlingPossible, promotionPossible,
                    firstTurnOnly, coordinateChange));
        }

        coordinateChange = new int[2];
        coordinateChange[0] = 2 * direction;
        coordinateChange[1] = 0;
        canTake = false;
        takeOnly = false;
        promotionPossible = false;
        firstTurnOnly = true;
        addPossibleMoveToList(new PossibleMove(canTake, takeOnly, castlingPossible,
                promotionPossible, firstTurnOnly, coordinateChange));
    }

    @Override
    public Move getMove(int[] initialSquare, int[] finalSquare) {
        if(notOnGrid(finalSquare))
            return new Move();

        int[] moveCoordinates = getRequestedMoveCoordinates(initialSquare, finalSquare);
        PossibleMove possibleMove = getRequestedMove(moveCoordinates);
        if(possibleMove != null && !(possibleMove.isFirstTurnOnly() && getNumberOfMovesCompleted() != 0)) {
            boolean promotionMove = (finalSquare[0] == 0 || finalSquare[0] == 7) && possibleMove.isPromotionPossible();
            return new Move(this, initialSquare, finalSquare, possibleMove.isCastlingOnly(),
                    promotionMove, possibleMove.isTakingOnly(), possibleMove.isTakingOnly(),
                    getCanJump(), true);
        }
        else
            return new Move();
    }

}
