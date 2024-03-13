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
    private boolean isTakingOnlyMove;
    private boolean isCastlingMove;
    private boolean isPromotionMove;

    public Piece(PieceColour colour, String iconResourcename) {
        Image tempIcon;
        this.colour = colour;
        this.iconResourceName = iconResourcename;
        try {
            tempIcon = new ImageIcon(ImageIO.read(Piece.class.getResourceAsStream(this.iconResourceName)))
                    .getImage();
        }
        catch(IOException excp){
            tempIcon = new ImageIcon().getImage();
        }
        this.pieceIcon = tempIcon;
    }

    public abstract boolean moveAllowed(int[] initialSquare, int[] finalSquare);
    protected abstract ArrayList<int[]> getAllowedFinalSquares(int[] initialSquare);
    public abstract ArrayList<int[]> getVisitedSquares(int[] initialSquare, int[] finalSquare);
    public boolean targetSquareValid(int[] targetSquare, ArrayList<int[]> validTargetSquares){
        for(int[] vector : validTargetSquares)
            if (Arrays.equals(targetSquare, vector))
                return true;

        return false;
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
    public boolean isTakingOnlyMove() {
        return isTakingOnlyMove;
    }
    public void setTakingOnlyMove(boolean takingOnlyMove) {
        isTakingOnlyMove = takingOnlyMove;
    }
    public boolean isCastlingMove() {
        return isCastlingMove;
    }
    public void setCastlingMove(boolean castlingMove) {
        isCastlingMove = castlingMove;
    }
    public boolean isPromotionMove() {
        return isPromotionMove;
    }
    public void setPromotionMove(boolean promotionMove) {
        isPromotionMove = promotionMove;
    }
}
