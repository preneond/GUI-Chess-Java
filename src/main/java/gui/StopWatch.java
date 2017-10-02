package gui;

import core.Game;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StopWatch implements Serializable {
    private static final long serialVersionUID = 11L;
    // GUI Components
    JLabel timeLabel;

    // Properties of Program.
    private Game game;

    private Runnable timeTask;
    private Runnable incrementTimeTask;
    private Runnable setTimeTask;
    private DecimalFormat timeFormatter;
    private boolean timerIsRunning = true;

    private ExecutorService executor = Executors.newCachedThreadPool();
    private Logger logger;

    /**
     * Constructor
     *
     * @param g current game
     */
    StopWatch(Game g) {
        this.game = g;
        logger = Logger.getLogger(this.getClass().getName());
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeFormatter = new DecimalFormat("00");

        timeTask = new Runnable() {
            @Override
            public void run() {
                while (timerIsRunning) {
                    executor.execute(incrementTimeTask);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        logger.log(Level.FINE, "Stopwatch-InterruptedException ");
                    }
                }
            }
        };

        incrementTimeTask = new Runnable() {
            @Override
            public void run() {
                if (game.seconds < 59) {
                    game.seconds++;
                } else {
                    if (game.minutes < 59) {
                        game.minutes++;
                        game.seconds = 0;
                    } else {
                        game.hours++;
                        game.seconds = 0;
                        game.minutes = 0;
                    }
                }

                executor.execute(setTimeTask);
            }
        };

        setTimeTask = new Runnable() {
            @Override
            public void run() {
                if (game.hours == 0) {
                    timeLabel.setText(timeFormatter.format(game.minutes) + ":" + timeFormatter.format(game.seconds));
                } else {
                    timeLabel.setText(timeFormatter.format(game.hours) + ":" + timeFormatter.format(game.minutes) + ":" + timeFormatter.format(game.seconds));
                }
            }
        };

        timeLabel.setText(timeFormatter.format(game.minutes) + ":" + timeFormatter.format(game.seconds));
    }

    /**
     * this method start stopwatches and after that
     */
    public void startStopWatches() {
        timerIsRunning = true;
        executor.execute(timeTask);
    }

    /**
     * @param game game to set
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Stop current running stopwatches.
     */
    void stopStopWatches() {
        timerIsRunning = false;
    }
}