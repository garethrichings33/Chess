package com.github.garethrichings33;

import java.util.Arrays;
import java.util.HashMap;

public class Turn {
    private final int player;
    private final Piece movedPiece;
    private final Piece takenPiece;
    private final int[] initialSquare;
    private final int[] finalSquare;
    private final MoveTypes movetype;
    private final boolean castling;
    private final boolean promotion;
    private final boolean check;
    private final boolean checkMate;
    private static final HashMap<String, String> pieceNotation = new HashMap<>() {{
        put("King", "K");
        put("Queen", "Q");
        put("Bishop", "B");
        put("Knight", "N");
        put("Rook", "R");
        put("Pawn", "");
    }};
    public Turn(int player, Piece movedPiece, Piece takenPiece, int[] initialSquare, int[] finalSquare,
                MoveTypes movetype, boolean check, boolean checkMate, boolean castling, boolean promotion) {
        this.player = player;
        this.movedPiece = movedPiece;
        this.takenPiece = takenPiece;
        this.initialSquare = initialSquare;
        this.finalSquare = finalSquare;
        this.movetype = movetype;
        this.check = check;
        this.checkMate = checkMate;
        this.castling = castling;
        this.promotion = promotion;
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
    @Override
    public String toString() {
        String pieceCode;
        String promotedPieceCode = "";
        if(!promotion)
            pieceCode = pieceNotation.get(getMovedPiece().getPieceName().split("_")[0]);
        else {
            pieceCode = pieceNotation.get("Pawn");
            promotedPieceCode = pieceNotation.get(getMovedPiece().getPieceName().split("_")[0]);
        }

        var initialFileAndRank = getFileAndRank(initialSquare);
        var finalFileAndRank = getFileAndRank(finalSquare);
        String takeString = "";
        if(takenPiece != null)
            takeString = "x";
        String castlingString = "";
        if(castling) {
            if (finalSquare[1] < initialSquare[1])
                castlingString = "0-0-0";
            else
                castlingString = "0-0";
        }
        String checkString = "";
        if(check)
            checkString = "+";
        String checkMateString = "";
        if(checkMate){
            checkString = "";
            checkString = "#";
        }

        return pieceCode + initialFileAndRank + takeString + finalFileAndRank + castlingString + checkString
                + checkMateString + promotedPieceCode + "\n";
    }

    private String getFileAndRank(int[] coordinates){
        return Character.toString((char)(97 + coordinates[1])) + (-1*(coordinates[0]-8));
    }

}
