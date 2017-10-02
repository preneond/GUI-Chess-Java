package pieces;

import enums.PieceColor;
import enums.PieceType;
import core.Game;
import core.Position;
import core.Move;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ondrej Prenek on 23.2.2016.
 */

public class Queen extends Piece implements Serializable {

    public Queen(PieceColor pieceColor, PieceType piece, Position position, Game game) {
        super(pieceColor, piece, position, game);
        pgnID = "Q";
        switch (pieceColor) {
            case BLACK:
                imagePath = this.getClass().getResource("/blackQueen.png").getPath();
                break;
            case WHITE:
                imagePath = this.getClass().getResource("/whiteQueen.png").getPath();
                break;
            default:
                break;
        }
    }

    @Override
    public void addAllPossibleMoves() {
        possibleMoves.clear();
        Bishop bishop = new Bishop(pieceColor, PieceType.BISHOP, currentPosition, game);
        Rook rook = new Rook(pieceColor, PieceType.ROOK, currentPosition, game);
        ArrayList<Move> bishopMoves = bishop.getMoves();
        ArrayList<Move> rookMoves;
        rookMoves = rook.getMoves();
        ArrayList<Move> allMoves = rookMoves;
        allMoves.addAll(bishopMoves);
        for (Move m : allMoves) {
            addMove(m.getTo());
        }

    }
}
