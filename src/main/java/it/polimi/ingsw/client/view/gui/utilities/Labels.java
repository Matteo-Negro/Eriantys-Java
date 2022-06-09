package it.polimi.ingsw.client.view.gui.utilities;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Labels {

    private Labels() {
    }

    static Label studentsNumber(int number) {
        Label label = new Label(String.format("x%1d", number));
        label.setFont(Font.font(14));
        label.setTextFill(Color.WHITE);
        return label;
    }
}
