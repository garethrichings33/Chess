package com.github.garethrichings33;

public class Move {
    private final boolean castlingMove;
    private final boolean promotionMove;
    private final boolean takingOnlyMove;
    private final boolean takingMove;
    private final boolean jumpingMove;
    private final boolean validMove;
    public Move() {
        this.castlingMove = false;
        this.promotionMove = false;
        this.takingOnlyMove = false;
        this.takingMove = false;
        this.jumpingMove = false;
        this.validMove = false;
    }

    public Move(boolean castlingMove, boolean promotionMove, boolean takingOnlyMove, boolean takingMove,
                boolean jumpingMove, boolean validMove) {
        this.castlingMove = castlingMove;
        this.promotionMove = promotionMove;
        this.takingOnlyMove = takingOnlyMove;
        this.takingMove = takingMove;
        this.jumpingMove = jumpingMove;
        this.validMove = validMove;
    }
    public boolean isCastlingMove() {
        return castlingMove;
    }
    public boolean isPromotionMove() {
        return promotionMove;
    }
    public boolean isTakingOnlyMove() {
        return takingOnlyMove;
    }
    public boolean isTakingMove() {
        return takingMove;
    }
    public boolean isJumpingMove() {
        return jumpingMove;
    }
    public boolean isValidMove() {
        return validMove;
    }
}
