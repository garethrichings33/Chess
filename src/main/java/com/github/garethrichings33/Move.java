package com.github.garethrichings33;

public class Move {
    private final int player;
    private final Piece movedPiece;
    private final Piece takenPiece;
    private final String initialSquare;
    private final String finalSquare;
    private final MoveTypes movetype;

    private final boolean check;
    public Move(int player, Piece movedPiece, Piece takenPiece, String initialSquare, String finalSquare, MoveTypes movetype, boolean check) {
        this.player = player;
        this.movedPiece = movedPiece;
        this.takenPiece = takenPiece;
        this.initialSquare = initialSquare;
        this.finalSquare = finalSquare;
        this.movetype = movetype;
        this.check = check;
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
    public boolean isCheck() {
        return check;
    }
}
