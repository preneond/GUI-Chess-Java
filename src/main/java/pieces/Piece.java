package pieces;

import core.Game;
import core.Move;
import core.Position;
import enums.PieceColor;
import enums.PieceType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ondrej Prenek on 23.2.2016.
 */
public abstract class Piece implements Serializable {
    String pgnID;
    private static final long serialVersionUID = 11L;
    protected PieceColor pieceColor;
    private PieceType pieceType;
    ArrayList<Move> possibleMoves;
    Position currentPosition;
    protected Game game;
    boolean onStartPosition;
    String imagePath;

    /**
     * Constructor
     *
     * @param pieceColor Piece's color
     * @param pieceType  Piece's type
     * @param position   Piece's current position
     * @param game       Game, where the piece is.
     */
    public Piece(PieceColor pieceColor, PieceType pieceType, Position position, Game game) {
        this.pieceColor = pieceColor;
        this.pieceType = pieceType;
        possibleMoves = new ArrayList<>();
        currentPosition = position;
        this.game = game;
        onStartPosition = true;
    }

    /**
     * fill arrayList with all possible moves of current piece
     */
    public abstract void addAllPossibleMoves();

    /**
     * set piece's current position
     */
    public void setCurrentPosition(Position to) {
        currentPosition = to;
    }

    /**
     * add move to arrayList possibleMoves
     *
     * @param destination end position in move of current piece
     */
    void addMove(Position destination) {
        Move move = new Move(currentPosition, destination);
        possibleMoves.add(move);
    }

    void addMove(Move move) {
        possibleMoves.add(move);
    }

    /**
     * @return possibleMoves- ArrayList of piece's possible moves
     */
    public ArrayList<Move> getMoves() {
        addAllPossibleMoves();
        return possibleMoves;
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }

    /**
     * @return enum PieceType of current piece
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * @return true if the current piece is on start position,false otherwise
     */
    public boolean isOnStartPosition() {
        return onStartPosition;
    }


    /**
     * Set if the piece is on start position, or not.
     *
     * @param onStartPosition true if the piece is on start position, false otherwise
     */
    public void setOnStartPosition(boolean onStartPosition) {
        this.onStartPosition = onStartPosition;

    }

    /**
     * Return current position of piece
     *
     * @return current position
     */
    public Position getCurrentPosition() {
        return currentPosition;
    }

    public String getImagePath() {
        return imagePath;
    }


    /**
     * @return ArrayList of valid moves- all possible moves,
     * when the king is not in danger after executing one of them
     */
    public List<Move> getValidMoves() {
        ArrayList<Move> validMoves = new ArrayList<>();
        for (Move move : getMoves()) {
            if (!game.isKingAfterMoveInDanger(move)) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    /**
     * return pieces's pgn
     */
    public String getPgnID() {
        return pgnID;
    }
}