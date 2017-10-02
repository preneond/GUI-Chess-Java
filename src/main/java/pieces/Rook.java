package pieces;

import core.Game;
import core.Move;
import core.Position;
import enums.Direction;
import enums.PieceColor;
import enums.PieceType;

import java.io.Serializable;

/**
 * Created by Ondrej Prenek on 23.2.2016.
 */
public class Rook extends Piece implements Serializable {
    public Rook(PieceColor pieceColor, PieceType piece, Position position, Game game) {
        super(pieceColor, piece, position, game);
        pgnID = "R";
        switch (pieceColor) {
            case BLACK:
                imagePath = this.getClass().getResource("/blackRook.png").getPath();
                break;
            case WHITE:
                imagePath = this.getClass().getResource("/whiteRook.png").getPath();
                break;
            default:
                break;
        }
    }

    @Override
    public void addAllPossibleMoves() {
        possibleMoves.clear();
        checkDirection(Direction.UP);
        checkDirection(Direction.DOWN);
        checkDirection(Direction.LEFT);
        checkDirection(Direction.RIGHT);
        checkCastling();

    }


    private void checkCastling() {
        if (!onStartPosition) {
            return;
        }
        checkCastling(Direction.LEFT);
        checkCastling(Direction.RIGHT);
    }

    private void checkCastling(Direction direction) {
        int row = (pieceColor == PieceColor.WHITE) ? 7 : 0;
        Piece king;
        king = game.getBoard()[row][4];
        switch (direction) {
            case LEFT:
                if (king != null && king.onStartPosition) {
                    for (int i = 1; i < 4; i++) {
                        if (!game.isPositionFree(new Position(row, i))) {
                            return;
                        }
                    }
                    Position destination = new Position(row, 4);
                    Move move = new Move(currentPosition, destination);
                    move.setCastling(true);
                    addMove(move);
                }
                break;
            case RIGHT:
                if (king != null && king.onStartPosition) {
                    for (int i = 5; i < 7; i++) {
                        if (!game.isPositionFree(new Position(row, i))) {
                            return;
                        }
                    }
                    Position destination = new Position(row, 4);
                    Move move = new Move(currentPosition, destination);
                    move.setCastling(true);
                    addMove(move);
                }
                break;
        }

    }
    private void checkDirection(Direction direction) {
        switch (direction) {
            case UP:
                int column;
                column = currentPosition.getColumn();
                for (int i = currentPosition.getRow() - 1; i >= 0; i--) {
                    Position pos = new Position(i, column);
                    if (!checkPosition(pos)) {
                        return;

                    }
                }

                break;
            case DOWN:
                column = currentPosition.getColumn();
                for (int i = currentPosition.getRow() + 1; i < 8; i++) {
                    Position pos = new Position(i, column);
                    if (!checkPosition(pos)) {
                        return;
                    }
                }
                break;

            case LEFT:
                int row = currentPosition.getRow();
                for (int i = currentPosition.getColumn() - 1; i >= 0; i--) {
                    Position pos = new Position(row, i);
                    if (!checkPosition(pos)) {
                        return;
                    }
                }
                break;

            case RIGHT:
                row = currentPosition.getRow();
                for (int i = currentPosition.getColumn() + 1; i < 8; i++) {
                    Position pos = new Position(row, i);
                    if (!checkPosition(pos)) {
                        return;
                    }
                }
                break;
            default:
                break;
        }
    }

    private boolean checkPosition(Position pos) {
        if (game.isPositionFree(pos)) {
            addMove(pos);
            return true;
        }
        else {
            if (isAttackPossible(pos)) {
                addMove(pos);
            }
            return false;
        }

    }

    private boolean isAttackPossible(Position pos) {
        return game.containsEnemy(currentPosition, pos);
    }
}
