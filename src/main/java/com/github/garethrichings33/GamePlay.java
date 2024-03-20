package com.github.garethrichings33;

import java.util.*;

public class GamePlay {
    private ChessGUI gui;
    private Square[][] board;
    private Player[] players;
    private int activePlayer;
    private int inactivePlayer;
//    private ArrayList<String> moveList;
    private Stack<Turn> turns;
    private Move attackingMove;
    public GamePlay(ChessGUI gui) {
        this.gui = gui;
        initialisePlayers();
        board = createBoard();
        addInitialPieces();

        turns = new Stack<>();
    }
    private void initialisePlayers() {
        players = new Player[2];
        players[0] = new Player(gui.getPlayerName(1), PieceColour.WHITE);
        players[1] = new Player(gui.getPlayerName(2), PieceColour.BLACK);
        activePlayer = 0;
        inactivePlayer = 1;
    }
    private Square[][] createBoard() {
        var tempBoard = new Square[8][8];
        int[] coordinates;
        for(int i = 0; i < 8; i++)
            for(int j =0; j < 8; j++) {
                coordinates = new int[2];
                coordinates[0] = i;
                coordinates[1] = j;
                if ((i + j) % 2 == 0)
                    tempBoard[i][j] = new Square(SquareColour.WHITE, coordinates);
                else
                    tempBoard[i][j] = new Square(SquareColour.BLACK, coordinates);
            }
        return tempBoard;
    }
    private void addInitialPieces() {
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
    public MoveTypes gameTurn(String fromButton, String toButton) {
        int[] fromSquareCoordinates = getSquareCoordinates(fromButton);
        int[] toSquareCoordinates = getSquareCoordinates(toButton);

        if(noPieceSelected(fromSquareCoordinates))
            return MoveTypes.INVALID;
        if(wrongColourPiece(fromSquareCoordinates))
            return MoveTypes.INVALID;

        MoveTypes returnMove = MoveTypes.VALID;
        Piece movingPiece = getSquare(fromSquareCoordinates).getPiece();

        attackingMove = getMove(movingPiece, fromSquareCoordinates, toSquareCoordinates);
        movingPiece = attackingMove.getMovingPiece();

//        attackIntoCheck(attackingMove);

//      Complete the actual move if not a special move.
        if(attackingMove.isValidMove()) {
            completeMove(movingPiece, fromSquareCoordinates, toSquareCoordinates, attackingMove.getTakenPiece(),
                    attackingMove.getTakenSquareCoordinates());
            if(attackerIntoCheck(activePlayer, attackingMove)){
                undoMove(movingPiece, fromSquareCoordinates, toSquareCoordinates, attackingMove.getTakenPiece(),
                        attackingMove.getTakenSquareCoordinates());
                attackingMove.setInvalidMove();
            }
            if(attackingMove.isCastlingMove())
                completeMove(attackingMove.getCastledRook(), attackingMove.getCastledRook().getCurrentSquare(),
                        attackingMove.getCastledRookSquareCoordinates(), attackingMove.getTakenPiece(),
                        attackingMove.getTakenSquareCoordinates());
            players[activePlayer].setInCheck(false);
        }

//        writeMove(fromSquareCoordinates, toSquareCoordinates, piece, pieceTaken);

        if(attackingMove.isValidMove()) {
            if(attackingMove.getTakenPiece() != null)
                players[inactivePlayer].removePiece(attackingMove.getTakenPiece().getPieceName());
            if(defenderInCheck(inactivePlayer,
                    players[inactivePlayer].getPiece("King").getCurrentSquare())) {
                if(isPlayerCheckmated()){
                    endGame();
                    returnMove = MoveTypes.CHECKMATE;
                }
                else {
                    players[inactivePlayer].setInCheck(true);
                    returnMove = MoveTypes.CHECK;
                }
            }
            turns.push(new Turn(activePlayer, movingPiece, attackingMove.getTakenPiece(), fromSquareCoordinates,
                    toSquareCoordinates, returnMove, players[inactivePlayer].isInCheck()));
            movingPiece.incrementMoves();
            updatePlayerIndices();
        }

        if(!attackingMove.isValidMove())
            returnMove = MoveTypes.INVALID;
        return returnMove;
    }

    private void updatePlayerIndices() {
        activePlayer = (activePlayer + 1) % 2;
        inactivePlayer = (inactivePlayer + 1) % 2;
    }
    private Move getMove(Piece movingPiece, int[] fromSquareCoordinates, int[] toSquareCoordinates) {
        var move = movingPiece.getMove(fromSquareCoordinates, toSquareCoordinates);
        if(!move.isValidMove())
            return new InvalidMove();

        if(!movingPiece.getCanJump()){
            ArrayList<int[]> visitedSquares =
                    movingPiece.getVisitedSquares(fromSquareCoordinates, toSquareCoordinates);
            for(var visitedSquare : visitedSquares)
                if(getSquare(visitedSquare).getPiece() != null)
                    return new InvalidMove();
        }

        if(getSquare(toSquareCoordinates).getPiece() != null) {
            if(getSquare(toSquareCoordinates).getPiece().getColour() == movingPiece.getColour() ||
                    !move.isTakingMove())
                return new InvalidMove();
            else {
                move.setTakenPiece(getSquare(toSquareCoordinates).getPiece());
                move.setTakenSquareCoordinates(Arrays.copyOf(toSquareCoordinates, toSquareCoordinates.length));
            }
        }
        else if(move.isTakingOnlyMove()) {
            if (isEnPassant(fromSquareCoordinates)) {
                move.setTakenPiece(getEnPassantTakenPiece());
                move.setTakenSquareCoordinates(move.getTakenPiece().getCurrentSquare());
                move.setEnPassantMove(true);
            }
            else
                return new InvalidMove();
        }

        if(move.isCastlingMove()){
            if(move.getTakenPiece() != null || players[activePlayer].isInCheck())
                return new InvalidMove();
            var castledRook = getCastlingRook(fromSquareCoordinates, toSquareCoordinates);
            if(castledRook != null){
                if(castlingPathClear(movingPiece, castledRook)) {
                    var castledRookSquare = getCastledRookSquare(fromSquareCoordinates, toSquareCoordinates);
                    completeMove(movingPiece, fromSquareCoordinates, castledRookSquare, null, null);
                    var checked = attackerIntoCheck(activePlayer, move);
                    undoMove(movingPiece,fromSquareCoordinates, castledRookSquare, null, null);
                    if(checked)
                        return new InvalidMove();
                    move.setCastledRook(castledRook);
                    move.setCastledRookSquareCoordinates(castledRookSquare);
                }
                else
                    return new InvalidMove();
            }
            else
                return new InvalidMove();
        }

        if(move.isPromotionMove() && (!move.isTakingMove() || move.getTakenPiece() != null)){
            var chosenPieceName = gui.promotePawn(fromSquareCoordinates, toSquareCoordinates);
            move.setTakenPiece(movingPiece);
            move.setTakenSquareCoordinates(Arrays.copyOf(fromSquareCoordinates, fromSquareCoordinates.length));
            move.setMovingPiece(pawnPromotion(toSquareCoordinates, chosenPieceName, movingPiece));
        }

        return move;
    }
    private boolean noPieceSelected(int[] fromSquareCoordinates) {
        return getSquare(fromSquareCoordinates).getPiece() == null;
    }
    private boolean wrongColourPiece(int[] fromSquareCoordinates) {
        return players[activePlayer].getPieceColour() != getSquare(fromSquareCoordinates).getPiece().getColour();
    }
    private boolean isPlayerCheckmated() {
        var defendingPieceNames = players[inactivePlayer].getPieceNames();

        Piece defendingPiece;
        int[] initialSquare;
        int [] finalSquare;
        Move move;
        for(var defendingPieceName : defendingPieceNames){
            defendingPiece = players[inactivePlayer].getPiece(defendingPieceName);
            initialSquare = defendingPiece.getCurrentSquare();
            for(var possibleMove : defendingPiece.getPossibleMoveList()){
                finalSquare = Vectors.sum(initialSquare, possibleMove.getCoordinateChange());
                move = getMove(defendingPiece, initialSquare, finalSquare);
                if(!move.isValidMove())
                    continue;
                if(!attackerIntoCheck(inactivePlayer, move))
                    return false;
            }
        }
        return true;
    }
    private void endGame() {
        players[activePlayer].addPoint();
    }
    private boolean defenderInCheck(int defendingPlayerIndex, int[] defendingKingSquareCoords){
        int attackingPlayerIndex = (defendingPlayerIndex + 1) % 2;
        var attackingPlayerPieceNames = players[attackingPlayerIndex].getPieceNames();
        return canAttackerTakeKing(attackingPlayerIndex, attackingPlayerPieceNames, new HashSet<>(),
                defendingKingSquareCoords, new InvalidMove());
    }
    private boolean attackerIntoCheck(int attackingPlayerIndex, Move lastMove) {
        int defendingPlayerIndex = (attackingPlayerIndex + 1) % 2;
        var defendingPlayerPieceNames = players[defendingPlayerIndex].getPieceNames();

        int[] kingSquareCoords;
        Piece movingPiece = lastMove.getMovingPiece();
        if(movingPiece.getPieceName() == "King")
            kingSquareCoords = lastMove.getToCoordinates();
        else
            kingSquareCoords = players[attackingPlayerIndex].getPiece("King").getCurrentSquare();

        Set<String> ignorePieces = new HashSet<>();
        if(lastMove.getTakenPiece() != null)
            ignorePieces.add(lastMove.getTakenPiece().getPieceName());

        var inCheck = canAttackerTakeKing(defendingPlayerIndex, defendingPlayerPieceNames, ignorePieces,
                kingSquareCoords, lastMove);

        if(lastMove.isCastlingMove())
            inCheck = inCheck ||
                    canAttackerTakeKing(defendingPlayerIndex, defendingPlayerPieceNames, ignorePieces,
                            lastMove.getCastledRookSquareCoordinates(), lastMove);
        return inCheck;
    }
    private boolean canAttackerTakeKing(int attackingPlayerIndex, Set<String> attackingPlayerPieceNames,
                                        Set<String> piecesIgnored, int[] defendingKingSquareCoords,
                                        Move lastmove){
        Piece attackingPiece;
        for(var attackingPieceName : attackingPlayerPieceNames){
            if(piecesIgnored.contains(attackingPieceName))
                continue;
            attackingPiece = players[attackingPlayerIndex].getPiece(attackingPieceName);
            if(canPieceTakeKing(attackingPiece, attackingPiece.getCurrentSquare(), defendingKingSquareCoords,
                    lastmove))
                return true;
        }
        return false;
    }
    private boolean canPieceTakeKing(Piece attackingPiece, int[] attackingPieceCoords,
                                     int[] defendingKingSquareCoords, Move lastMove){
        var nextAttackingMove = attackingPiece.getMove(attackingPieceCoords, defendingKingSquareCoords);
        var canMoveTakeKing = nextAttackingMove.isValidMove() && nextAttackingMove.isTakingMove();
        if(canMoveTakeKing && !attackingPiece.getCanJump()) {
            ArrayList<int[]> visitedSquares =
                    attackingPiece.getVisitedSquares(attackingPieceCoords, defendingKingSquareCoords);
            for (var visitedSquare : visitedSquares)
                canMoveTakeKing = canMoveTakeKing &&
                        getSquare(visitedSquare).getPiece() == null &&
                        !Arrays.equals(visitedSquare, lastMove.getToCoordinates());
        }
        return canMoveTakeKing;
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
    public Player getInactivePlayer() {
        return players[inactivePlayer];
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
    private int[] getCastledRookSquare(int[] fromSquareCoordinates, int[] toSquareCoordinates) {
        int[] coords = new int[2];
        coords[0] = fromSquareCoordinates[0];
        coords[1] = (fromSquareCoordinates[1] + toSquareCoordinates[1])/2;
        return coords;
    }
}
