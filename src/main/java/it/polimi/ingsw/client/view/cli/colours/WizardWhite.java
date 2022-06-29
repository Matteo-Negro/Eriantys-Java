package it.polimi.ingsw.client.view.cli.colours;

/**
 * Wizard white colour.
 */
public class WizardWhite implements Colour {

    private static WizardWhite instance = null;

    private WizardWhite() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static WizardWhite getInstance() {
        if (instance == null)
            instance = new WizardWhite();
        return instance;
    }

    /**
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 233;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 238;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 235;
    }
}
