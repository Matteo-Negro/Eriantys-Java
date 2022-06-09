package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utilities.Pair;

/**
 * An interface which defines the basic methods implemented by the view classes.
 *
 * @author Riccardo Milici
 * @author Riccardo Motta
 */
public interface View {

    /**
     * Shows an error message on the screen.
     *
     * @param message The message to show.
     */
    void showError(String message);

    /**
     * Shows an info text on the screen.
     *
     * @param info The info to show.
     */
    void showInfo(Pair<String, String> info);

    /**
     * Updates the view screen.
     *
     * @param def Boolean parameter used by CLI.
     */
    void updateScreen(boolean def);
}
