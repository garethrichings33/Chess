package com.github.garethrichings33;

public class Move {
    private final Piece movedPiece;
    private final Piece takenPiece;
    private final String initialSquare;
    private final String finalSquare;
    private final MoveTypes movetype;
    public Move(Piece movedPiece, Piece takenPiece, String initialSquare, String finalSquare, MoveTypes movetype) {
        this.movedPiece = movedPiece;
        this.takenPiece = takenPiece;
        this.initialSquare = initialSquare;
        this.finalSquare = finalSquare;
        this.movetype = movetype;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public Piece getTakenPiece() {
        return takenPiece;
    }

    public String getInitialSquare() {
        return initialSquare;
    }

    public String getFinalSquare() {
        return finalSquare;
    }

    public MoveTypes getMovetype() {
        return movetype;
    }
}
