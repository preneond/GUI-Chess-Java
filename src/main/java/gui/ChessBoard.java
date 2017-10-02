

package gui;

/**
 * Created by Ondrej Prenek on 20.3.2016.
 */

import core.Game;
import core.Move;
import core.Position;
import core.Simulator;
import enums.PieceColor;
import pieces.Piece;
import players.Person;
import players.Player;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChessBoard extends JFrame implements Serializable {
    private static final long serialVersionUID = 11L;
    private ChessLabel[][] labels;
    private boolean[][] clicked;
    private ChessLabel clickedLabel;
    private Piece clickedPiece;
    private Game game;

    private boolean whiteNameIsChosen;
    private boolean blackNameIsChosen;

    private Toolbar toolbar;
    private Container contentPane;
    private JFrame window;
    private Border border;
    private Simulator simulator;
    private Logger logger;

    public ChessBoard(Game game) {
        logger = Logger.getLogger(this.getClass().getName());
        this.game = game;
        clicked = new boolean[8][8];
        labels = new ChessLabel[8][8];
        clickedLabel = null;
        clickedPiece = null;
        border = BorderFactory.createLineBorder(Color.RED, 5);
    }

    /**
     * open window with board and set all required configurations
     */
    public void showWindow() {
        initGUI();
        showPopUpWindows();
    }

    private void showPopUpWindows() {
        String[] opponentChoices = new String[]{"Computer", "Second player"};
        String defaultChoice = "Computer";
        int opponentChoice = JOptionPane.showOptionDialog(contentPane, //Component parentComponent
                "Choose an opponent: ", //Object message,
                "Choose an opponent", //String title
                JOptionPane.YES_NO_OPTION, //int optionType
                JOptionPane.QUESTION_MESSAGE, //int messageType
                null, //Icon icon,
                opponentChoices, //Object[] options,
                defaultChoice);//Object initialValue
        game.opponentIsComputer = (opponentChoice == 0);

        if (game.opponentIsComputer) {
            String[] colorChoices = new String[]{"White", "Black"};
            defaultChoice = "White";
            int colorChoice = JOptionPane.showOptionDialog(contentPane, //Component parentComponent
                    "Choose a color of your pieces: ", //Object message,
                    "Choose a color", //String title
                    JOptionPane.YES_NO_OPTION, //int optionType
                    JOptionPane.QUESTION_MESSAGE, //int messageType
                    null, //Icon icon,
                    colorChoices, //Object[] options,
                    defaultChoice);//Object initialValue
            PieceColor playerColor;
            playerColor = (colorChoice == 0) ? PieceColor.WHITE : PieceColor.BLACK;
            PieceColor computerColor;
            computerColor = (colorChoice == 0) ? PieceColor.BLACK : PieceColor.WHITE;

            simulator.setOpponentAsComputer(computerColor);
            game.getComputerPlayer().setPieceColor(computerColor);
            while (!((playerColor == PieceColor.WHITE) ? whiteNameIsChosen : blackNameIsChosen)) {
                setPlayerName(playerColor);
            }
        } else {
            while (!(whiteNameIsChosen && blackNameIsChosen)) {
                if (!whiteNameIsChosen) {
                    setPlayerName(PieceColor.WHITE);
                    continue;
                }
                if (!blackNameIsChosen) {
                    setPlayerName(PieceColor.BLACK);
                }
            }
        }
    }

    /**
     * Initialize window with chess board and buttons,
     * set listener for each button and for chess board too.
     */
    private void initGUI() {
        window = new JFrame("Chess game");
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setWindowListener(window);
        ImageIcon img = new ImageIcon(this.getClass().getResource("/whiteKing.png"));
        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        window.add(contentPane);

        JPanel labelsPanel = new JPanel();
        fillBoard(labelsPanel);
        toolbar = new Toolbar(game);

        setListenerToLoadButton(toolbar.getLoadButton());
        setListenerToSaveButton(toolbar.getSaveButton());
        setListenerToNewGameButton(toolbar.getNewGameButton());
        contentPane.add(toolbar, BorderLayout.EAST);
        contentPane.add(labelsPanel, BorderLayout.CENTER);
        window.setIconImage(img.getImage());
        window.setResizable(false);
        window.setSize(670, 600);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

    }


    private void fillBoard(JPanel labelsPanel) {
        GridLayout gridLayout = new GridLayout(8, 8);
        labelsPanel.setLayout(gridLayout);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                final ChessLabel chessLabel;
                if (game.getBoard()[i][j] == null) {
                    chessLabel = new ChessLabel();
                } else {
                    chessLabel = new ChessLabel(game.getBoard()[i][j].getImagePath());
                }
                labels[i][j] = chessLabel;
                labels[i][j].set(i, j);
                labelsPanel.add(labels[i][j]);
                setFieldListener(i, j, chessLabel);
            }
        }
    }

    /**
     * hide all highlighted possible moves on board
     */
    public void hidePossibleMoves() {
        clicked = new boolean[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                labels[i][j].setBorder(null);

            }
        }
        clickedLabel = null;
        clickedPiece = null;
    }

    private void setFieldListener(final int row, final int column, final ChessLabel chessLabel) {
        chessLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (game.getBoard()[row][column] == null) {
                    if (clicked[row][column]) {
                        simulator.makeClickedMove(row, column);
                    }
                } else {
                    if (game.getBoard()[row][column].getPieceColor() != game.getCurrentPlayer()) {
                        if (clickedPiece != null && clicked[row][column]) {
                            simulator.makeClickedMove(row, column);
                        }
                    } else {
                        if (clicked[row][column]) {
                            simulator.makeClickedMove(row, column);
                        }
                        hidePossibleMoves();
                        if (game.getBoard()[row][column] != clickedPiece) {
                            chessLabel.setBorder(border);
                            ArrayList<Move> validMoves = game.getValidMoves(row, column);
                            labels[row][column].setBorder(border);
                            for (Move move : validMoves) {
                                labels[move.getTo().getRow()][move.getTo().getColumn()].setBorder(border);
                                clicked[move.getTo().getRow()][move.getTo().getColumn()] = true;
                            }
                            clickedPiece = game.getBoard()[row][column];
                            clickedLabel = chessLabel;
                        }
                    }
                }
            }
        });
    }


    private void setPlayerName(PieceColor color) {
        String name;
        switch (color) {
            case WHITE:
                name = JOptionPane.showInputDialog(contentPane, "Write a player name with white pieces:  ", "Write a name", JOptionPane.INFORMATION_MESSAGE);
                if (name != null && !Objects.equals(name, "")) {
                    game.setWhitePlayer(new Person(name));
                    whiteNameIsChosen = true;
                    return;
                }
                break;
            case BLACK:
                name = JOptionPane.showInputDialog(contentPane, "Write a player name with black pieces: ", "Write a name", JOptionPane.INFORMATION_MESSAGE);
                if (name != null && !Objects.equals(name, "")) {
                    game.setBlackPlayer(new Person(name));
                    blackNameIsChosen = true;
                    return;
                }
                break;
            default:
                break;
        }
        JOptionPane.showMessageDialog(contentPane, "Name must be filled in", "Error", JOptionPane.ERROR_MESSAGE);
    }


    private void saveGame(File file) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            writeObject(outputStream);
            outputStream.close();
        } catch (Exception ex) {
            logger.log(Level.FINE, "Saving file exception");

        }
    }

    private void loadGame(File file) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
            readObject(inputStream);
            inputStream.close();

        } catch (Exception ex) {
            logger.log(Level.FINE, "Loading file exception");
        }
    }

    private void refreshGUI() {
        hidePossibleMoves();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                labels[i][j].setIcon(null);
                if (game.getBoard()[i][j] != null) {
                    ImageIcon icon = new ImageIcon(game.getBoard()[i][j].getImagePath());
                    labels[i][j].setIcon(icon);
                }
            }
        }
    }

    private void setListenerToLoadButton(final JButton loadButton) {
        loadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JFileChooser fileOpen = new JFileChooser();
                fileOpen.setFileFilter(new FileFilter() {
                    public boolean accept(File f) {
                        return f.getName().toLowerCase().endsWith(".dat") || f.isDirectory();
                    }

                    public String getDescription() {
                        return "Data files";
                    }

                });

                int option = fileOpen.showOpenDialog(contentPane);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileOpen.getSelectedFile();
                    loadGame(file);
                }
            }
        });
    }

    private void setListenerToSaveButton(JButton saveButton) {
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JFileChooser chooser = new JFileChooser();
                chooser.setSelectedFile(new File("save.dat"));
                int option = chooser.showSaveDialog(contentPane);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    saveGame(file);
                }
            }
        });
    }


    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeObject(game);
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        game = (Game) stream.readObject();
        simulator.setGame(game);
        game.makePgnFile();
        toolbar.getStopWatches().setGame(game);
        refreshGUI();
    }

    /**
     * show executedMove in GUI
     *
     * @param move executed move
     */
    public void initMoveToGui(Move move) {
        setTimeToNull();
        if (move.isEnPassant() || move.isCastling()) {
            refreshGUI();
        } else {
            Position from = move.getFrom();
            Position to = move.getTo();
            ImageIcon icon = new ImageIcon(game.getBoard()[to.getRow()][to.getColumn()].getImagePath());
            labels[from.getRow()][from.getColumn()].setIcon(null);
            labels[to.getRow()][to.getColumn()].setIcon(icon);
        }
    }

    private void setTimeToNull() {
        game.hours = 0;
        game.minutes = 0;
        game.seconds = -1;
    }

    private void setWindowListener(JFrame window) {
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showOptionDialog(null, "Are you sure? All unsaved progress will be lost.", "Are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, "No");
                if (result == JOptionPane.YES_OPTION) {
                    game.getPgnWriter().close();
                    System.exit(0);
                }
            }
        });
    }

    private void setListenerToNewGameButton(JButton newGameButton) {
        newGameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                window.setVisible(false);
                simulator = new Simulator();
                simulator.startGame();
            }
        });
    }

    /**
     * @return clicked piece, which has highlighted piece on board
     */
    public Piece getClickedPiece() {
        return clickedPiece;
    }

    /**
     * set simulator which  manage the course of the game
     *
     * @param simulator simulator which manage game
     */
    public void setSimulator(Simulator simulator) {
        this.simulator = simulator;
    }

    /**
     * @return toolbar panel with buttons
     */
    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * @param hasWinner true- the game has winner-it is not a tie
     *                  false- the game hasn't got a winner- it's a tie
     * @param winner    null, if it is a tie, else it contains winner of the game
     */
    public void endGame(boolean hasWinner, Player winner) {
        toolbar.getStopWatches().stopStopWatches();
        if (hasWinner) {
            JOptionPane.showMessageDialog(contentPane, "End of the game. Winner is: " + winner.getName(), "End of the game", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(contentPane, "End of the game. It's a tie", "End of the game", JOptionPane.INFORMATION_MESSAGE);
        }

    }

}


