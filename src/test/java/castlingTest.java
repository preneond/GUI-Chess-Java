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
public class castlingTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game();
    }

    @Test
    public void castlingTest() {
        //move with knight
        Position from = new Position(7, 1);
        Position to = new Position(5, 0);
        Move move = new Move(from, to);
        game.updateMove(move, game.getBoard()[from.getRow()][from.getColumn()]);
        //move with pawn
        from = new Position(6, 3);
        to = new Position(4, 3);
        move = new Move(from, to);
        game.updateMove(move, game.getBoard()[from.getRow()][from.getColumn()]);
        //move with bishop
        from = new Position(7, 2);
        to = new Position(5, 4);
        move = new Move(from, to);
        game.updateMove(move, game.getBoard()[from.getRow()][from.getColumn()]);
        //move with queen
        from = new Position(7, 3);
        to = new Position(6, 3);
        move = new Move(from, to);
        game.updateMove(move, game.getBoard()[from.getRow()][from.getColumn()]);
        //And finally! castling!
        from = new Position(7, 0);
        to = new Position(7, 4);
        move = new Move(from, to);
        game.updateMove(move, game.getBoard()[from.getRow()][from.getColumn()]);
        Assert.assertTrue(move.isCastling());
        Assert.assertEquals(game.getBoard()[7][3].getPieceType(), PieceType.ROOK);
        Assert.assertEquals(game.getBoard()[7][2].getPieceType(), PieceType.KING);
    }
}
