package it.polimi.ingsw.client.view.cli.colours;

/**
 * Title colour.
 */
public class Title implements Colour {

    private static Title instance = null;

    private Title() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static Title getInstance() {
        if (instance == null)
            instance = new Title();
        return instance;
    }

    /**
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 87;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 201;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 244;
    }
}
