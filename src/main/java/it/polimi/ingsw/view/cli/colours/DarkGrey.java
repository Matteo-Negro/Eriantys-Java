package it.polimi.ingsw.view.cli.colours;

import it.polimi.ingsw.view.cli.Colour;

/**
 * Gray colour.
 */
public class DarkGrey implements Colour {

    private static DarkGrey instance = null;

    private DarkGrey() {
    }

    /**
     * Gets R value.
     *
     * @return R value.
     */
    @Override
    public int getR() {
        return 50;
    }

    /**
     * Gets G value.
     *
     * @return G value.
     */
    @Override
    public int getG() {
        return 50;
    }

    /**
     * Gets B value.
     *
     * @return B value.
     */
    @Override
    public int getB() {
        return 50;
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static DarkGrey getInstance() {
        if (instance == null)
            instance = new DarkGrey();
        return instance;
    }
}
