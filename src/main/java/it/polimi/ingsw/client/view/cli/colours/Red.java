package it.polimi.ingsw.client.view.cli.colours;

import it.polimi.ingsw.client.view.cli.Colour;

/**
 * Red colour.
 */
public class Red implements Colour {

    private static Red instance = null;

    private Red() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static Red getInstance() {
        if (instance == null)
            instance = new Red();
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
        return 0;
    }

    /**
     * Gets B value.
     *
     * @return B value.
     */
    @Override
    public int getB() {
        return 0;
    }
}
