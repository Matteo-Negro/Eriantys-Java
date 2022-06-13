package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.utilities.WizardType;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class BoardContainer {

    private ImageView assistant;
    private Label coins;
    private Parent pane;
    private WizardType wizard;

    BoardContainer() {
        assistant = null;
        coins = null;
        pane = null;
        wizard = null;
    }

    public Parent getPane() {
        return pane;
    }

    void setPane(Parent pane) {
        this.pane = pane;
    }

    public void enable(boolean value) {
        Platform.runLater(() -> {
            if (value)
                pane.setStyle("-fx-background-color: #38a2ed");
            else
                pane.setStyle("-fx-background-color: rgba(255, 255, 255, 0%)");
        });
    }

    void setAssistant(ImageView assistant) {
        this.assistant = assistant;
    }

    public void setAssistant(Integer id) {
        Platform.runLater(() -> {
            if (id == null)
                assistant.setImage(Images.getWizardByType(wizard));
            else
                assistant.setImage(Images.getAssistantById(id));
        });
    }

    void setCoins(Label coins) {
        this.coins = coins;
    }

    public void setCoins(int coins) {
        if (this.coins != null && coins >= 0)
            this.coins.setText(String.format("x%d", coins));
    }

    void setWizard(WizardType wizard) {
        this.wizard = wizard;
    }
}
