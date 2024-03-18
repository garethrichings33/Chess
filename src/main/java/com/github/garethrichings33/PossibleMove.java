package com.github.garethrichings33;

public class PossibleMove {
    private final boolean canTake;
    private final boolean takingOnly;
    private final boolean castlingOnly;
    private final boolean promotionPossible;
    private final boolean firstTurnOnly;
    private final int[] coordinateChange;

    public PossibleMove(boolean canTake, boolean takingOnly, boolean castlingOnly,
                        boolean promotionPossible, boolean firstTurnOnly, int[] coordinateChange) {
        this.canTake = canTake;
        this.takingOnly = takingOnly;
        this.castlingOnly = castlingOnly;
        this.promotionPossible = promotionPossible;
        this.coordinateChange = coordinateChange;
        this.firstTurnOnly = firstTurnOnly;
    }
    public boolean isCanTake() {
        return canTake;
    }
    public boolean isTakingOnly() {
        return takingOnly;
    }
    public boolean isCastlingOnly() {
        return castlingOnly;
    }
    public boolean isPromotionPossible() {
        return promotionPossible;
    }
    public boolean isFirstTurnOnly() {
        return firstTurnOnly;
    }
    public int[] getCoordinateChange() {
        return coordinateChange;
    }
    public boolean coordinatesEqual(int[] move){
        if(move.length != coordinateChange.length)
            throw new IllegalArgumentException();
        return move[0] == coordinateChange[0] && move[1] == coordinateChange[1];
    }
}
