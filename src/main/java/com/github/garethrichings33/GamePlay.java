package com.github.garethrichings33;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class GamePlay {
    private ChessGUI gui;
    private Square[][] board;
    private HashMap<String, Piece> pieces;
    private Player[] players;
    private int activePlayer;
    private int inactivePlayer;
//    private ArrayList<String> moveList;
    private Stack<Move> moves;
    private boolean inCheck;

    public GamePlay(ChessGUI gui) {
        this.gui = gui;
        board = new Square[8][8];
        initialisePlayers();
        initialiseBoard();
        addPieces();

        moves = new Stack<>();
        this.inCheck = false;
    }
    private void initialisePlayers() {
        players = new Player[2];
        players[0] = new Player("Player 1", PieceColour.WHITE);
        players[1] = new Player("Player 2", PieceColour.BLACK);
        activePlayer = 0;
        inactivePlayer = 1;
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
    private void addPieces() {
        int blackPlayerIndex;
        if(players[0].getPieceColour() == PieceColour.BLACK)
            blackPlayerIndex = 0;
        else
            blackPlayerIndex = 1;
        int whitePlayerIndex = (blackPlayerIndex + 1) % 2;

        board[0][0].setPiece(players[blackPlayerIndex].getPiece("Queens_Rook"));
        board[0][1].setPiece(players[blackPlayerIndex].getPiece("Queens_Knight"));
        board[0][2].setPiece(players[blackPlayerIndex].getPiece("Queens_Bishop"));
        board[0][3].setPiece(players[blackPlayerIndex].getPiece("Queen"));
        board[0][4].setPiece(players[blackPlayerIndex].getPiece("King"));
        board[0][5].setPiece(players[blackPlayerIndex].getPiece("Kings_Bishop"));
        board[0][6].setPiece(players[blackPlayerIndex].getPiece("Kings_Knight"));
        board[0][7].setPiece(players[blackPlayerIndex].getPiece("Kings_Rook"));

        board[7][0].setPiece(players[whitePlayerIndex].getPiece("Queens_Rook"));
        board[7][1].setPiece(players[whitePlayerIndex].getPiece("Queens_Knight"));
        board[7][2].setPiece(players[whitePlayerIndex].getPiece("Queens_Bishop"));
        board[7][3].setPiece(players[whitePlayerIndex].getPiece("Queen"));
        board[7][4].setPiece(players[whitePlayerIndex].getPiece("King"));
        board[7][5].setPiece(players[whitePlayerIndex].getPiece("Kings_Bishop"));
        board[7][6].setPiece(players[whitePlayerIndex].getPiece("Kings_Knight"));
        board[7][7].setPiece(players[whitePlayerIndex].getPiece("Kings_Rook"));

        for(int i = 0; i < 8; i++){
            board[1][i].setPiece(players[blackPlayerIndex]
                    .getPiece("Pawn_" + Character.toString((char)(65+i))));
            board[6][i].setPiece(players[whitePlayerIndex]
                    .getPiece("Pawn_" + Character.toString((char)(65+i))));
        }

    }
    public MoveTypes movePiece(String fromButton, String toButton) {
        int[] fromSquareCoordinates = getSquareCoordinates(fromButton);
        int[] toSquareCoordinates = getSquareCoordinates(toButton);
        MoveTypes returnMove = MoveTypes.VALID;

        var piece = getSquare(fromSquareCoordinates).getPiece();
        if(piece == null)
            return MoveTypes.INVALID;

//      Check colour of piece matches player's colour
        var pieceColour = piece.getColour();
        if(players[activePlayer].getPieceColour() != pieceColour)
            return MoveTypes.INVALID;

//      Check move is within scope of the chosen piece.
        if(!piece.moveAllowed(fromSquareCoordinates, toSquareCoordinates))
            return MoveTypes.INVALID;

//      Check path is clear for move for non-jumping pieces.
        if(!piece.getCanJump()){
            ArrayList<int[]> visitedSquares = piece.getVisitedSquares(fromSquareCoordinates, toSquareCoordinates);
            for(var visitedSquare : visitedSquares)
                if(getSquare(visitedSquare).getPiece() != null)
                    return MoveTypes.INVALID;
        }

//      Check a taking move. Must take a piece of the opposite colour.
        Piece pieceTaken = null;
        if(getSquare(toSquareCoordinates).getPiece() != null) {
            if(getSquare(toSquareCoordinates).getPiece().getColour() == pieceColour) {
                return MoveTypes.INVALID;
            }
            else {
                pieceTaken = getSquare(toSquareCoordinates).getPiece();
            }
        }

//      Check pawn for en passant.
        if(pieceTaken == null && piece.isTakingOnlyMove()) {
            if (isEnPassant(fromSquareCoordinates, toSquareCoordinates)) {
                enPassantTake(fromSquareCoordinates, toSquareCoordinates, piece);
                returnMove = MoveTypes.ENPASSANT;
            }
            else
                returnMove = MoveTypes.INVALID;
        }

//      Check for a castling move
        if(piece.isCastlingMove()){
            returnMove = MoveTypes.CASTLE;
        }

//      Check for pawn promotion.
        if(piece.isPromotionMove()){
            gui.promotePawn(fromSquareCoordinates, toSquareCoordinates);
            returnMove = MoveTypes.PROMOTION;
        }

//      Complete the actual move if not a special move.
        if(returnMove == MoveTypes.VALID) {
            completeMove(piece, fromSquareCoordinates, toSquareCoordinates, pieceTaken, toSquareCoordinates);
        }

//        writeMove(fromSquareCoordinates, toSquareCoordinates, piece, pieceTaken);

        if(returnMove != MoveTypes.INVALID) {
            moves.push(new Move(activePlayer, piece, pieceTaken, fromSquareCoordinates, toSquareCoordinates, returnMove, inCheck));
            if(pieceTaken != null)
                players[inactivePlayer].removePiece(pieceTaken.getPieceName());
            piece.incrementMoves();
            if(isPlayerChecked(inactivePlayer, players[inactivePlayer].getPiece("King").getCurrentSquare())) {
                players[inactivePlayer].setInCheck(true);
                returnMove = MoveTypes.CHECK;
            }
            activePlayer = (activePlayer + 1) % 2;
            inactivePlayer = (inactivePlayer + 1) % 2;
        }

        return returnMove;
    }

    private boolean isPlayerChecked(int playerIndex, int[] kingSquareCoords) {
        int otherPlayerIndex = (playerIndex + 1) % 2;
        var otherPlayerPieceNames = players[otherPlayerIndex].getPieceNames();

        Piece piece;
        int[] currentPieceCoords;
        boolean canPieceTakeKing;
        for(var pieceName : otherPlayerPieceNames){
            piece = players[otherPlayerIndex].getPiece(pieceName);
            currentPieceCoords = piece.getCurrentSquare();
            canPieceTakeKing = piece.moveAllowed(currentPieceCoords, kingSquareCoords);
            if(canPieceTakeKing && !piece.getCanJump()) {
                ArrayList<int[]> visitedSquares = piece.getVisitedSquares(currentPieceCoords, kingSquareCoords);
                for (var visitedSquare : visitedSquares)
                    canPieceTakeKing = canPieceTakeKing && getSquare(visitedSquare).getPiece() == null;
            }
            if(canPieceTakeKing)
                return true;
        }
        return false;
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
    private void writeMove(int[] fromSquareCoordinates, int[] toSquareCoordinates, Piece piece, Piece pieceTaken) {
    }
    public void pawnPromotion(int[] fromCoordinates, int[] toCoordinates, String chosenPieceType) {
        Piece promotedPiece = null;
        Player player = players[activePlayer];
        player.addPromotion();
        String pieceName = chosenPieceType + "_" + player.getNumberOfPromotions();
        switch (chosenPieceType){
            case "Queen":
                promotedPiece = new QueenPiece(player.getPieceColour(), toCoordinates, pieceName);
                break;
            case "Bishop":
                promotedPiece = new BishopPiece(player.getPieceColour(),
                        board[toCoordinates[0]][toCoordinates[1]].getSquareColour(), toCoordinates, pieceName);
                break;
            case "Rook":
                promotedPiece = new RookPiece(player.getPieceColour(), toCoordinates, pieceName);
                break;
            case "Knight":
                promotedPiece = new KnightPiece(player.getPieceColour(), toCoordinates, pieceName);
                break;
        }
        player.addPiece(pieceName, promotedPiece);
        completeMove(promotedPiece, fromCoordinates, toCoordinates, null, null);
    }
    private boolean isEnPassant(int[] fromSquareCoordinates, int[] toSquareCoordinates) {
        if(moves.isEmpty())
            return false;

        Move lastMove = moves.peek();
        Piece lastMovedPiece = lastMove.getMovedPiece();

//      Last moved piece must be a pawn.
        if(lastMovedPiece.getClass() != PawnPiece.class)
            return false;

//      Last moved piece must only have moved once.
        if(lastMovedPiece.getNumberOfMoves() != 1)
            return false;

//      Taking piece must move directly behind the last moved piece.
        int[] lastMoveFinalCoords = lastMove.getFinalSquare();
        if(lastMoveFinalCoords[0] != fromSquareCoordinates[0] ||
                Math.abs(lastMoveFinalCoords[1] - fromSquareCoordinates[1]) != 1)
            return false;

//      Last moved piece must have moved 2 squares.
        int[] lastMoveInitialCoords = lastMove.getInitialSquare();
        if(Math.abs(lastMoveFinalCoords[0] - lastMoveInitialCoords[0]) != 2)
            return false;

        return true;
    }
    private void enPassantTake(int[] fromSquareCoordinates, int[] toSquareCoordinates, Piece piece) {
        int[] takenSquareCoords = moves.peek().getFinalSquare();
        var pieceTaken = board[takenSquareCoords[0]][takenSquareCoords[1]].getPiece();
        completeMove(piece, fromSquareCoordinates, toSquareCoordinates, pieceTaken, takenSquareCoords);
    }

    private void completeMove(Piece movedPiece, int[] fromCoordinates, int[] toCoordinates,
                                Piece pieceTaken, int[] takenCoordinates){

        if(pieceTaken != null){
            board[takenCoordinates[0]][takenCoordinates[1]].setPiece(null);
            players[inactivePlayer].removePiece(pieceTaken.getPieceName());
        }
        board[toCoordinates[0]][toCoordinates[1]].setPiece(movedPiece);
        players[activePlayer].getPiece(movedPiece.getPieceName()).setCurrentSquare(toCoordinates);
        board[fromCoordinates[0]][fromCoordinates[1]].setPiece(null);
    }
    public int getActivePlayer() {
        return activePlayer;
    }
    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
    }
}