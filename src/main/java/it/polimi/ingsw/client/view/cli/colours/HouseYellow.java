package it.polimi.ingsw.client.view.cli.colours;

/**
 * House yellow colour.
 */
public class HouseYellow implements Colour {

    private static HouseYellow instance = null;

    private HouseYellow() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static HouseYellow getInstance() {
        if (instance == null)
            instance = new HouseYellow();
        return instance;
    }

    /**
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 255;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 182;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 9;
    }
}
