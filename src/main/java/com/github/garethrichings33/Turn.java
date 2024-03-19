package com.github.garethrichings33;

public class Turn {
    private final int player;
    private final Piece movedPiece;
    private final Piece takenPiece;
    private final int[] initialSquare;
    private final int[] finalSquare;
    private final MoveTypes movetype;
    private final boolean check;
    public Turn(int player, Piece movedPiece, Piece takenPiece, int[] initialSquare, int[] finalSquare, MoveTypes movetype, boolean check) {
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
    public int[] getInitialSquare() {
        return initialSquare;
    }
    public int[] getFinalSquare() {
        return finalSquare;
    }
    public MoveTypes getMovetype() {
        return movetype;
    }
    public boolean isCheck() {
        return check;
    }
}
