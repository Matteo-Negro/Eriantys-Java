package it.polimi.ingsw.client.view.cli.colours;

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
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 61;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 186;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 119;
    }
}
