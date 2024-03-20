package com.github.garethrichings33;

import java.util.HashMap;
import java.util.Set;


public class Player {
    private final String name;
    private PieceColour pieceColour;
    private String pieceColourString;
    private HashMap<String, Piece> pieces;
    private boolean inCheck;
    private final int initialRank;
    private final int initialPawnRank;
    private int numberOfPromotions;
    private int pointsWon;
    public Player(String name, PieceColour pieceColour) {
        this.name = name;
        this.pieceColour = pieceColour;
        this.inCheck = false;
        this.numberOfPromotions = 0;
        this.pointsWon = 0;
        if(pieceColour == PieceColour.WHITE) {
            pieceColourString = "White";
            initialRank = 7;
            initialPawnRank = 6;
        }
        else {
            pieceColourString = "Black";
            initialRank = 0;
            initialPawnRank = 1;
        }

        initialisePieces();
    }
    private void initialisePieces() {
        pieces = new HashMap<>();
        pieces.put("King", new KingPiece(pieceColour, new int[]{initialRank, 4}, "King"));
        pieces.put("Queen", new QueenPiece(pieceColour, new int[]{initialRank, 3}, "Queen"));
        pieces.put("Kings_Bishop", new BishopPiece(pieceColour, SquareColour.BLACK, new int[]{initialRank, 5}, "Kings_Bishop"));
        pieces.put("Queens_Bishop", new BishopPiece(pieceColour, SquareColour.WHITE, new int[]{initialRank, 2}, "Queens_Bishop"));
        pieces.put("Kings_Knight", new KnightPiece(pieceColour, new int[]{initialRank, 6}, "Kings_Knight"));
        pieces.put("Queens_Knight", new KnightPiece(pieceColour, new int[]{initialRank, 1}, "Queens_Knight"));
        pieces.put("Kings_Rook", new RookPiece(pieceColour, new int[]{initialRank, 7}, "Kings_Rook"));
        pieces.put("Queens_Rook", new RookPiece(pieceColour, new int[]{initialRank, 0}, "Queens_Rook"));
        String pawnName;
        for(int i = 0; i < 8; i++) {
            pawnName = "Pawn_" + Character.toString((char) (65 + i));
            pieces.put(pawnName, new PawnPiece(pieceColour, new int[]{initialPawnRank, i}, pawnName));
        }
    }
    public String getName() {
        return name;
    }
    public PieceColour getPieceColour() {
        return pieceColour;
    }
    public void setPieceColour(PieceColour pieceColour) {
        this.pieceColour = pieceColour;
    }
    public void addPromotion(){
        numberOfPromotions++;
    }
    public int getNumberOfPromotions() {
        return numberOfPromotions;
    }
    public String getPieceColourString() {
        return pieceColourString;
    }
    public boolean isInCheck() {
        return inCheck;
    }
    public void setInCheck(boolean inCheck) {
        this.inCheck = inCheck;
    }
    public Piece getPiece(String key){
        if(pieces.containsKey(key))
            return pieces.get(key);
        else
            return null;
    }
    public void addPiece(String pieceName, Piece piece){
        if(piece == null)
            return;
        pieces.put(pieceName, piece);
    }
    public void removePiece(String pieceName){
        pieces.remove(pieceName);
    }
    public Set<String> getPieceNames(){
        return pieces.keySet();
    }
    public void addPoint(){
        pointsWon++;
    }
    public int getPointsWon() {
        return pointsWon;
    }
}
