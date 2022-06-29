package it.polimi.ingsw.client.view.cli.colours;

/**
 * Grey colour.
 */
public class Subtitle implements Colour {

    private static Subtitle instance = null;

    private Subtitle() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static Subtitle getInstance() {
        if (instance == null)
            instance = new Subtitle();
        return instance;
    }

    /**
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 150;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 150;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 150;
    }
}
