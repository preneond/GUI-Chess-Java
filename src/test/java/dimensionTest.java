import core.Game;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pieces.Piece;

/**
 * Created by Ondrej Prenek on 8.5.16.
 */
public class dimensionTest {
    private Game game;
    private Piece[][] board;

    @Before
    public void setUp() {
        game = new Game();
        board = game.getBoard();
    }

    @Test
    public void testBoardDimension() {
        board = game.getBoard();
        Assert.assertEquals("Dimension of chess board is incorrect", board.length, 8);
        Assert.assertEquals("Dimension of chess board is incorrect", board[0].length, 8);
    }
}
