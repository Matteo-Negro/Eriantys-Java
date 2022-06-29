package it.polimi.ingsw.client.view.cli.colours;

/**
 * Black colour.
 */
public class Black implements Colour {

    private static Black instance = null;

    private Black() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static Black getInstance() {
        if (instance == null)
            instance = new Black();
        return instance;
    }

    /**
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 0;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 0;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 0;
    }
}
