package com.github.garethrichings33;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class GameBoard {
    private ChessGUI gui;
    private Square[][] board;
    private HashMap<String, Piece> pieces;
    private Player[] players;
    private int activePlayer;
    private ArrayList<String> moveList;
    private Stack<Move> moves;

    public GameBoard(ChessGUI gui) {
        this.gui = gui;
        board = new Square[8][8];
        initialiseBoard();

        pieces = new HashMap<>();
        createPieces();
        addPieces();

        initialisePlayers();
        moveList = new ArrayList<>();
        moves = new Stack<>();
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
    public MoveTypes checkMove(String fromButton, String toButton) {
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
            gui.promotePawn(fromButton, toButton);
            returnMove = MoveTypes.PROMOTION;
        }

//      Complete the actual move if not a special move.
        piece.incrementMoves();
        if(returnMove == MoveTypes.VALID) {
            board[toSquareCoordinates[0]][toSquareCoordinates[1]].setPiece(piece);
            board[fromSquareCoordinates[0]][fromSquareCoordinates[1]].setPiece(null);
        }

        moves.push(new Move(piece, pieceTaken, fromButton, toButton, returnMove));
//        writeMove(fromSquareCoordinates, toSquareCoordinates, piece, pieceTaken);

        if(returnMove != MoveTypes.INVALID)
            activePlayer = (activePlayer + 1) % 2;

        return returnMove;
    }

    private void enPassantTake(int[] fromSquareCoordinates, int[] toSquareCoordinates, Piece piece) {
        Move lastMove = moves.peek();
        int[] takenSquareCoords = getSquareCoordinates(lastMove.getFinalSquare());
        board[toSquareCoordinates[0]][toSquareCoordinates[1]].setPiece(piece);
        board[fromSquareCoordinates[0]][fromSquareCoordinates[1]].setPiece(null);
        board[takenSquareCoords[0]][takenSquareCoords[1]].setPiece(null);
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
        int[] lastMoveFinalCoords = getSquareCoordinates((lastMove.getFinalSquare()));
        if(lastMoveFinalCoords[0] != fromSquareCoordinates[0] ||
                Math.abs(lastMoveFinalCoords[1] - fromSquareCoordinates[1]) != 1)
            return false;

//      Last moved piece must have moved 2 squares.
        int[] lastMoveInitialCoords = getSquareCoordinates((lastMove.getInitialSquare()));
        if(Math.abs(lastMoveFinalCoords[0] - lastMoveInitialCoords[0]) != 2)
            return false;

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

    private void writeMove(int[] fromSquareCoordinates, int[] toSquareCoordinates, Piece piece, Piece pieceTaken) {
    }

    public void pawnPromotion(String fromSquareButton, String toSquareButton, String chosenPieceType) {
        int[] toCoordinates = getSquareCoordinates(toSquareButton);
        int[] fromCoordinates = getSquareCoordinates(fromSquareButton);

        Piece promotedPiece = null;
        Player player = players[activePlayer];
        player.addPromotion();
        String pieceName = player.getPieceColourString() + player.getNumberOfPromotions() + "_" + chosenPieceType;
        switch (chosenPieceType){
            case "Queen":
                promotedPiece = new QueenPiece(player.getPieceColour());
                break;
            case "Bishop":
                promotedPiece = new BishopPiece(player.getPieceColour(), board[toCoordinates[0]][toCoordinates[1]].getSquareColour());
                break;
            case "Rook":
                promotedPiece = new RookPiece(player.getPieceColour());
                break;
            case "Knight":
                promotedPiece = new KnightPiece(player.getPieceColour());
                break;
        }
        pieces.put(pieceName, promotedPiece);
        board[toCoordinates[0]][toCoordinates[1]].setPiece(pieces.get(pieceName));
        board[fromCoordinates[0]][fromCoordinates[1]].setPiece(null);
    }

    public int getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
    }
}
