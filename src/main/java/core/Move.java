package core;

import java.io.Serializable;

/**
 * Created by Ondrej Prenek on 18.3.2016.
 */
public class Move implements Serializable {
    private static final long serialVersionUID = 11L;
    private Position from, to;
    private boolean castling = false;
    private boolean enPassant = false;
    private boolean promotion = false;

    public Move(Position from, Position to) {
        this.to = to;
        this.from = from;

    }

    /**
     * @return start position
     */
    public Position getFrom() {
        return from;
    }

    /**
     * @return end position
     */
    public Position getTo() {
        return to;
    }

    /**
     * Set castling
     *
     * @param castling-true if the move is castling, false otherwise
     */
    public void setCastling(boolean castling) {
        this.castling = castling;
    }

    /**
     * Check if the move is en passant, or not
     *
     * @return true if the move is en passant, false otherwise
     */
    public boolean isEnPassant() {
        return enPassant;
    }

    /**
     * Set en passant.
     *
     * @param enPassant-true if the move is en passant,false otherwise.
     */
    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    /**
     * Check if the move is castling
     *
     * @return true if the move is castling,false otherwise
     */
    public boolean isCastling() {
        return castling;
    }

    /**
     * Check if the move is pawn promotion
     *
     * @return true if the move is pawn promotion, false otherwise
     */
    public boolean isPromotion() {
        return promotion;
    }


    /**
     * Set Pawn promotion
     *
     * @param promotion true if the move is pawn promotion,false otherwise
     */
    public void setPromotion(boolean promotion) {
        this.promotion = promotion;
    }
}
