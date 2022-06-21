package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.GameBoard;
import it.polimi.ingsw.client.view.gui.CommandAssembler;
import it.polimi.ingsw.utilities.Pair;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Parses the model to obtain the clouds.
 */
public class Islands {

    private Islands() {
    }

    /**
     * Returns all the islands.
     *
     * @param gameBoard   GameBoard from which get all the information.
     * @param connections The List of lines which connect the various islands.
     * @param assembler   CommandAssembler for generating the commands to send to the server.
     * @return A List of islands and a corresponding list of booleans which indicate whether the island is connected to the following one (subdivision done only for graphical purposes).
     */
    public static Pair<List<IslandContainer>, List<Boolean>> get(GameBoard gameBoard, List<Line> connections, CommandAssembler assembler) {

        IslandContainer islandContainer;
        List<IslandContainer> list = new ArrayList<>();
        List<Boolean> nextList = new ArrayList<>();

        for (int index = 0; index < 12; index++) {
            final int islandId = index;
            islandContainer = new IslandContainer();
            list.add(islandContainer);

            Group group = new Group();
            islandContainer.setPane(group);

            islandContainer.setConnection(connections.get(index));

            Button islandButton = new Button("");
            islandButton.setStyle("-fx-border-color: #FCFFAD;" +
                    "-fx-border-width: 1px;" +
                    "-fx-border-radius: 50em;" +
                    "-fx-background-radius: 50em;" +
                    "-fx-background-color: radial-gradient(focus-distance 0% ,center 50% 50%, radius 90%, transparent, #FCFFAD);" +
                    "-fx-min-width: 155px;" +
                    "-fx-min-height: 155px;");
            islandButton.setVisible(false);
            islandButton.setOnAction(mouseEvent -> assembler.manageIslandSelection(islandId));
            islandButton.setVisible(false);

            ImageView ban = Images.banIsland();
            islandContainer.setBan(ban);

            group.getChildren().addAll(Images.island(), Grids.island(gameBoard.getIslandById(index), islandContainer), ban, islandButton);
        }

        return new Pair<>(Collections.unmodifiableList(list), nextList);
    }
}
