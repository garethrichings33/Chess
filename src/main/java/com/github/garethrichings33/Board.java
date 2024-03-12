package com.github.garethrichings33;

import java.util.HashMap;

public class Board {

    private Square[][] board;

    private HashMap<String, Piece> pieces;

    public Board() {
        board = new Square[8][8];
        initialiseBoard();

        pieces = new HashMap<>();
        createPieces();
        addPieces();
    }

    private void initialiseBoard() {
        int[] coordinates = new int[2];
        for(int i = 0; i < 8; i++)
            for(int j =0; j < 8; j++) {
                coordinates[0] = i;
                coordinates[1] = j;
                if ((i + j) % 2 == 0)
                    board[i][j] = new Square(SquareColour.WHITE, coordinates);
                else
                    board[i][j] = new Square(SquareColour.BLACK, coordinates);
            }
    }

    private void createPieces() {
        pieces.put("Black_King", new KingPiece(PieceColour.BLACK));
        pieces.put("White_King", new KingPiece(PieceColour.WHITE));
    }

    private void addPieces() {
        board[0][4].setPiece(pieces.get("Black_King"));
        board[7][4].setPiece(pieces.get("White_King"));
    }

    public Square getSquare(int i, int j){
        return board[i][j];
    }
}
