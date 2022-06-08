package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.utilities.TowerType;
import javafx.scene.Group;
import javafx.scene.image.ImageView;

public class IslandContainer {

    private ImageView motherNature;
    private Group pane;
    private ImageView tower;

    IslandContainer() {
        motherNature = null;
        pane = null;
        tower = null;
    }

    public ImageView getMotherNature() {
        return motherNature;
    }

    void setMotherNature(ImageView motherNature) {
        this.motherNature = motherNature;
    }

    public void setMotherNatureVisibility(boolean visible) {
        motherNature.setVisible(visible);
    }

    public Group getPane() {
        return pane;
    }

    void setPane(Group pane) {
        this.pane = pane;
    }

    public ImageView getTower() {
        return tower;
    }

    void setTower(ImageView tower) {
        this.tower = tower;
    }

    public void setTower(TowerType towerType) {
        if (towerType != null)
            tower.setImage(Images.getTower(towerType));
    }
}
