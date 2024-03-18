package com.github.garethrichings33;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Piece {
    private final Image pieceIcon;
    protected String iconResourceName;
    private final PieceColour colour;
    private boolean canJump;
    private int numberOfMovesCompleted;
    private int[] currentSquare;
    private final String pieceName;
    protected final ArrayList<PossibleMove> possibleMoveList;
    public Piece(PieceColour colour, String iconResourcename, int[] currentSquare, String pieceName) {
        Image tempIcon;
        this.colour = colour;
        this.iconResourceName = iconResourcename;
        this.currentSquare = currentSquare;
        this.pieceName = pieceName;
        try {
            tempIcon = new ImageIcon(ImageIO.read(Piece.class.getResourceAsStream(this.iconResourceName)))
                    .getImage();
        }
        catch(IOException excp){
            tempIcon = new ImageIcon().getImage();
        }
        this.pieceIcon = tempIcon;
        this.numberOfMovesCompleted = 0;
        possibleMoveList = new ArrayList<>();
    }
    public Move getMove(int[] initialSquare, int[] finalSquare){
        int[] moveCoordinates = getRequestedMoveCoordinates(initialSquare, finalSquare);
        PossibleMove possibleMove = getRequestedMove(moveCoordinates);

        if(possibleMove != null){
            return new Move(possibleMove.isCastlingOnly(), possibleMove.isPromotionPossible(),
                    possibleMove.isTakingOnly(), possibleMove.isCanTake(), getCanJump(), true);
        }
        else
            return new Move();
    };
    protected abstract void createMoveList();
    public ArrayList<int[]> getVisitedSquares(int[] initialSquare, int[] finalSquare){
        int[] step = Vectors.scaleToLargestValue(Vectors.difference(finalSquare, initialSquare));
        int[] visitedSquare = Arrays.copyOf(initialSquare, initialSquare.length);
        ArrayList<int[]> visitedSquares = new ArrayList<>();

        while(!Arrays.equals(Vectors.sum(visitedSquare, step), finalSquare)) {
            visitedSquare = Vectors.sum(visitedSquare, step);
            visitedSquares.add(visitedSquare);
        }

        return visitedSquares;
    }
    public PossibleMove getRequestedMove(int[] move){
        for(var possibleMove : possibleMoveList)
            if(possibleMove.coordinatesEqual(move))
                return possibleMove;
        return null;
    }
    public boolean getCanJump(){
        return canJump;
    };
    public void setCanJump(boolean canJump){
        this.canJump = canJump;
    }
    public Image getPieceIcon() {
        return pieceIcon;
    }
    public PieceColour getColour() {
        return colour;
    }
    public int getNumberOfMovesCompleted() {
        return numberOfMovesCompleted;
    }
    public void incrementMoves(){
        numberOfMovesCompleted++;
    }
    public int[] getCurrentSquare() {
        return currentSquare;
    }
    public void setCurrentSquare(int[] currentSquare) {
        this.currentSquare = currentSquare;
    }
    public String getPieceName() {
        return pieceName;
    }
    public void addPossibleMoveToList(PossibleMove possibleMove){
        possibleMoveList.add(possibleMove);
    }
    public int[] getRequestedMoveCoordinates(int[] initialCoordinates, int[] finalCoordinates){
        if(initialCoordinates.length != 2 || finalCoordinates.length != 2)
            throw new IllegalArgumentException();
        return Vectors.difference(finalCoordinates, initialCoordinates);
    }
}
