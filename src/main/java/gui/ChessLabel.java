package gui;

/**
 * Created by Ondrej Prenek on 20.3.2016.
 */

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;


class ChessLabel extends JLabel implements Serializable {
    private static final long serialVersionUID = 11L;

    //Font font = new Font("Times New Roman", Font.PLAIN, 35);
    private Color bgLight = new Color(220, 194, 135);
    private Color bgDark = new Color(139, 69, 19);

    /**
     * Constructor
     *
     * @param path path to image file
     */
    ChessLabel(String path) {
        super(new ImageIcon(path));
    }

    /**
     * overloaded constructor- it is used when the field on chessboard does not contains any piece
     */
    ChessLabel() {
        super();
    }

    /**
     * set background and alignment for label
     *
     * @param row    row on board
     * @param column column on board
     */
    void set(int row, int column) {
        setOpaque(true);
        setBackground((row + column) % 2 == 0 ? bgLight : bgDark);
        setHorizontalAlignment(SwingConstants.CENTER); // center the text
    }
}