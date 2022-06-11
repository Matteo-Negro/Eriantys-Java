package it.polimi.ingsw.client.view.gui.utilities;

import javafx.scene.Parent;

public class BoardContainer {

    private Parent pane;

    BoardContainer() {
        pane = null;
    }

    public Parent getPane() {
        return pane;
    }

    void setPane(Parent pane) {
        this.pane = pane;
    }
}
