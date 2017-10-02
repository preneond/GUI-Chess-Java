package players;

import core.Move;

import java.io.Serializable;

/**
 * Created by Ondrej Prenek on 8.5.16.
 */
public class Person extends Player implements Serializable {

    public Person(String name) {
        super(name);
    }

    @Override
    public Move getMove() {
        //it is null, because person make the move in GUI
        return null;
    }
}
