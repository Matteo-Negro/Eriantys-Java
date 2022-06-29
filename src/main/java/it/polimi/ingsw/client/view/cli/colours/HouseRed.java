package it.polimi.ingsw.client.view.cli.colours;

/**
 * House red colour.
 */
public class HouseRed implements Colour {

    private static HouseRed instance = null;

    private HouseRed() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static HouseRed getInstance() {
        if (instance == null)
            instance = new HouseRed();
        return instance;
    }

    /**
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 238;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 37;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 43;
    }
}
