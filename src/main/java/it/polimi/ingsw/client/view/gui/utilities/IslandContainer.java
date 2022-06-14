package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.EnumMap;
import java.util.Map;

public class IslandContainer {

    private ImageView motherNature;
    private Parent pane;
    private Map<HouseColor, HBox> studentsBoxes;
    private Map<HouseColor, Label> studentsLabels;
    private ImageView tower;

    IslandContainer() {
        motherNature = null;
        pane = null;
        studentsBoxes = null;
        studentsLabels = null;
        tower = null;
    }

    void setMotherNature(ImageView motherNature) {
        this.motherNature = motherNature;
    }

    public void setMotherNatureVisibility(boolean visible) {
        Platform.runLater(() -> {
            motherNature.setVisible(visible);
        });
    }

    public Parent getPane() {
        return pane;
    }

    void setPane(Parent pane) {
        this.pane = pane;
    }

    void setStudentsBoxes(HouseColor houseColor, HBox hBox) {
        if (studentsBoxes == null)
            studentsBoxes = new EnumMap<>(HouseColor.class);
        studentsBoxes.put(houseColor, hBox);
    }

    void setStudentsLabels(HouseColor houseColor, Label label) {
        if (studentsLabels == null)
            studentsLabels = new EnumMap<>(HouseColor.class);
        studentsLabels.put(houseColor, label);
    }

    public void setStudentsNumber(HouseColor houseColor, int number) {
        Platform.runLater(() -> {
            studentsBoxes.get(houseColor).setVisible(number != 0);
            studentsLabels.get(houseColor).setText(String.format("x%1d", number));
        });
    }

    void setTower(ImageView tower) {
        this.tower = tower;
    }

    public void setTower(TowerType towerType) {
        if (towerType != null)
            Platform.runLater(() -> {
                tower.setImage(Images.getTowerRealmByColor(towerType));
                tower.setVisible(true);
            });
    }
}
