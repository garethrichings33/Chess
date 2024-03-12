package com.github.garethrichings33;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public abstract class Piece {
    protected Image pieceIcon;
    protected String iconResourceName;

    protected final PieceColour colour;
    public Piece(PieceColour colour, String iconResourcename) {
        this.colour = colour;
        try {
            if(colour == PieceColour.WHITE)
                this.iconResourceName = "/Chess_klt60.png";
            else
                this.iconResourceName = "/Chess_kdt60.png";

            pieceIcon = new ImageIcon(ImageIO.read(KingPiece.class.getResourceAsStream(this.iconResourceName)))
                    .getImage();
        }
        catch(IOException excp){
            pieceIcon = new ImageIcon().getImage();
        }
    }

    public abstract boolean moveAllowed(int[] initialSquare, int[] finalSquare);

    public Image getPieceIcon() {
        return pieceIcon;
    }

    public PieceColour getColour() {
        return colour;
    }
}
