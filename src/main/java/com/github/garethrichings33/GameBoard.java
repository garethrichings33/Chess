package com.github.garethrichings33;

import java.util.ArrayList;
import java.util.HashMap;

public class GameBoard {

    private Square[][] board;
    private HashMap<String, Piece> pieces;
    private Player[] players;
    private int activePlayer;

    public GameBoard() {
        board = new Square[8][8];
        initialiseBoard();

        pieces = new HashMap<>();
        createPieces();
        addPieces();

        initialisePlayers();
    }

    private void initialisePlayers() {
        players = new Player[2];
        players[0] = new Player("Player 1", PieceColour.WHITE);
        players[1] = new Player("Player 2", PieceColour.BLACK);
        activePlayer = 0;
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
        pieces.put("Black_Queen", new QueenPiece(PieceColour.BLACK));
        pieces.put("White_Queen", new QueenPiece(PieceColour.WHITE));
        pieces.put("Black_Kings_Bishop", new BishopPiece(PieceColour.BLACK, SquareColour.BLACK));
        pieces.put("Black_Queens_Bishop", new BishopPiece(PieceColour.BLACK, SquareColour.WHITE));
        pieces.put("White_Queens_Bishop", new BishopPiece(PieceColour.WHITE, SquareColour.BLACK));
        pieces.put("White_Kings_Bishop", new BishopPiece(PieceColour.WHITE, SquareColour.WHITE));
        pieces.put("Black_Kings_Knight", new KnightPiece(PieceColour.BLACK));
        pieces.put("Black_Queens_Knight", new KnightPiece(PieceColour.BLACK));
        pieces.put("White_Kings_Knight", new KnightPiece(PieceColour.WHITE));
        pieces.put("White_Queens_Knight", new KnightPiece(PieceColour.WHITE));
        pieces.put("Black_Kings_Rook", new RookPiece(PieceColour.BLACK));
        pieces.put("Black_Queens_Rook", new RookPiece(PieceColour.BLACK));
        pieces.put("White_Kings_Rook", new RookPiece(PieceColour.WHITE));
        pieces.put("White_Queens_Rook", new RookPiece(PieceColour.WHITE));
        for(int i = 0; i < 8; i++){
            pieces.put("Black_Pawn_" + Character.toString((char)(65+i)) , new PawnPiece(PieceColour.BLACK));
            pieces.put("White_Pawn_" + Character.toString((char)(65+i)) , new PawnPiece(PieceColour.WHITE));
        }
    }
    private void addPieces() {
        board[0][0].setPiece(pieces.get("Black_Queens_Rook"));
        board[0][1].setPiece(pieces.get("Black_Queens_Knight"));
        board[0][2].setPiece(pieces.get("Black_Queens_Bishop"));
        board[0][3].setPiece(pieces.get("Black_Queen"));
        board[0][4].setPiece(pieces.get("Black_King"));
        board[0][5].setPiece(pieces.get("Black_Kings_Bishop"));
        board[0][6].setPiece(pieces.get("Black_Kings_Knight"));
        board[0][7].setPiece(pieces.get("Black_Kings_Rook"));

        board[7][0].setPiece(pieces.get("White_Kings_Rook"));
        board[7][1].setPiece(pieces.get("White_Kings_Knight"));
        board[7][2].setPiece(pieces.get("White_Kings_Bishop"));
        board[7][3].setPiece(pieces.get("White_Queen"));
        board[7][4].setPiece(pieces.get("White_King"));
        board[7][5].setPiece(pieces.get("White_Queens_Bishop"));
        board[7][6].setPiece(pieces.get("White_Queens_Knight"));
        board[7][7].setPiece(pieces.get("White_Queens_Rook"));

        for(int i = 0; i < 8; i++){
            board[1][i].setPiece(pieces.get("Black_Pawn_" + Character.toString((char)(65+i))));
            board[6][i].setPiece(pieces.get("White_Pawn_" + Character.toString((char)(65+i))));
        }

    }
    public boolean movePiece(String fromButton, String toButton) {
        int[] fromSquareCoordinates = getSquareCoordinates(fromButton);
        int[] toSquareCoordinates = getSquareCoordinates(toButton);

//        ArrayList<Square> path = getPath(fromSquareCoordinates, toSquareCoordinates);

        var piece = getSquare(fromSquareCoordinates).getPiece();
        var pieceColour = piece.getColour();
        if(players[activePlayer].getPieceColour() != pieceColour)
            return false;

        if(!piece.moveAllowed(fromSquareCoordinates, toSquareCoordinates))
            return false;

        if(piece.isTakingMove() &&
                (getSquare(toSquareCoordinates).getPiece() == null ||
                        pieceColour == getSquare(toSquareCoordinates).getPiece().getColour()))
            return false;

        if(!piece.isTakingMove() && getSquare(toSquareCoordinates).getPiece() != null)
            return false;

        if(piece.getCanJump()){}

        if(piece.isCastlingMove()){}

        if(piece.isPromotionMove()){}

        board[toSquareCoordinates[0]][toSquareCoordinates[1]].setPiece(piece);
        board[fromSquareCoordinates[0]][fromSquareCoordinates[1]].setPiece(null);

        activePlayer = (activePlayer + 1) % 2;
        return true;
    }

    private ArrayList getPath(int[] fromSquareCoordinates, int[] toSquareCoordinates) {
        var path = new ArrayList<Square>();
        return path;
    }

    public Square getSquare(int i, int j){
        return board[i][j];
    }
    public Square getSquare(int[] coordinates){
        return board[coordinates[0]][coordinates[1]];
    }

    private int[] getSquareCoordinates(String squareButton) {
        int temp = Integer.parseInt(squareButton);
        int[] coordinates = new int[2];
        coordinates[0] = temp / 10;
        coordinates[1] = temp % 10;

        return coordinates;
    }

    public int getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
    }
}
