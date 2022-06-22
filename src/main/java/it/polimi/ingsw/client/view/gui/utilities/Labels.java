package it.polimi.ingsw.client.view.gui.utilities;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Static class for getting all the required labels for the game.
 */
public class Labels {

    private Labels() {
    }

    /**
     * Gets the label which contains the number of coins.
     *
     * @param number The number to display.
     * @return The generated label.
     */
    static Label coinsNumber(int number) {
        Label label = new Label(String.format("x%d", number));
        label.setFont(Font.font(20));
        label.setTextFill(Color.WHITE);
        return label;
    }

    /**
     * Gets the label which contains the number of students.
     *
     * @param number The number to display.
     * @return The generated label.
     */
    static Label studentsNumber(int number) {
        Label label = new Label(String.format("x%1d", number));
        label.setFont(Font.font(14));
        label.setTextFill(Color.WHITE);
        return label;
    }

    /**
     * Gets the label which contains the name of the player for the board tab.
     *
     * @param name The name to display.
     * @return The generated label.
     */
    static Label playerName(String name) {
        Label label = new Label(name);
        label.setFont(Font.font(24));
        label.setTextFill(Color.WHITE);
        return label;
    }
}
