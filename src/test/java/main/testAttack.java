package main;

import core.Game;
import core.Move;
import core.Position;
import enums.PieceColor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pieces.Piece;

/**
 * Created by Ondrej Prenek on 8.5.16.
 */
public class testAttack {
    private Game game;
    private Piece[][] board;

    @Before
    public void setUp() {
        game = new Game();
        board = game.getBoard();
    }

    @Test
    public void testAttack() {
        Move whiteMove = new Move(new Position(6, 0), new Position(4, 0));
        game.updateMove(whiteMove, board[6][0]);
        game.switchCurrentPlayer();
        Move blackMove = new Move(new Position(1, 1), new Position(3, 1));
        game.updateMove(blackMove, board[1][1]);
        game.switchCurrentPlayer();
        whiteMove = new Move(new Position(4, 0), new Position(3, 1));
        game.updateMove(whiteMove, board[4][0]);
        Assert.assertEquals(game.getBoard()[3][1].getPieceColor(), PieceColor.WHITE);
    }

}
