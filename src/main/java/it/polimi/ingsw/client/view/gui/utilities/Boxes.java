package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.GameModel;
import it.polimi.ingsw.utilities.HouseColor;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Boxes {

    private Boxes() {
    }

    static HBox board(GameModel gameModel) {

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);

        Group group = new Group();
        group.getChildren().add(Images.board());
        if (gameModel.isExpert())
            group.getChildren().add(Images.boardCoins());

        hBox.getChildren().add(group);

        return hBox;
    }

    static HBox islandStudent(HouseColor houseColor, int students, IslandContainer islandContainer) {

        HBox hBox = new HBox();
        hBox.setFillHeight(true);
        hBox.setAlignment(Pos.CENTER);

        Label label = Labels.studentsNumber(students);
        islandContainer.setStudentsLabels(houseColor, label);

        hBox.getChildren().addAll(Images.student3d(houseColor), label);
        hBox.setVisible(students != 0);

        islandContainer.setStudentsBoxes(houseColor, hBox);

        return hBox;
    }
}
