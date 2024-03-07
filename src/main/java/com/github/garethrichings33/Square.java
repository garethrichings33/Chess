package com.github.garethrichings33;

public class Square{
    private SquareColour squareColour;
    private Character rank;
    private int file;
    private boolean occupied;
    private Piece piece;

    public Square(SquareColour squareColour, Character rank, int file) {
        this.squareColour = squareColour;
        this.rank = rank;
        this.file = file;
        this.occupied = false;
    }
    public String getRankAndFile() {
        return rank.toString() + Integer.toString(file);
    }

    public SquareColour getSquareColour() {
        return squareColour;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}