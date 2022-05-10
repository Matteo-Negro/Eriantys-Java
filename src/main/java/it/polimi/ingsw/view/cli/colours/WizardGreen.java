package it.polimi.ingsw.view.cli.colours;

import it.polimi.ingsw.view.cli.Colour;

/**
 * Wizard green colour.
 */
public class WizardGreen implements Colour {

    private static WizardGreen instance = null;

    private WizardGreen() {
    }

    /**
     * Gets R value.
     *
     * @return R value.
     */
    @Override
    public int getR() {
        return 29;
    }

    /**
     * Gets G value.
     *
     * @return G value.
     */
    @Override
    public int getG() {
        return 127;
    }

    /**
     * Gets B value.
     *
     * @return B value.
     */
    @Override
    public int getB() {
        return 100;
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static WizardGreen getInstance() {
        if (instance == null)
            instance = new WizardGreen();
        return instance;
    }
}
