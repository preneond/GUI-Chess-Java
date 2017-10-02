package pieces;


import core.Move;
import enums.Direction;
import enums.PieceColor;
import enums.PieceType;
import core.Game;
import core.Position;

import java.io.Serializable;

/**
 * Created by Ondrej Prenek on 23.2.2016.
 */
public class King extends Piece implements Serializable {

    public King(PieceColor pieceColor, PieceType piece, Position position, Game game) {
        super(pieceColor, piece, position, game);
        pgnID = "K";
        switch (pieceColor) {
            case BLACK:
                imagePath = this.getClass().getResource("/blackKing.png").getPath();
                break;
            case WHITE:
                imagePath = this.getClass().getResource("/whiteKing.png").getPath();
                break;
            default:
                break;
        }
    }

    @Override
    public void addAllPossibleMoves() {
        possibleMoves.clear();
        Position[] potentialMoves = new Position[8];
        int row = currentPosition.getRow();
        int column = currentPosition.getColumn();
        potentialMoves[0] = new Position(row + 1, column + 1);
        potentialMoves[1] = new Position(row + 1, column - 1);
        potentialMoves[2] = new Position(row - 1, column + 1);
        potentialMoves[3] = new Position(row - 1, column - 1);
        potentialMoves[4] = new Position(row + 1, column);
        potentialMoves[5] = new Position(row - 1, column);
        potentialMoves[6] = new Position(row, column + 1);
        potentialMoves[7] = new Position(row, column - 1);

        for (int i = 0; i < 8; i++) {
            if (isMovePossible(potentialMoves[i])) {
                addMove(potentialMoves[i]);
            }
        }
        checkCastling();
    }

    private void checkCastling() {
        if (!onStartPosition) return;
        checkCastling(Direction.LEFT);
        checkCastling(Direction.RIGHT);
    }

    private void checkCastling(Direction direction) {
        int row = (pieceColor == PieceColor.WHITE) ? 7 : 0;
        Piece rook;
        switch (direction) {
            case LEFT:
                rook = game.getBoard()[row][0];
                if (rook != null && rook.onStartPosition) {
                    for (int i = 3; i > 0; i--) {
                        if (!game.isPositionFree(new Position(row, i))) {
                            return;
                        }
                    }
                    Move move = new Move(currentPosition, new Position(row, 0));
                    move.setCastling(true);
                    addMove(move);
                }
                break;
            case RIGHT:
                rook = game.getBoard()[row][7];
                if (rook != null && rook.onStartPosition) {
                    for (int i = 5; i < 7; i++) {
                        if (!game.isPositionFree(new Position(row, i))) {
                            return;
                        }
                    }
                    Move move = new Move(currentPosition, new Position(row, 7));
                    move.setCastling(true);
                    addMove(move);
                }
                break;
            default:
                break;
        }

    }

    private boolean isMovePossible(Position position) {
        return !game.isOutOfBoard(position) && !game.containsOwnPiece(currentPosition, position);
    }

}
