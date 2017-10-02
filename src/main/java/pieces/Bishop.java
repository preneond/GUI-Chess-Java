package pieces;

import core.Game;
import core.Position;
import enums.Direction;
import enums.PieceColor;
import enums.PieceType;

import java.io.Serializable;


/**
 * Created by Ondrej Prenek on 23.2.2016.
 */
public class Bishop extends Piece implements Serializable {


    public Bishop(PieceColor pieceColor, PieceType piece, Position position, Game game) {
        super(pieceColor, piece, position, game);
        pgnID = "B";
        switch (pieceColor) {
            case BLACK:
                imagePath = this.getClass().getResource("/blackBishop.png").getPath();
                break;
            case WHITE:
                imagePath = this.getClass().getResource("/whiteBishop.png").getPath();
                break;
            default:
                break;

        }
    }

    @Override
    public void addAllPossibleMoves() {
        possibleMoves.clear();
        checkDirection(Direction.UPLEFT);
        checkDirection(Direction.UPRIGHT);
        checkDirection(Direction.DOWNLEFT);
        checkDirection(Direction.DOWNRIGHT);
    }

    private void checkDirection(Direction direction) {
        int row = currentPosition.getRow();
        int column = currentPosition.getColumn();

        switch (direction) {
            case UPLEFT:
                row--;
                column--;
                while (row >= 0 && column >= 0) {
                    Position pos = new Position(row, column);
                    if (!checkPosition(pos)) {
                        return;
                    }
                    row--;
                    column--;
                }
                break;

            case UPRIGHT:
                row--;
                column++;
                while (row >= 0 && column < 8) {
                    Position pos = new Position(row, column);
                    if (!checkPosition(pos)) return;
                    row--;
                    column++;
                }
                break;

            case DOWNLEFT:
                row++;
                column--;
                while (row < 8 && column >= 0) {
                    Position pos = new Position(row, column);
                    if (!checkPosition(pos)) return;
                    row++;
                    column--;
                }
                break;
            case DOWNRIGHT:
                row++;
                column++;
                while (row < 8 && column < 8) {
                    Position pos = new Position(row, column);
                    if (!checkPosition(pos)) return;
                    row++;
                    column++;
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
        } else if (isMovePossible(pos)) {
            addMove(pos);
            return false;
        }
        return false;
    }

    private boolean isMovePossible(Position pos) {
        return game.containsEnemy(currentPosition, pos);
    }


}

