package it.polimi.ingsw.client.view.gui.utilities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Various {

    private Various() {
    }

    public static Rectangle rectangle() {
        Rectangle rectangle = new Rectangle(900, 5);
        rectangle.setArcWidth(5);
        rectangle.setArcHeight(5);
        rectangle.setSmooth(true);
        rectangle.setFill(Color.WHITE);
        rectangle.setStrokeWidth(0);
        return rectangle;
    }
}
