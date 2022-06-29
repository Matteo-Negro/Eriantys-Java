package it.polimi.ingsw.client.view.cli.colours;

/**
 * Wizard yellow colour.
 */
public class WizardYellow implements Colour {

    private static WizardYellow instance = null;

    private WizardYellow() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static WizardYellow getInstance() {
        if (instance == null)
            instance = new WizardYellow();
        return instance;
    }

    /**
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 247;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 193;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 87;
    }
}
