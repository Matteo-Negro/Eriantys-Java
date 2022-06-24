package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.GameModel;
import it.polimi.ingsw.client.model.Player;
import it.polimi.ingsw.client.view.gui.CommandAssembler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses the model to obtain the boards.
 */
public class Boards {

    private Boards() {
    }

    /**
     * Returns all the boards.
     *
     * @param gameModel     GameModel from which get all the information.
     * @param assembler     CommandAssembler for generating the commands to send to the server.
     * @param currentPlayer The name of the players who owns the instance.
     * @return A Map with the username and it's respective board.
     */
    public static Map<String, BoardContainer> get(GameModel gameModel, CommandAssembler assembler, String currentPlayer) {

        BoardContainer boardContainer;
        Map<String, BoardContainer> map = new HashMap<>();

        for (Player player : gameModel.getPlayers()) {

            boardContainer = new BoardContainer(assembler);
            map.put(player.getName(), boardContainer);

            VBox vBox = new VBox();
            Group group = new Group();
            boardContainer.setPane(group);

            Button diningRoomButton = new Button("");
            diningRoomButton.setStyle("-fx-min-width: 380px;" +
                    "-fx-min-height: 280px;" +
                    "-fx-border-width: 2px;" +
                    "-fx-border-color: #FCFFAD;" +
                    "-fx-padding: 0px;" +
                    "-fx-background-color: radial-gradient(focus-distance 0%, center 50% 50%, radius 100% ,transparent, #FCFFAD);");
            diningRoomButton.setTranslateX(118);
            diningRoomButton.setTranslateY(74);
            diningRoomButton.setOnMouseClicked(mouseEvent -> assembler.manageDiningRoomSelection());
            diningRoomButton.setVisible(false);

            vBox.setAlignment(Pos.CENTER);
            Label name = Labels.playerName(player.getName());
            name.setStyle("-fx-font-weight: bold;");

            HBox board = Boxes.board(gameModel, player.getName(), boardContainer);
            vBox.getChildren().addAll(name, board);
            VBox.setMargin(name, new Insets(10, 0, 10, 0));
            VBox.setMargin(board, new Insets(0, 0, 10, 0));

            if (player.getName().equals(currentPlayer)) {
                vBox.getChildren().add(Various.assistantsPane(assembler, gameModel.getPlayerByName(player.getName()).getHand()));
                boardContainer.setHand(gameModel.getPlayerByName(player.getName()).getHand());
            }

            group.getChildren().addAll(vBox, diningRoomButton);
        }

        return Collections.unmodifiableMap(map);
    }
}
