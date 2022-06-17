package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.GameBoard;
import javafx.scene.Group;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.client.view.gui.utilities.CommandAssembler.manageIslandSelection;

public class Islands {

    private Islands() {
    }

    public static List<IslandContainer> get(GameBoard gameBoard) {

        IslandContainer islandContainer;
        List<IslandContainer> list = new ArrayList<>();

        for (int index = 0; index < 12; index++) {
            final int islandId = index;
            islandContainer = new IslandContainer();
            list.add(islandContainer);

            Group group = new Group();
            islandContainer.setPane(group);

            Button islandButton = new Button("");
            islandButton.setStyle("-fx-border-color: #FCFFAD;" +
                    "-fx-border-width: 1px;" +
                    "-fx-border-radius: 50em;" +
                    "-fx-background-radius: 50em;" +
                    "-fx-background-color: radial-gradient(focus-distance 0% ,center 50% 50%, radius 90%, transparent, #FCFFAD);" +
                    "-fx-min-width: 155px;" +
                    "-fx-min-height: 155px;");
            islandButton.setOnAction(mouseEvent -> manageIslandSelection(islandId));
            group.getChildren().addAll(Images.island(), Grids.island(gameBoard.getIslandById(index), islandContainer), islandButton);
        }

        return Collections.unmodifiableList(list);
    }
}
