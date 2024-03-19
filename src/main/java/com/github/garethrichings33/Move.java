package com.github.garethrichings33;

public class Move {
    private Piece movingPiece;
    private int[] fromCoordinates;
    private int[] toCoordinates;
    private final boolean castlingMove;
    private final boolean promotionMove;
    private final boolean takingOnlyMove;
    private final boolean takingMove;
    private final boolean jumpingMove;
    private boolean enPassantMove;
    private boolean validMove;
    private int[] takenSquareCoordinates;
    private Piece takenPiece;
    private int[] castledRookSquareCoordinates;
    private Piece castledRook;
    private Piece promotedPiece;
    public Move() {
        this.movingPiece = null;
        this.castlingMove = false;
        this.promotionMove = false;
        this.takingOnlyMove = false;
        this.takingMove = false;
        this.jumpingMove = false;
        this.validMove = false;
        this.takenSquareCoordinates = null;
        this.takenPiece = null;
        this.castledRookSquareCoordinates = null;
        this.castledRook = null;
        this.promotedPiece = null;
        this.enPassantMove = false;
    }

    public Move(Piece movingPiece, int[] fromCoordinates, int[] toCoordinates, boolean castlingMove,
                boolean promotionMove, boolean takingOnlyMove, boolean takingMove,
                boolean jumpingMove, boolean validMove) {
        this.movingPiece = movingPiece;
        this.fromCoordinates = fromCoordinates;
        this.toCoordinates = toCoordinates;
        this.castlingMove = castlingMove;
        this.promotionMove = promotionMove;
        this.takingOnlyMove = takingOnlyMove;
        this.takingMove = takingMove;
        this.jumpingMove = jumpingMove;
        this.validMove = validMove;
        this.takenSquareCoordinates = new int[2];
        this.takenPiece = null;
        this.castledRookSquareCoordinates = new int[2];
        this.castledRook = null;
        this.promotedPiece = null;
        this.enPassantMove = false;
    }
    public Piece getMovingPiece() {
        return movingPiece;
    }
    public void setMovingPiece(Piece movingPiece) {
        if(isPromotionMove())
            this.movingPiece = movingPiece;
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
    public void setInvalidMove(){
        validMove = false;
    }
    public void setTakenSquareCoordinates(int[] coordinates){
        if(isTakingMove()) {
            takenSquareCoordinates[0] = coordinates[0];
            takenSquareCoordinates[1] = coordinates[1];
        }
    }
    public int[] getTakenSquareCoordinates() {
        if(isTakingMove())
            return takenSquareCoordinates;
        else
            return null;
    }
    public Piece getTakenPiece() {
        if(isTakingMove())
            return takenPiece;
        else
            return null;
    }
    public void setTakenPiece(Piece takenPiece) {
        if(isTakingMove())
            this.takenPiece = takenPiece;
    }
    public int[] getCastledRookSquareCoordinates() {
        if(isCastlingMove())
            return castledRookSquareCoordinates;
        else
            return null;
    }
    public void setCastledRookSquareCoordinates(int[] castledRookSquareCoordinates) {
        if(isCastlingMove())
            this.castledRookSquareCoordinates = castledRookSquareCoordinates;
    }
    public Piece getCastledRook() {
        if(isCastlingMove())
            return castledRook;
        else
            return null;
    }
    public void setCastledRook(Piece castledRook) {
        if(isCastlingMove())
            this.castledRook = castledRook;
    }
    public Piece getPromotedPiece() {
        if(isPromotionMove())
            return promotedPiece;
        else
            return null;
    }
    public void setPromotedPiece(Piece promotedPiece) {
        if(isPromotionMove())
            this.promotedPiece = promotedPiece;
    }
    public boolean isEnPassantMove() {
        return enPassantMove;
    }
    public void setEnPassantMove(boolean enPassantMove) {
        this.enPassantMove = enPassantMove;
    }
    public int[] getFromCoordinates() {
        return fromCoordinates;
    }
    public int[] getToCoordinates() {
        return toCoordinates;
    }
}
