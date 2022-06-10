package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.GameBoard;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Islands {

    private Islands() {
    }

    public static List<IslandContainer> get(GameBoard gameBoard) {

        IslandContainer islandContainer;
        List<IslandContainer> list = new ArrayList<>();

        for (int index = 0; index < 12; index++) {

            islandContainer = new IslandContainer();
            list.add(islandContainer);

            Group group = new Group();
            islandContainer.setPane(group);
            group.getChildren().addAll(Images.island(), Grids.island(gameBoard.getIslandById(index), islandContainer));
        }

        return Collections.unmodifiableList(list);
    }
}
