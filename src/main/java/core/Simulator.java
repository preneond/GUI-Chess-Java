package core;

import enums.PieceColor;
import gui.ChessBoard;
import pieces.Piece;
import players.Player;
import players.RandomPlayer;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ondrej Prenek on 24.4.16.
 */


public class Simulator implements Serializable {
    private static final long serialVersionUID = 11L;
    private final Logger logger;
    private Game game;
    private ChessBoard chessboard;

    /**
     * Constructor
     */
    public Simulator() {
        game = new Game();
        chessboard = new ChessBoard(game);
        chessboard.setSimulator(this);
        chessboard.showWindow();
        logger = Logger.getLogger(this.getClass().getName());
    }

    /**
     * Start the game and create the pgn file
     */
    public void startGame() {
        game.makePgnFile();
        chessboard.getToolbar().getStopWatches().startStopWatches();
        if (game.opponentIsComputer && game.getComputerPlayer().getPieceColor() == PieceColor.WHITE) {
            makeComputerMove();
        }


    }

    /**
     * Make move, which was clicked in GUI
     * Write move to pgn file, hide all highlighted moves on board in GUI and change current player.
     * Thanks to changing current player, opponent can play his move.
     *
     * @param row row
     * @param column column
     */
    public void makeClickedMove(int row, int column) {
        Position curPosition = chessboard.getClickedPiece().getCurrentPosition();
        Move move = new Move(curPosition, new Position(row, column));
        game.updateMove(move, game.getBoard()[curPosition.getRow()][curPosition.getColumn()]);
        chessboard.initMoveToGui(move);
        writeMoveToPGN(move);
        game.switchCurrentPlayer();
        if (endConditionReached()) {
            evaluateWinner(game.getCurrentPlayer());
            return;
        }
        if (game.opponentIsComputer) {
            makeComputerMove();
        }
        chessboard.hidePossibleMoves();
    }

    private boolean endConditionReached() {
        ArrayList<Piece> pieces = game.getPieceList(game.getCurrentPlayer());
        ArrayList<Move> validMoves = new ArrayList<>();
        for (Piece piece : pieces) {
            validMoves.addAll(piece.getValidMoves());
        }
        return validMoves.isEmpty();
    }

    private void makeComputerMove() {
        Move move = game.getComputerPlayer().getMove();
        Position to = move.getTo();
        Position from = move.getFrom();
        if (game.isMoveValid(move)) {
            game.updateMove(move, game.getBoard()[from.getRow()][from.getColumn()]);
            chessboard.initMoveToGui(move);
            writeMoveToPGN(move);
        }
        game.switchCurrentPlayer();
        if (endConditionReached()) {
            evaluateWinner(game.getCurrentPlayer());
        }
    }

    private void evaluateWinner(PieceColor looser) {
        boolean checkMate = false;
        String result;
        if (game.isKingInDanger(looser)) {
            checkMate = true;
        }
        Player winner = (looser == PieceColor.BLACK) ? game.getWhitePlayer() : game.getBlackPlayer();
        //if game has winner-it is not a tie
        if (checkMate) {
            result = (looser == PieceColor.BLACK) ? "1-0" : "0-1";
        } else {
            result = " 1/2-1/2";
        }
        chessboard.endGame(checkMate, winner);
        game.getPgnWriter().print(result);
        game.getPgnWriter().close();
        replaceResultLine(result);
    }

    private void replaceResultLine(String result) {
        result = "[Result: \"" + result + "\"]";
        String lineToReplaced = "[Result: \"*\"]";

        String input = "";
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(game.getPgnFile()));
            while (((line = br.readLine()) != null)) {
                if (line.equals(lineToReplaced)) {
                    input += result + '\n';
                } else {
                    input += line + '\n';
                }
            }
            File tmpFile = new File("game.tmp");
            FileWriter fw = new FileWriter(tmpFile);
            fw.write(input);
            fw.close();
            if (!tmpFile.renameTo(game.getPgnFile())) {
                logger.log(Level.FINE, "Rename file problem");

            }
        } catch (FileNotFoundException e) {
            logger.log(Level.FINE, "File not found exception");
        } catch (IOException e) {
            logger.log(Level.FINE, "File input exception");
        }
    }

    /**
     * Set computer as opponent
     *
     * @param computerColor- PieceColor which computer will have
     */
    public void setOpponentAsComputer(PieceColor computerColor) {
        game.setComputerPlayer(new RandomPlayer("Computer"));
        game.opponentIsComputer = true;
        game.getComputerPlayer().setGame(game);
        if (computerColor == PieceColor.BLACK) {
            game.setBlackPlayer(game.getComputerPlayer());
        } else {
            game.setWhitePlayer(game.getComputerPlayer());
        }
    }

    private void writeMoveToPGN(Move move) {
        if (move.isCastling()) {
            if (move.getFrom().getRow() == 0) {
                game.prevBlackMove = move;
                game.prevBlackMovedPiece = null;
            } else {
                game.prevWhiteMove = move;
                game.prevWhiteMovedPiece = null;
                writeRoundToPgn();

            }
        } else {
            Piece movedPiece = game.getBoard()[move.getTo().getRow()][move.getTo().getColumn()];
            if (movedPiece.getPieceColor() == PieceColor.WHITE) {
                game.prevWhiteMove = move;
                game.prevWhiteMovedPiece = movedPiece;
            } else {
                game.prevBlackMove = move;
                game.prevBlackMovedPiece = movedPiece;
                writeRoundToPgn();
            }
        }
    }

    private void writeRoundToPgn() {
        if (game.prevWhiteMove == null || game.prevBlackMove == null) return;
        game.moveCounter++;
        String promotion = "=Q";
        String round = game.moveCounter + ". ";
        if (game.prevWhiteMovedPiece == null) {
            if (game.prevWhiteMove.getFrom().getColumn() == 0 || game.prevWhiteMove.getTo().getColumn() == 0) {
                round += "O-O-O";
            } else {
                round += "O-O";
            }
        } else {
            if (game.prevWhiteMove.isPromotion()) {
                round += game.prevWhiteMovedPiece.getCurrentPosition() + promotion;
            } else {
                round += game.prevWhiteMovedPiece.getPgnID() + game.prevWhiteMovedPiece.getCurrentPosition();
            }
        }
        if (game.isKingInDanger(PieceColor.BLACK)) round += "+";
        round += " ";
        if (game.prevBlackMovedPiece == null) {
            if (game.prevBlackMove.getFrom().getColumn() == 0 || game.prevBlackMove.getTo().getColumn() == 0) {
                round += "O-O-O";
            } else {
                round += "O-O";
            }
        } else {
            if (game.prevBlackMove.isPromotion()) {
                round += game.prevBlackMovedPiece.getCurrentPosition() + promotion;
            } else {
                round += game.prevBlackMovedPiece.getPgnID() + game.prevBlackMovedPiece.getCurrentPosition();
            }

        }
        if (game.isKingInDanger(PieceColor.WHITE)) round += "+";
        round += " ";
        game.getPgnWriter().print(round);
    }

    /**
     * @return current game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Set current game.
     *
     * @param game to set
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Set name of match, which will be played
     *
     * @param nameOfMatch name to set
     */
    void setNameOfMatch(String nameOfMatch) {
        game.setNameOfMatch(nameOfMatch);
    }
}
