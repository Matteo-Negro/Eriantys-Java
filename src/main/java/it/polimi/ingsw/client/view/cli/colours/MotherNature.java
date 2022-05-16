package it.polimi.ingsw.client.view.cli.colours;

import it.polimi.ingsw.client.view.cli.Colour;

/**
 * Mother Nature colour.
 */
public class MotherNature implements Colour {

    private static MotherNature instance = null;

    private MotherNature() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static MotherNature getInstance() {
        if (instance == null)
            instance = new MotherNature();
        return instance;
    }

    /**
     * Gets R value.
     *
     * @return R value.
     */
    @Override
    public int getR() {
        return 255;
    }

    /**
     * Gets G value.
     *
     * @return G value.
     */
    @Override
    public int getG() {
        return 107;
    }

    /**
     * Gets B value.
     *
     * @return B value.
     */
    @Override
    public int getB() {
        return 2;
    }
}
