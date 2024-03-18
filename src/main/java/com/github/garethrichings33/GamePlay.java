package com.github.garethrichings33;

import java.util.ArrayList;
import java.util.Arrays;
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
    private Stack<Turn> turns;
    private boolean inCheck;

    public GamePlay(ChessGUI gui) {
        this.gui = gui;
        board = new Square[8][8];
        initialisePlayers();
        initialiseBoard();
        addPieces();

        turns = new Stack<>();
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
        int[] takenSquareCoordinates = null;
        MoveTypes returnMove = MoveTypes.VALID;

        var piece = getSquare(fromSquareCoordinates).getPiece();
        if(piece == null)
            return MoveTypes.INVALID;

//      Check colour of piece matches player's colour
        var pieceColour = piece.getColour();
        if(players[activePlayer].getPieceColour() != pieceColour)
            return MoveTypes.INVALID;

//      Check move is within scope of the chosen piece.
        var move = piece.getMove(fromSquareCoordinates, toSquareCoordinates);
        if(!move.isValidMove())
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
            if(getSquare(toSquareCoordinates).getPiece().getColour() == pieceColour ||  !move.isTakingMove()) {
                return MoveTypes.INVALID;
            }
            else {
                pieceTaken = getSquare(toSquareCoordinates).getPiece();
                takenSquareCoordinates = Arrays.copyOf(toSquareCoordinates, toSquareCoordinates.length);
            }
        }

//      Check pawn for en passant.
        if(pieceTaken == null && move.isTakingOnlyMove()) {
            if (isEnPassant(fromSquareCoordinates)) {
                pieceTaken = getEnPassantTakenPiece();
                takenSquareCoordinates = pieceTaken.getCurrentSquare();
                returnMove = MoveTypes.ENPASSANT;
            }
            else
                returnMove = MoveTypes.INVALID;
        }

//      Check for a castling move
        Piece castledRook = null;
        int[] castledRookSquare = new int[2];
        if(move.isCastlingMove()){
            if(pieceTaken != null || players[activePlayer].isInCheck())
                return MoveTypes.INVALID;
            castledRook = getCastlingRook(fromSquareCoordinates, toSquareCoordinates);
            if(castledRook != null)
                if(castlingPathClear(piece, castledRook)) {
                    castledRookSquare = getCastledRookSquare(fromSquareCoordinates, toSquareCoordinates);
                    completeMove(piece, fromSquareCoordinates, castledRookSquare, null, null);
                    var checked = isPlayerChecked(activePlayer, castledRookSquare, null);
                    undoMove(piece,fromSquareCoordinates, castledRookSquare, null, null);
                    if(checked)
                        return MoveTypes.INVALID;

                    returnMove = MoveTypes.CASTLE;
                }
                else
                    return MoveTypes.INVALID;
        }

//      Check for pawn promotion.
        if(move.isPromotionMove() && (!move.isTakingMove() || pieceTaken != null)){
            var chosenPieceName = gui.promotePawn(fromSquareCoordinates, toSquareCoordinates);
            pieceTaken = piece;
            takenSquareCoordinates = Arrays.copyOf(fromSquareCoordinates, fromSquareCoordinates.length);
            piece = pawnPromotion(toSquareCoordinates, chosenPieceName, piece);
            returnMove = MoveTypes.PROMOTION;
        }

//      Complete the actual move if not a special move.
        if(returnMove == MoveTypes.VALID || returnMove == MoveTypes.ENPASSANT
                || returnMove == MoveTypes.PROMOTION || returnMove == MoveTypes.CASTLE) {
            completeMove(piece, fromSquareCoordinates, toSquareCoordinates, pieceTaken, takenSquareCoordinates);
            if(isPlayerChecked(activePlayer,
                    players[activePlayer].getPiece("King").getCurrentSquare(), pieceTaken)){
                undoMove(piece, fromSquareCoordinates, toSquareCoordinates, pieceTaken, takenSquareCoordinates);
                returnMove = MoveTypes.INVALID;
            }
            if(returnMove == MoveTypes.CASTLE)
                completeMove(castledRook, castledRook.getCurrentSquare(),
                        castledRookSquare, null, null);
            players[activePlayer].setInCheck(false);
        }

