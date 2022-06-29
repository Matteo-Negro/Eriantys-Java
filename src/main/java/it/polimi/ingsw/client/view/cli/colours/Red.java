package it.polimi.ingsw.client.view.cli.colours;

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
