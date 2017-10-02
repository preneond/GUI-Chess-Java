package main;

import core.Game;
import core.Move;
import core.Position;
import enums.PieceType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Ondrej Prenek on 8.5.16.
 */
public class testMove {
    private Game game;

    @Before
    public void setUp() {
        game = new Game();
    }

    @Test
    public void PawnMove() {
        Move moveWithPawn = new Move(new Position(6, 0), new Position(4, 0));
        game.updateMove(moveWithPawn, game.getBoard()[6][0]);
        Assert.assertEquals(game.getBoard()[4][0].getPieceType(), PieceType.PAWN);
    }

    @Test
    public void KnightMove() {
        Move moveWithKnight = new Move(new Position(7, 1), new Position(5, 0));
        Assert.assertTrue(game.isMoveValid(moveWithKnight));
        game.updateMove(moveWithKnight, game.getBoard()[7][1]);
        Assert.assertEquals(game.getBoard()[5][0].getPieceType(), PieceType.KNIGHT);
    }

}
