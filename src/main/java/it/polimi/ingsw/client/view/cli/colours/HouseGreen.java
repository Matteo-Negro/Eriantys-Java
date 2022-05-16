package it.polimi.ingsw.client.view.cli.colours;

import it.polimi.ingsw.client.view.cli.Colour;

/**
 * House green colour.
 */
public class HouseGreen implements Colour {

    private static HouseGreen instance = null;

    private HouseGreen() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static HouseGreen getInstance() {
        if (instance == null)
            instance = new HouseGreen();
        return instance;
    }

    /**
     * Gets R value.
     *
     * @return R value.
     */
    @Override
    public int getR() {
        return 61;
    }

    /**
     * Gets G value.
     *
     * @return G value.
     */
    @Override
    public int getG() {
        return 186;
    }

    /**
     * Gets B value.
     *
     * @return B value.
     */
    @Override
    public int getB() {
        return 119;
    }
}
