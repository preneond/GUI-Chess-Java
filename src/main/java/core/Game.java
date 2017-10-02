package core;

import enums.PieceColor;
import enums.PieceType;
import pieces.*;
import players.Player;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ondrej Prenek on 25.2.2016.
 */
public class Game implements Serializable {
    private static final long serialVersionUID = 11L;
    private String nameOfMatch;
    private Piece[][] board;
    int moveCounter;

    private Player whitePlayer;
    private Position whiteKingPosition;
    public Piece prevWhiteMovedPiece;
    Move prevWhiteMove;

    private Player blackPlayer;
    private Position blackKingPosition;
    public Piece prevBlackMovedPiece;
    Move prevBlackMove;

    private Player computerPlayer;
    private PieceColor currentPlayer;
    public boolean opponentIsComputer;


    private transient PrintWriter pgnWriter;
    private File pgnFile;

    //stopwatch data
    public byte seconds;
    public int minutes;
    public int hours;
    private final Logger logger;

    /**
     * Constructor
     */
    public Game() {
        logger = Logger.getLogger(this.getClass().getName());
        moveCounter = 0;
        currentPlayer = PieceColor.WHITE;
        board = new Piece[8][8];
        addPieces(PieceColor.WHITE);
        addPieces(PieceColor.BLACK);
    }

