import core.Game;
import enums.PieceColor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pieces.Piece;

/**
 * Created by ondra on 17.4.16.
 */
public class colorTest {
    private Piece[][] board;

    @Before
    public void setUp() {
        Game game = new Game();
        board = game.getBoard();
    }

    @Test
    public void testPieceColor() {
        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < 8; column++) {
                Assert.assertEquals("Wrong piece color", PieceColor.BLACK, board[row][column].getPieceColor());
            }
        }

        for (int row = 7; row > 5; row--) {
            for (int column = 0; column < 8; column++) {
                Assert.assertEquals("Wrong piece color", PieceColor.WHITE, board[row][column].getPieceColor());
            }
        }
    }
}
