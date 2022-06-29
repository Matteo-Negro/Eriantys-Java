package it.polimi.ingsw.client.view.cli.colours;

/**
 * Tower white colour.
 */
public class TowerWhite implements Colour {

    private static TowerWhite instance = null;

    private TowerWhite() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static TowerWhite getInstance() {
        if (instance == null)
            instance = new TowerWhite();
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
