package core;

import java.io.Serializable;

/**
 * Created by ondra on 25.2.2016.
 */
public class Main implements Serializable {
    public static void main(String[] args) {
        Simulator sim = new Simulator();
        sim.setNameOfMatch("CVUT FEL PR2 Match");
        sim.startGame();
    }
}