//        writeMove(fromSquareCoordinates, toSquareCoordinates, piece, pieceTaken);

        if(returnMove != MoveTypes.INVALID) {
            turns.push(new Turn(activePlayer, piece, pieceTaken, fromSquareCoordinates, toSquareCoordinates, returnMove, inCheck));
            if(pieceTaken != null)
                players[inactivePlayer].removePiece(pieceTaken.getPieceName());
            if(isPlayerChecked(inactivePlayer,
                    players[inactivePlayer].getPiece("King").getCurrentSquare(), null)) {
                players[inactivePlayer].setInCheck(true);
                returnMove = MoveTypes.CHECK;
            }
            piece.incrementMoves();
            activePlayer = (activePlayer + 1) % 2;
            inactivePlayer = (inactivePlayer + 1) % 2;
        }

        return returnMove;
    }

    private boolean isPlayerChecked(int playerIndex, int[] kingSquareCoords, Piece pieceTaken) {
        int otherPlayerIndex = (playerIndex + 1) % 2;
        var otherPlayerPieceNames = players[otherPlayerIndex].getPieceNames();
        String pieceTakenName = "";
        if(pieceTaken != null)
            pieceTakenName = pieceTaken.getPieceName();

        Piece piece;
        int[] currentPieceCoords;
        boolean canPieceTakeKing;
        Move move;
        for(var pieceName : otherPlayerPieceNames){
            if(pieceName.equals(pieceTakenName))
                continue;
            piece = players[otherPlayerIndex].getPiece(pieceName);
            currentPieceCoords = piece.getCurrentSquare();
            move = piece.getMove(currentPieceCoords, kingSquareCoords);
            canPieceTakeKing = move.isValidMove() && move.isTakingMove();
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
    public Piece pawnPromotion(int[] toCoordinates, String chosenPieceType, Piece promotedPiece) {
        Piece chosenPiece = null;
        Player player = players[activePlayer];
        player.addPromotion();
        String chosenPieceName = chosenPieceType + "_" + player.getNumberOfPromotions();
        switch (chosenPieceType){
            case "Queen":
                chosenPiece = new QueenPiece(player.getPieceColour(), toCoordinates, chosenPieceName);
                break;
            case "Bishop":
                chosenPiece = new BishopPiece(player.getPieceColour(),
                        board[toCoordinates[0]][toCoordinates[1]].getSquareColour(), toCoordinates, chosenPieceName);
                break;
            case "Rook":
                chosenPiece = new RookPiece(player.getPieceColour(), toCoordinates, chosenPieceName);
                break;
            case "Knight":
                chosenPiece = new KnightPiece(player.getPieceColour(), toCoordinates, chosenPieceName);
                break;
        }
        player.addPiece(chosenPieceName, chosenPiece);
        player.removePiece(promotedPiece.getPieceName());
        return chosenPiece;
    }
    private boolean isEnPassant(int[] fromSquareCoordinates) {
        if(turns.isEmpty())
            return false;

        Turn lastTurn = turns.peek();
        Piece lastMovedPiece = lastTurn.getMovedPiece();

//      Last moved piece must be a pawn.
        if(lastMovedPiece.getClass() != PawnPiece.class)
            return false;

//      Last moved piece must only have moved once.
        if(lastMovedPiece.getNumberOfMovesCompleted() != 1)
            return false;

//      Taking piece must move directly behind the last moved piece.
        int[] lastMoveFinalCoords = lastTurn.getFinalSquare();
        if(lastMoveFinalCoords[0] != fromSquareCoordinates[0] ||
                Math.abs(lastMoveFinalCoords[1] - fromSquareCoordinates[1]) != 1)
            return false;

//      Last moved piece must have moved 2 squares.
        int[] lastMoveInitialCoords = lastTurn.getInitialSquare();
        if(Math.abs(lastMoveFinalCoords[0] - lastMoveInitialCoords[0]) != 2)
            return false;

        return true;
    }
    private Piece getEnPassantTakenPiece() {
        int[] takenSquareCoords = turns.peek().getFinalSquare();
        return board[takenSquareCoords[0]][takenSquareCoords[1]].getPiece();
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
    private void undoMove(Piece movedPiece, int[] fromCoordinates, int[] toCoordinates,
                          Piece pieceTaken, int[] takenCoordinates){
        board[fromCoordinates[0]][fromCoordinates[1]].setPiece(movedPiece);
        players[activePlayer].getPiece(movedPiece.getPieceName()).setCurrentSquare(fromCoordinates);
        board[toCoordinates[0]][toCoordinates[1]].setPiece(null);
        if (pieceTaken != null) {
            players[inactivePlayer].addPiece(pieceTaken.getPieceName(), pieceTaken);
            board[takenCoordinates[0]][takenCoordinates[1]].setPiece(pieceTaken);
        }
    }
    public Player getActivePlayer() {
        return players[activePlayer];
    }
    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
    }
    private Piece getCastlingRook(int[] fromSquareCoordinates, int[] toSquareCoordinates) {
        String pieceName;
        if(fromSquareCoordinates[1] < toSquareCoordinates[1])
            pieceName = "Kings_Rook";
        else
            pieceName = "Queens_Rook";

        var piece = players[activePlayer].getPiece(pieceName);
        if(piece.getNumberOfMovesCompleted() != 0)
            return null;
        else
            return piece;
    }
    private boolean castlingPathClear(Piece king, Piece castledRook) {
        var kingsCoords = king.getCurrentSquare();
        var rookCoords = castledRook.getCurrentSquare();
        if(kingsCoords[1] < rookCoords[1])
            return true;

        int[] check = Vectors.sum(rookCoords, new int[]{0 ,1});
        return(board[check[0]][check[1]].getPiece() == null);
    }
    private int[] getCastledRookSquare(int[] fromSquareCoordinates, int[] toSquareCoordinates)
    {
        int[] coords = new int[2];
        coords[0] = fromSquareCoordinates[0];
        coords[1] = (fromSquareCoordinates[1] + toSquareCoordinates[1])/2;
        return coords;
    }
}
