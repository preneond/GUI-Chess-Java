package pieces;

import core.Move;
import enums.PieceColor;
import enums.PieceType;
import core.Game;
import core.Position;

import java.io.Serializable;

/**
 * Created by Ondrej Prenek on 23.2.2016.
 */
public class Pawn extends Piece implements Serializable {

    public Pawn(PieceColor pieceColor, PieceType piece, Position position, Game game) {
        super(pieceColor, piece, position, game);
        pgnID = "";
        switch (pieceColor) {
            case BLACK:
                imagePath = this.getClass().getResource("/blackPawn.png").getPath();
                break;
            case WHITE:
                imagePath = this.getClass().getResource("/whitePawn.png").getPath();
                break;
            default:
                break;
        }
    }

    @Override
    public void addAllPossibleMoves() {
        possibleMoves.clear();
        switch (pieceColor) {
            case BLACK:
                blackMoves();
                blackAttacks();
                blackEnPassant();
                break;
            case WHITE:
                whiteMoves();
                whiteAttacks();
                whiteEnPassant();
                break;
            default:
                break;
        }
    }

    private void whiteEnPassant() {
        int row = currentPosition.getRow();
        int column = currentPosition.getColumn();
        if (currentPosition.getRow() == 3) {
            Position pos = new Position(row, column + 1);
            Position destination = new Position(row - 1, column + 1);
            if (isAttackPossible(pos) &&
                    game.getBoard()[row][column + 1] == game.prevBlackMovedPiece &&
                    game.getBoard()[row][column + 1].getPieceType() == PieceType.PAWN &&
                    game.isPositionFree(destination)) {

                Move move = new Move(currentPosition, destination);
                move.setEnPassant(true);
                addMove(move);
            }
            pos = new Position(row, column - 1);
            destination = new Position(row - 1, column - 1);
            if (isAttackPossible(pos) &&
                    game.getBoard()[row][column - 1] == game.prevBlackMovedPiece &&
                    game.getBoard()[row][column - 1].getPieceType() == PieceType.PAWN &&
                    game.isPositionFree(destination)) {
                Move move = new Move(currentPosition, destination);
                move.setEnPassant(true);
                addMove(move);
            }
        }
    }

    private void blackEnPassant() {
        int row = currentPosition.getRow();
        int column = currentPosition.getColumn();
        if (currentPosition.getRow() == 4) {
            Position pos = new Position(row, column + 1);
            Position destination = new Position(row + 1, column + 1);
            if (isAttackPossible(pos) &&
                    game.getBoard()[row][column + 1] == game.prevWhiteMovedPiece &&
                    game.getBoard()[row][column + 1].getPieceType() == PieceType.PAWN &&
                    game.isPositionFree(destination)) {
                Move move = new Move(currentPosition, destination);
                move.setEnPassant(true);
                addMove(move);
            }
            pos = new Position(row, column - 1);
            destination = new Position(row + 1, column - 1);
            if (isAttackPossible(pos) &&
                    game.getBoard()[row][column - 1] == game.prevWhiteMovedPiece &&
                    game.getBoard()[row][column - 1].getPieceType() == PieceType.PAWN &&
                    game.isPositionFree(destination)) {
                Move move = new Move(currentPosition, destination);
                move.setEnPassant(true);
                addMove(move);
            }
        }
    }

    private void blackMoves() {
        int column = currentPosition.getColumn();
        int row = currentPosition.getRow();
        int steps = isOnStartPosition() ? 2 : 1;
        for (int i = 1; i <= steps; i++) {
            Position position = new Position(i + row, column);
            if (isMovePossible(position)) {
                addMove(position);
            } else break;
        }

    }

    private void whiteMoves() {
        int column = currentPosition.getColumn();
        int row = currentPosition.getRow();
        int steps = isOnStartPosition() ? 2 : 1;
        for (int i = 1; i <= steps; i++) {
            Position position = new Position(row - i, column);
            if (isMovePossible(position)) {
                addMove(position);
            } else break;
        }

    }

    private void whiteAttacks() {
        Position attack = new Position(currentPosition.getRow() - 1, currentPosition.getColumn() + 1);
        if (isAttackPossible(attack)) {
            addMove(attack);
        }
        attack = new Position(currentPosition.getRow() - 1, currentPosition.getColumn() - 1);
        if (isAttackPossible(attack)) {
            addMove(attack);
        }

    }

    private boolean isAttackPossible(Position attack) {
        return (!game.isOutOfBoard(attack) && game.containsEnemy(currentPosition, attack));
    }

    private void blackAttacks() {
        Position attack = new Position(currentPosition.getRow() + 1, currentPosition.getColumn() + 1);
        if (isAttackPossible(attack)) {
            addMove(attack);
        }
        attack = new Position(currentPosition.getRow() + 1, currentPosition.getColumn() - 1);
        if (isAttackPossible(attack)) {
            addMove(attack);
        }
    }

    private boolean isMovePossible(Position position) {
        return (!game.isOutOfBoard(position) && game.isPositionFree(position));
    }
}
