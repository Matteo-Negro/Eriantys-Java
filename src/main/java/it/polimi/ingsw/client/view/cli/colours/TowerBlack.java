package it.polimi.ingsw.client.view.cli.colours;

/**
 * Tower black colour.
 */
public class TowerBlack implements Colour {

    private static TowerBlack instance = null;

    private TowerBlack() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static TowerBlack getInstance() {
        if (instance == null)
            instance = new TowerBlack();
        return instance;
    }

    /**
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 60;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 60;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 60;
    }
}
