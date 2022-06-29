package it.polimi.ingsw.client.view.cli.colours;

/**
 * Wizard white colour.
 */
public class WizardDarkWhite implements Colour {

    private static WizardDarkWhite instance = null;

    private WizardDarkWhite() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static WizardDarkWhite getInstance() {
        if (instance == null)
            instance = new WizardDarkWhite();
        return instance;
    }

    /**
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 115;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 117;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 116;
    }
}
