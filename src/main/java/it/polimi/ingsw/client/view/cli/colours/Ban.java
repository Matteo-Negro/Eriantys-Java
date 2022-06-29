package it.polimi.ingsw.client.view.cli.colours;

/**
 * Ban colour.
 */
public class Ban implements Colour {

    private static Ban instance = null;

    private Ban() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static Ban getInstance() {
        if (instance == null)
            instance = new Ban();
        return instance;
    }

    /**
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 204;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 28;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 28;
    }
}
