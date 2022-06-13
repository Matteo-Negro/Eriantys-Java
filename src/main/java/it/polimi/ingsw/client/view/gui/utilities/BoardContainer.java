package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.WizardType;
import it.polimi.ingsw.utilities.exceptions.IllegalActionException;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.*;

public class BoardContainer {

    private ImageView assistant;
    private Label coins;
    private Map<HouseColor, Integer> entranceColors;
    private List<ImageView> entranceImages;
    private Parent pane;
    private WizardType wizard;

    BoardContainer() {
        assistant = null;
        coins = null;
        entranceColors = null;
        entranceImages = null;
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

    void setEntrance(List<ImageView> entranceImages) {
        this.entranceImages = Collections.unmodifiableList(entranceImages);
        if (entranceColors != null)
            updateEntrance(entranceToList());
    }

    void setEntrance(Map<HouseColor, Integer> entranceColors) {
        this.entranceColors = new EnumMap<>(entranceColors);
        if (entranceImages != null)
            updateEntrance(entranceToList());
    }

    public void addToEntrance(HouseColor houseColor) {
        entranceColors.replace(houseColor, entranceColors.get(houseColor) + 1);
        updateEntrance();
    }

    public void removeFromEntrance(HouseColor houseColor) throws IllegalActionException {
        if (entranceColors.get(houseColor) == 0)
            throw new IllegalActionException("No more students of color " + houseColor.name().toLowerCase());
        entranceColors.replace(houseColor, entranceColors.get(houseColor) - 1);
        updateEntrance();
    }

    void setWizard(WizardType wizard) {
        this.wizard = wizard;
    }

    private List<HouseColor> entranceToList() {
        List<HouseColor> list = new ArrayList<>();
        for (Map.Entry<HouseColor, Integer> entry : entranceColors.entrySet())
            for (int index = 0; index < entry.getValue(); index++)
                list.add(entry.getKey());
        for (int index = 0; index < entranceImages.size() - list.size(); index++)
            list.add(null);
        return list;
    }

    private void updateEntrance() {
        List<HouseColor> entrance = entranceToList();
        Platform.runLater(() -> updateEntrance(entrance));
    }

    private void updateEntrance(List<HouseColor> students) {
        for (int index = 0; index < entranceImages.size(); index++) {
            entranceImages.get(index).setImage(Images.getStudent2dByColor(students.get(index)));
            entranceImages.get(index).setVisible(students.get(index) != null);
        }
    }
}
