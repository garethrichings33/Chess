package com.github.garethrichings33;

public class Board {

    private Square[][] board;

    public Board() {
        board = new Square[8][8];
        initialiseBoard();
    }

    private void initialiseBoard() {
        Character rank;
        int file;
        for(int i = 0; i < 8; i++)
            for(int j =0; j < 8; j++) {
                rank = (char)(65+j);
                file = 8-i;
                if ((i + j) % 2 == 0)
                    board[i][j] = new Square(SquareColour.WHITE, rank, file);
                else
                    board[i][j] = new Square(SquareColour.BLACK, rank, file);
            }
    }

    public Square getSquare(int i, int j){
        return board[i][j];
    }
}
