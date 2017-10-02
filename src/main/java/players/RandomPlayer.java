package players;

import core.*;
import pieces.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by Ondrej Prenek on 26.2.2016.
 */
public class RandomPlayer extends Player implements Serializable {
    public RandomPlayer(String name) {
        super(name);
    }

    @Override
    public Move getMove() {
        ArrayList<Piece> pieces = game.getPieceList(pieceColor);
        Random random = new Random();
        ArrayList<Move> posMoves = new ArrayList<>();
        for (Piece piece : pieces) {
            posMoves.addAll(piece.getValidMoves());
        }
        return posMoves.get(random.nextInt(posMoves.size()));
    }

}
