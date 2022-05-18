package it.polimi.ingsw.client.view.cli.colours;

import it.polimi.ingsw.client.view.cli.Colour;

/**
 * Green colour.
 */
public class Green implements Colour {

    private static Green instance = null;

    private Green() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static Green getInstance() {
        if (instance == null)
            instance = new Green();
        return instance;
    }

    /**
     * Gets R value.
     *
     * @return R value.
     */
    @Override
    public int getR() {
        return 0;
    }

    /**
     * Gets G value.
     *
     * @return G value.
     */
    @Override
    public int getG() {
        return 255;
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
