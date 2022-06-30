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

/**
 * This class is a simple interface to manage a GUI island. Provides all the required methods to create and update it.
 */
public class IslandContainer {

    private ImageView ban;
    private Line connection;
    private ImageView motherNature;
    private Parent pane;
    private Map<HouseColor, HBox> studentsBoxes;
    private Map<HouseColor, Label> studentsLabels;
    private ImageView tower;

    /**
     * Default class constructor.
     */
    IslandContainer() {
        ban = null;
        connection = null;
        motherNature = null;
        pane = null;
        studentsBoxes = null;
        studentsLabels = null;
        tower = null;
    }

    /**
     * Gets the pane to print on the screen.
     *
     * @return The pane with all the graphics.
     */
    public Parent getPane() {
        return pane;
    }

    /**
     * Initializes the pane.
     *
     * @param pane The pane to add.
     */
    void setPane(Parent pane) {
        this.pane = pane;
    }

    /**
     * Sets the ban image.
     *
     * @param ban The ban image.
     */
    void setBan(ImageView ban) {
        this.ban = ban;
    }

    /**
     * Toggles ban visibility.
     *
     * @param ban Visibility.
     */
    public void updateBan(boolean ban) {
        Platform.runLater(() -> this.ban.setVisible(ban));
    }

    /**
     * Adds the connection to the following island.
     *
     * @param connection Line which connects to the following island.
     */
    void setConnection(Line connection) {
        this.connection = connection;
        this.connection.setVisible(false);
    }

    /**
     * Connects the island to the following island.
     */
    public void connect() {
        Platform.runLater(() -> connection.setVisible(true));
    }

    /**
     * Sets mother natura image.
     *
     * @param motherNature Mother nature image.
     */
    void setMotherNature(ImageView motherNature) {
        this.motherNature = motherNature;
    }

    /**
     * Toggles mother nature visibility.
     *
     * @param visible Visibility.
     */
    public void updateMotherNatureVisibility(boolean visible) {
        Platform.runLater(() -> motherNature.setVisible(visible));
    }

    /**
     * Links the students to the island.
     *
     * @param houseColor The color of the student.
     * @param hBox       The box which contains the student image and its number.
     */
    void setStudentsBoxes(HouseColor houseColor, HBox hBox) {
        if (studentsBoxes == null)
            studentsBoxes = new EnumMap<>(HouseColor.class);
        studentsBoxes.put(houseColor, hBox);
    }

    /**
     * Sets the student's number labels.
     *
     * @param houseColor The color of the student.
     * @param label      The label which contains the number of students of that color on the island.
     */
    void setStudentsLabels(HouseColor houseColor, Label label) {
        if (studentsLabels == null)
            studentsLabels = new EnumMap<>(HouseColor.class);
        studentsLabels.put(houseColor, label);
    }

    /**
     * Updates the students on the island.
     *
     * @param map The Map with the number of students of each color.
     */
    public void updateStudents(Map<HouseColor, Integer> map) {
        Platform.runLater(() -> {
            for (Map.Entry<HouseColor, Integer> entry : map.entrySet()) {
                studentsBoxes.get(entry.getKey()).setVisible(entry.getValue() != 0);
                studentsLabels.get(entry.getKey()).setText(String.format("x%1d", entry.getValue()));
            }
        });
    }

    /**
     * Sets the tower image.
     *
     * @param tower The tower image.
     */
    void setTower(ImageView tower) {
        this.tower = tower;
    }

    /**
     * Updates the tower on the island (adds one if not preset or changes it).
     *
     * @param towerType The color of the tower to put on the island.
     */
    public void updateTower(TowerType towerType) {
        if (towerType != null)
            Platform.runLater(() -> {
                tower.setImage(Images.getTowerRealmByColor(towerType));
                tower.setVisible(true);
            });
    }
}
