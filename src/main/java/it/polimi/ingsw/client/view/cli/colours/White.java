package it.polimi.ingsw.client.view.cli.colours;

/**
 * White colour.
 */
public class White implements Colour {

    private static White instance = null;

    private White() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static White getInstance() {
        if (instance == null)
            instance = new White();
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
        return 255;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 255;
    }
}