    /**
     * Switch current player
     * If the current player has white pieces, set current player to player with white pieces,
     */
    public void switchCurrentPlayer() {
        currentPlayer = (currentPlayer == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
    }


    private void addPieces(PieceColor pieceColor) {
        switch (pieceColor) {
            case BLACK:
                for (int i = 0; i < 8; i++) {
                    board[1][i] = new Pawn(pieceColor, PieceType.PAWN, new Position(1, i), this);
                }
                board[0][0] = new Rook(pieceColor, PieceType.ROOK, new Position(0, 0), this);
                board[0][1] = new Knight(pieceColor, PieceType.KNIGHT, new Position(0, 1), this);
                board[0][2] = new Bishop(pieceColor, PieceType.BISHOP, new Position(0, 2), this);
                board[0][3] = new Queen(pieceColor, PieceType.QUEEN, new Position(0, 3), this);
                board[0][4] = new King(pieceColor, PieceType.KING, new Position(0, 4), this);
                blackKingPosition = new Position(0, 4);
                board[0][5] = new Bishop(pieceColor, PieceType.BISHOP, new Position(0, 5), this);
                board[0][6] = new Knight(pieceColor, PieceType.KNIGHT, new Position(0, 6), this);
                board[0][7] = new Rook(pieceColor, PieceType.ROOK, new Position(0, 7), this);
                break;
            case WHITE:
                for (int i = 0; i < 8; i++) {
                    board[6][i] = new Pawn(pieceColor, PieceType.PAWN, new Position(6, i), this);
                }
                board[7][0] = new Rook(pieceColor, PieceType.ROOK, new Position(7, 0), this);
                board[7][1] = new Knight(pieceColor, PieceType.KNIGHT, new Position(7, 1), this);
                board[7][2] = new Bishop(pieceColor, PieceType.BISHOP, new Position(7, 2), this);
                board[7][3] = new Queen(pieceColor, PieceType.QUEEN, new Position(7, 3), this);
                board[7][4] = new King(pieceColor, PieceType.KING, new Position(7, 4), this);
                whiteKingPosition = new Position(7, 4);
                board[7][5] = new Bishop(pieceColor, PieceType.BISHOP, new Position(7, 5), this);
                board[7][6] = new Knight(pieceColor, PieceType.KNIGHT, new Position(7, 6), this);
                board[7][7] = new Rook(pieceColor, PieceType.ROOK, new Position(7, 7), this);
                break;
            default:
                break;
        }


    }

    /**
     * Check if the king is in check, or not
     *
     * @param color king's color
     * @return true if the king is in check,false otherwise
     */
    boolean isKingInDanger(PieceColor color) {
        Position kingPos = null;
        PieceColor opponent = null;
        switch (color) {
            case BLACK:
                kingPos = blackKingPosition;
                opponent = PieceColor.WHITE;
                break;
            case WHITE:
                kingPos = whiteKingPosition;
                opponent = PieceColor.BLACK;
                break;
            default:
                break;
        }
        ArrayList<Piece> pieceList = getPieceList(opponent);
        for (Piece piece : pieceList) {
            for (Move move : piece.getMoves()) {
                if (move.getTo().isEqual(kingPos)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void checkPawnPromotion(Move move) {
        int row = move.getTo().getRow();
        if (row == 7 || row == 0) {
            Position to = move.getTo();
            Piece piece = board[to.getRow()][to.getColumn()];
            PieceColor color = piece.getPieceColor();
            Queen queen = new Queen(color, PieceType.QUEEN, move.getTo(), this);
            int column = move.getTo().getColumn();
            board[row][column] = queen;
            move.setPromotion(true);
        }
    }

    /**
     * Make move on chess board.
     *
     * @param move updated move
     * @param movedPiece -the piece,which the player move
     */
    public void updateMove(Move move, Piece movedPiece) {
        checkCastling(move);
        checkEnPassant(move);
        if (move.isCastling()) {
            makeCastling(move, board[move.getFrom().getRow()][move.getFrom().getColumn()].getPieceType());
            return;
        }
        if (move.isEnPassant()) {
            makeEnpassant(move);
            return;
        }
        Position from = move.getFrom();
        Position to = move.getTo();
        movedPiece.setCurrentPosition(move.getTo());

        board[from.getRow()][from.getColumn()] = null;
        if (board[to.getRow()][to.getColumn()] != null && board[to.getRow()][to.getColumn()].getPieceType() == PieceType.KING) {
            switch (board[to.getRow()][to.getColumn()].getPieceColor()) {
                case BLACK:
                    blackKingPosition = null;
                    break;
                case WHITE:
                    whiteKingPosition = null;
                    break;
                default:
                    break;
            }
        }

        board[to.getRow()][to.getColumn()] = movedPiece;
        movedPiece.setOnStartPosition(false);

        switch (movedPiece.getPieceType()) {
            case KING:
                updateKingPosition(move);
                break;
            case PAWN:
                checkPawnPromotion(move);
                break;
            default:
                break;
        }

    }

    private void makeEnpassant(Move move) {
        Position from = move.getFrom();
        Position to = move.getTo();
        Piece movedPiece = board[from.getRow()][from.getColumn()];
        board[from.getRow()][to.getColumn()] = null;
        board[from.getRow()][from.getColumn()] = null;
        board[to.getRow()][to.getColumn()] = movedPiece;
        movedPiece.setCurrentPosition(to);

    }

    private void makeCastling(Move move, PieceType pieceType) {
        Position currentKingPos;
        Position currentTowerPos;
        int kingPosX;
        int kingPosY;
        int towerPosX;
        int towerPosY;
        if (pieceType == PieceType.KING) {
            kingPosX = move.getFrom().getRow();
            kingPosY = move.getFrom().getColumn();
            towerPosX = move.getTo().getRow();
            towerPosY = move.getTo().getColumn();
        } else {
            towerPosX = move.getFrom().getRow();
            towerPosY = move.getFrom().getColumn();
            kingPosX = move.getTo().getRow();
            kingPosY = move.getTo().getColumn();
        }
        Piece king = board[kingPosX][kingPosY];
        Piece rook = board[towerPosX][towerPosY];


        // jedna li se o vez vpravo
        if (towerPosY > kingPosY) {
            board[kingPosX][kingPosY + 2] = king;
            board[towerPosX][towerPosY - 2] = rook;

            currentKingPos = new Position(kingPosX, kingPosY + 2);
            currentTowerPos = new Position(towerPosX, towerPosY - 2);
            // jedna li se o vez vlevo
        } else {
            board[kingPosX][kingPosY - 2] = king;
            board[towerPosX][towerPosY + 3] = rook;

            currentKingPos = new Position(kingPosX, kingPosY - 2);
            currentTowerPos = new Position(towerPosX, towerPosY + 3);
        }

        board[kingPosX][kingPosY] = null;
        board[towerPosX][towerPosY] = null;


        switch (king.getPieceColor()) {
            case WHITE:
                if (pieceType == PieceType.KING) {
                    whiteKingPosition = currentKingPos;
                } else {
                    whiteKingPosition = currentTowerPos;
                }
                break;
            case BLACK:
                if (pieceType == PieceType.KING) {
                    blackKingPosition = currentKingPos;
                } else {
                    blackKingPosition = currentTowerPos;
                }
                break;
            default:
                break;
        }
        king.setOnStartPosition(false);
        rook.setOnStartPosition(false);
        king.setCurrentPosition(currentKingPos);
        rook.setCurrentPosition(currentTowerPos);
    }

    /**
     * @param move Move
     * @return true- if the move is valid-move is not out of board or the target field does not contain own piece,
     * false otherwise-move is out of board and target field contains own field
     */
    public boolean isMoveValid(Move move) {
        Position from = move.getFrom();
        Position to = move.getTo();
        if (board[from.getRow()][from.getColumn()] == null || board[from.getRow()][from.getColumn()].getPieceColor() != currentPlayer)
            return false;

        for (Move validMove : board[from.getRow()][from.getColumn()].getValidMoves()) {
            if (validMove.getTo().isEqual(to)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return WHITE or BLACK-PieceColor of player which is currently playing.
     */
    public PieceColor getCurrentPlayer() {
        return currentPlayer;
    }

    private void updateKingPosition(Move move) {
        Position to = move.getTo();
        Piece piece = board[to.getRow()][to.getColumn()];
        if (piece.getPieceType() == PieceType.KING) {
            switch (piece.getPieceColor()) {
                case BLACK:
                    blackKingPosition = to;
                    break;
                case WHITE:
                    whiteKingPosition = to;
                    break;
            }
        }
    }

    /**
     * Find out if end position contains piece  with different color such as on the start position
     *
     * @param startPos start position
     * @param endPos end position
     * @return true if the field contains enemy,false otherwise
     */
    public boolean containsEnemy(Position startPos, Position endPos) {
        if (board[startPos.getRow()][startPos.getColumn()] == null || board[endPos.getRow()][endPos.getColumn()] == null) {
            return false;
        }
        return !isPositionFree(endPos) && !(board[startPos.getRow()][startPos.getColumn()].getPieceColor() == board[endPos.getRow()][endPos.getColumn()].getPieceColor());

    }

    /**
     * Find out if end position contains the same piece such as on the start position
     *
     * @param startPos start position
     * @param endPos   end position
     * @return true if the end position contains own piece, false otherwise.
     */

    public boolean containsOwnPiece(Position startPos, Position endPos) {
        if (board[startPos.getRow()][startPos.getColumn()] == null || board[endPos.getRow()][endPos.getColumn()] == null) {
            return false;
        }
        return !isPositionFree(endPos) && (board[startPos.getRow()][startPos.getColumn()].getPieceColor() == board[endPos.getRow()][endPos.getColumn()].getPieceColor());

    }

    /**
     * @param color- set which Color pieces should be.
     * @return Arraylist of all pieces on board
     */
    public ArrayList<Piece> getPieceList(PieceColor color) {
        ArrayList<Piece> pieceList = new ArrayList<>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[x][y] != null && board[x][y].getPieceColor() == color) {
                    pieceList.add(board[x][y]);
                }
            }
        }
        return pieceList;
    }

    /**
     * Find out if the position does not contain any piece.
     *
     * @param position Position on board
     * @return true if the field on board is empty, false otherwise
     */
    public boolean isPositionFree(Position position) {
        return board[position.getRow()][position.getColumn()] == null;
    }

    /**
     * @return Chess board- two dimensional array of Pieces
     */
    public Piece[][] getBoard() {
        return board;
    }


    private void checkCastling(Move move) {
        Position to = move.getTo();
        if (board[to.getRow()][to.getColumn()] != null && board[to.getRow()][to.getColumn()].getPieceColor() == currentPlayer) {
            move.setCastling(true);
        }

    }


    private void checkEnPassant(Move move) {
        Position from = move.getFrom();
        Position to = move.getTo();
        if (board[from.getRow()][from.getColumn()].getPieceType() == PieceType.PAWN &&
                board[to.getRow()][to.getColumn()] == null &&
                from.getColumn() != to.getColumn()) {
                move.setEnPassant(true);
            }
        }


    /**
     * Check if the king is not in check after move.
     * Make the move and check if the king is ok, or not
     * and make the move back.
     *
     * @param move Move
     * @return true if the king is in check,false otherwise
     */
    public boolean isKingAfterMoveInDanger(Move move) {
        checkCastling(move);
        checkEnPassant(move);
        if (move.isCastling() || move.isEnPassant()) {
            return false;
        }
        int fromX = move.getFrom().getRow();
        int fromY = move.getFrom().getColumn();
        int toX = move.getTo().getRow();
        int toY = move.getTo().getColumn();
        Piece pieceTo = board[toX][toY];
        Piece pieceFrom = board[fromX][fromY];
        boolean pieceFromMoved = board[fromX][fromY].isOnStartPosition();
        PieceColor kingColor = board[fromX][fromY].getPieceColor();
        updateMove(move, pieceFrom);
        boolean kingInDanger = isKingInDanger(kingColor);
        updateMove(new Move(new Position(toX, toY), new Position(fromX, fromY)), pieceFrom);
        board[toX][toY] = pieceTo;
        pieceFrom.setOnStartPosition(pieceFromMoved);
        return kingInDanger;
    }

    /**
     * Check all valid moves from field on chess board
     *
     * @param row    Row
     * @param column Column
     * @return ArrayList of valid moves
     */
    public ArrayList<Move> getValidMoves(int row, int column) {
        ArrayList<Move> validMoves = new ArrayList<>();
        for (Move move : getBoard()[row][column].getMoves()) {
            if (!isKingAfterMoveInDanger(move)) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    /**
     * Set the player, which will have white pieces
     *
     * @param whitePlayer Player with white pieces
     */
    public void setWhitePlayer(Player whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    /**
     * Set the player, which will have black pieces
     *
     * @param blackPlayer Player with black pieces
     */
    public void setBlackPlayer(Player blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    /**
     * @return whitePlayer-player with white pieces
     */
    Player getWhitePlayer() {
        return whitePlayer;
    }

    /**
     * @return blackPlayer-player with black pieces
     */
    Player getBlackPlayer() {
        return blackPlayer;
    }

    /**
     * Check if the position is on the chessboard or not
     *
     * @param pos Position
     * @return true if the position is on the chessboard, false otherwise
     */
    public boolean isOutOfBoard(Position pos) {
        return ((pos.getRow() > 7) || (pos.getRow() < 0) || (pos.getColumn() > 7) || (pos.getColumn() < 0));
    }

    /**
     * If the game is between two persons, returns null.
     *
     * @return computerPlayer
     */
    public Player getComputerPlayer() {

        return computerPlayer;
    }

    /**
     * sets computer player
     *
     * @param computerPlayer computer player
     */
    void setComputerPlayer(Player computerPlayer) {
        this.computerPlayer = computerPlayer;
    }


    /**
     * Set name of match
     *
     * @param nameOfMatch- name of match
     */
    void setNameOfMatch(String nameOfMatch) {
        this.nameOfMatch = nameOfMatch;
    }


    public PrintWriter getPgnWriter() {
        return pgnWriter;
    }

    /**
     * Make PGN File with default header and prepare the file for writing the moves
     *
     */
    public void makePgnFile() {
        pgnFile = new File("game.pgn");
        try {
            pgnWriter = (new PrintWriter(pgnFile, "UTF-8"));
        } catch (FileNotFoundException e) {
            logger.log(Level.FINE, "File not found");
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.FINE, "Unsupported encoding");
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy.dd.MM");
        Date date = new Date();
        pgnWriter.println("[Event \"" + nameOfMatch + "\"]" +
                "\n[Site \"Prague, Czech Republic|CZE\"]" +
                "\n[Date " + dateFormat.format(date) + "]" +
                "\n[Round \"1\"]" +
                "\n[White:\"" + whitePlayer.getName() + "\"]" +
                "\n[Black:\"" + blackPlayer.getName() + "\"]" +
                "\n[Result: \"*\"]\n\n");

    }

    /**
     * @return pgnFile-file where moves are written
     */
    File getPgnFile() {
        return pgnFile;
    }

}