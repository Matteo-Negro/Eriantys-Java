package it.polimi.ingsw.client.view.cli.colours;

/**
 * Gray colour.
 */
public class DarkGrey implements Colour {

    private static DarkGrey instance = null;

    private DarkGrey() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static DarkGrey getInstance() {
        if (instance == null)
            instance = new DarkGrey();
        return instance;
    }

    /**
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 50;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 50;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 50;
    }
}
