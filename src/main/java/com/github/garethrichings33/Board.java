package com.github.garethrichings33;

public class Board {

    private Square[][] board;

    public Board() {
        board = new Square[8][8];
        initialiseBoard();
    }

    private void initialiseBoard() {
        for(int i = 0; i < 8; i++)
            for(int j =0; j < 8; j++)
                if((i+j)%2 == 0)
                    board[i][j] = new Square(SquareColour.WHITE);
                else
                    board[i][j] = new Square(SquareColour.BLACK);
    }

    public Square getSquare(int i, int j){
        return board[i][j];
    }

    public class Square{
        private SquareColour squareColour;
        private boolean occupied;
        private Piece piece;

        public Square(SquareColour squareColour) {
            this.squareColour = squareColour;
            this.occupied = false;
        }

        public SquareColour getSquareColour() {
            return squareColour;
        }
    }

    private enum SquareColour{
        BLACK,
        WHITE
    }
}
