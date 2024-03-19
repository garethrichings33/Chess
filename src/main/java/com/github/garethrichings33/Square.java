package com.github.garethrichings33;

public class Square{
    private SquareColour squareColour;
    private int rank;
    private Character file;
    private boolean occupied;
    private int[] coordinates = new int[2];
    private Piece piece;

    public Square(SquareColour squareColour, int[] coordinates) {
        this.squareColour = squareColour;
        setCoordinates(coordinates);
        this.occupied = false;
        this.piece = null;
    }

    private void setCoordinates(int[] coordinates) {
        if(coordinates.length !=2)
            throw new IllegalArgumentException("Coordinates array incorrect size");
        this.coordinates[0] = coordinates[0];
        this.coordinates[1] = coordinates[1];
    }

    public String getFileAndRank() {
        rank = 8-coordinates[0];
        file = (char)(65+coordinates[1]);
        return file.toString() + Integer.toString(rank);
    }

    public SquareColour getSquareColour() {
        return squareColour;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        setOccupied(!isOccupied());
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}