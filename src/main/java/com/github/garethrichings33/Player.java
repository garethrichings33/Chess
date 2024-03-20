package com.github.garethrichings33;

import java.util.HashMap;
import java.util.Set;


public class Player {
    private final String name;
    private PieceColour pieceColour;
    private String pieceColourString;
    private HashMap<String, Piece> pieces;
    private boolean inCheck;
    private boolean inCheckMate;
    private int initialRank;
    private int initialPawnRank;
    private int numberOfPromotions;
    private float pointsWon;
    public Player(String name, PieceColour pieceColour) {
        this.name = name;
        this.inCheck = false;
        this.inCheckMate = false;
        this.numberOfPromotions = 0;
        this.pieceColour = pieceColour;
        if(pieceColour == PieceColour.WHITE)
            initialiseWhite();
        else
            initialiseBlack();
        initialisePieces();
    }
    private void initialiseWhite(){
        pieceColourString = "White";
        initialRank = 7;
        initialPawnRank = 6;
    }
    private void  initialiseBlack(){
        pieceColourString = "Black";
        initialRank = 0;
        initialPawnRank = 1;
    }
    private void initialisePieces() {
        pieces = new HashMap<>();
        pieces.put("King", new KingPiece(pieceColour, new int[]{initialRank, 4}, "King"));
        pieces.put("Queen", new QueenPiece(pieceColour, new int[]{initialRank, 3}, "Queen"));
        pieces.put("Bishop_Kings", new BishopPiece(pieceColour, SquareColour.BLACK,
                new int[]{initialRank, 5}, "Bishop_Kings"));
        pieces.put("Bishop_Queens", new BishopPiece(pieceColour, SquareColour.WHITE,
                new int[]{initialRank, 2}, "Bishop_Queens"));
        pieces.put("Knight_Kings", new KnightPiece(pieceColour, new int[]{initialRank, 6}, "Knight_Kings"));
        pieces.put("Knight_Queens", new KnightPiece(pieceColour, new int[]{initialRank, 1}, "Knight_Queens"));
        pieces.put("Rook_Kings", new RookPiece(pieceColour, new int[]{initialRank, 7}, "Rook_Kings"));
        pieces.put("Rook_Queens", new RookPiece(pieceColour, new int[]{initialRank, 0}, "Rook_Queens"));
        String pawnName;
        for(int i = 0; i < 8; i++) {
            pawnName = "Pawn_" + Character.toString((char) (65 + i));
            pieces.put(pawnName, new PawnPiece(pieceColour, new int[]{initialPawnRank, i}, pawnName));
        }
    }
    public void playerNewGame(){
        this.inCheck = false;
        this.inCheckMate = false;
        this.numberOfPromotions = 0;
        if(pieceColour == PieceColour.WHITE) {
            this.pieceColour = PieceColour.BLACK;
            initialiseBlack();
        }
        else{
            this.pieceColour = PieceColour.WHITE;
            initialiseWhite();
        }
        initialisePieces();
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
    public boolean isInCheckMate() {
        return inCheckMate;
    }
    public void setInCheckMate(boolean inCheckMate) {
        this.inCheckMate = inCheckMate;
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
    public void addHalfPoint(){
        pointsWon += 0.5;
    }
    public float getPointsWon() {
        return pointsWon;
    }
}
