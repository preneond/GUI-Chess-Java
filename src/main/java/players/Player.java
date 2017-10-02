package players;

import core.Game;
import core.Move;
import core.Position;
import enums.PieceColor;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ondrej Prenek on 25.2.2016.
 */
public abstract class Player implements Serializable {
    private final String name;
    protected Game game;
    protected PieceColor pieceColor;
    private static final long serialVersionUID = 11L;

    public Player(String name) {
        this.name = name;
    }

    /**
     * @return Move, which player has played
     */
    public abstract Move getMove();

    /**
     * @return name of the Player
     */
    public String getName() {
        return name;
    }

    /**
     * @return color which has his pieces in game
     */
    public PieceColor getPieceColor() {
        return pieceColor;
    }

    /**
     * Set color which will have his pieces in game
     *
     * @param pieceColor color of his pieces
     */
    public void setPieceColor(PieceColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    /**
     * Set current game
     *
     * @param game current game
     */
    public void setGame(Game game) {
        this.game = game;
    }
}
