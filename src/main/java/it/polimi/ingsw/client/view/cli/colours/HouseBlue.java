package it.polimi.ingsw.client.view.cli.colours;

/**
 * House blue colour.
 */
public class HouseBlue implements Colour {

    private static HouseBlue instance = null;

    private HouseBlue() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static HouseBlue getInstance() {
        if (instance == null)
            instance = new HouseBlue();
        return instance;
    }

    /**
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 87;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 201;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 244;
    }
}
