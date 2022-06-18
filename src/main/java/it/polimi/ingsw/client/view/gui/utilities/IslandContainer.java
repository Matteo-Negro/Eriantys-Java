package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;

import java.util.EnumMap;
import java.util.Map;

public class IslandContainer {

    private Line connection;
    private ImageView motherNature;
    private Parent pane;
    private Map<HouseColor, HBox> studentsBoxes;
    private Map<HouseColor, Label> studentsLabels;
    private ImageView tower;

    IslandContainer() {
        connection = null;
        motherNature = null;
        pane = null;
        studentsBoxes = null;
        studentsLabels = null;
        tower = null;
    }

    void setConnection(Line connection) {
        this.connection = connection;
    }

    public void connect() {
        Platform.runLater(() -> connection.setVisible(true));
    }

    void setMotherNature(ImageView motherNature) {
        this.motherNature = motherNature;
    }

    public void updateMotherNatureVisibility(boolean visible) {
        Platform.runLater(() -> motherNature.setVisible(visible));
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

    public void updateStudents(Map<HouseColor, Integer> map) {
        Platform.runLater(() -> {
            for (Map.Entry<HouseColor, Integer> entry : map.entrySet()) {
                studentsBoxes.get(entry.getKey()).setVisible(entry.getValue() != 0);
                studentsLabels.get(entry.getKey()).setText(String.format("x%1d", entry.getValue()));
            }
        });
    }

    void setTower(ImageView tower) {
        this.tower = tower;
    }

    public void updateTower(TowerType towerType) {
        if (towerType != null)
            Platform.runLater(() -> {
                tower.setImage(Images.getTowerRealmByColor(towerType));
                tower.setVisible(true);
            });
    }
}
