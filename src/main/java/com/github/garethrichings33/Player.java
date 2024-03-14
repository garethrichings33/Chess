package com.github.garethrichings33;

public class Player {
    private String name;
    private PieceColour pieceColour;

    private String pieceColourString;

    private boolean inCheck;
    int numberOfPromotions;
    public Player(String name, PieceColour pieceColour) {
        this.name = name;
        this.pieceColour = pieceColour;
        this.inCheck = false;
        this.numberOfPromotions = 0;
        if(pieceColour == PieceColour.WHITE)
            pieceColourString = "White";
        else
            pieceColourString = "Black";
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
}
