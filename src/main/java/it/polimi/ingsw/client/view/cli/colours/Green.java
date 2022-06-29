package it.polimi.ingsw.client.view.cli.colours;

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
        return 255;
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
