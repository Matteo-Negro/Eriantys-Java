package it.polimi.ingsw.client.view.cli.colours;

/**
 * House fuchsia colour.
 */
public class HouseFuchsia implements Colour {

    private static HouseFuchsia instance = null;

    private HouseFuchsia() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static HouseFuchsia getInstance() {
        if (instance == null)
            instance = new HouseFuchsia();
        return instance;
    }

    /**
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 221;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 61;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 165;
    }
}
