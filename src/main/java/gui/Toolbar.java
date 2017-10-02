package gui;

import core.Game;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Created by Ondrej Prenek on 19.4.2016.
 */
public class Toolbar extends JPanel implements Serializable {
    private static final long serialVersionUID = 11L;
    private JButton loadButton;
    private JButton saveButton;
    private JButton newGameButton;
    private StopWatch watch;

    Toolbar(Game game) {
        GridLayout gridLayout = new GridLayout(5, 1);
        setLayout(gridLayout);
        setSize(100, 10);

        loadButton = new JButton("Load game");
        loadButton.setPreferredSize(new Dimension(100, 10));
        loadButton.setMargin(new Insets(0, 0, 0, 0));

        saveButton = new JButton("Save game");
        saveButton.setPreferredSize(new Dimension(100, 10));
        saveButton.setMargin(new Insets(0, 0, 0, 0));

        newGameButton = new JButton("New game");
        newGameButton.setPreferredSize(new Dimension(100, 10));
        newGameButton.setMargin(new Insets(0, 0, 0, 0));

        watch = new StopWatch(game);
        watch.timeLabel.setPreferredSize(new Dimension(100, 10));

        add(watch.timeLabel);
        add(saveButton);
        add(loadButton);
        add(newGameButton);
    }

    /**
     * @return button, which load game
     */
    JButton getLoadButton() {
        return loadButton;
    }

    /**
     * @return button, which save game
     */
    JButton getSaveButton() {
        return saveButton;
    }

    /**
     * @return button, which makes new game
     */
    JButton getNewGameButton() {
        return newGameButton;
    }

    /**
     * @return stopwatches which measure time
     */
    public StopWatch getStopWatches() {
        return watch;
    }

}
