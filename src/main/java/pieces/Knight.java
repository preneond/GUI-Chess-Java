package pieces;

import enums.PieceColor;
import enums.PieceType;
import core.Game;
import core.Move;
import core.Position;

import java.io.Serializable;

/**
 * Created by Ondrej Prenek on 23.2.2016.
 */
public class Knight extends Piece implements Serializable {
    public Knight(PieceColor pieceColor, PieceType piece, Position position, Game game) {
        super(pieceColor, piece, position, game);
        pgnID = "N";
        switch (pieceColor) {
            case BLACK:
                imagePath = this.getClass().getResource("/blackKnight.png").getPath();
                break;
            case WHITE:
                imagePath = this.getClass().getResource("/whiteKnight.png").getPath();
                break;
        }
    }

    @Override
    public void addAllPossibleMoves() {
        Position position;
        possibleMoves.clear();
        int row = currentPosition.getRow();
        int column = currentPosition.getColumn();

        position = new Position(row + 1, column + 2);
        if (isMovePossible(position)) {
            possibleMoves.add(new Move(currentPosition, position));
        }

        position = new Position(row + 1, column - 2);
        if (isMovePossible(position)) {
            possibleMoves.add(new Move(currentPosition, position));
        }

        position = new Position(row - 1, column + 2);
        if (isMovePossible(position)) {
            possibleMoves.add(new Move(currentPosition, position));
        }

        position = new Position(row - 1, column - 2);
        if (isMovePossible(position)) {
            possibleMoves.add(new Move(currentPosition, position));
        }

        position = new Position(row + 2, column + 1);
        if (isMovePossible(position)) {
            possibleMoves.add(new Move(currentPosition, position));
        }

        position = new Position(row + 2, column - 1);
        if (isMovePossible(position)) {
            possibleMoves.add(new Move(currentPosition, position));
        }

        position = new Position(row - 2, column + 1);
        if (isMovePossible(position)) {
            possibleMoves.add(new Move(currentPosition, position));
        }

        position = new Position(row - 2, column - 1);
        if (isMovePossible(position)) {
            possibleMoves.add(new Move(currentPosition, position));
        }
    }

    private boolean isMovePossible(Position pos) {
        return (!game.isOutOfBoard(pos) && (game.containsEnemy(currentPosition, pos) || game.isPositionFree(pos)));
    }
}
