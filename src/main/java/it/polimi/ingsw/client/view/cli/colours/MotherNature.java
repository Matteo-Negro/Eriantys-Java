package it.polimi.ingsw.client.view.cli.colours;

/**
 * Mother Nature colour.
 */
public class MotherNature implements Colour {

    private static MotherNature instance = null;

    private MotherNature() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static MotherNature getInstance() {
        if (instance == null)
            instance = new MotherNature();
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
        return 107;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 2;
    }
}
