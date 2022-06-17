package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.Assistant;
import it.polimi.ingsw.client.model.GameModel;
import it.polimi.ingsw.client.model.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.client.view.gui.utilities.CommandAssembler.manageAssistantSelection;
import static it.polimi.ingsw.client.view.gui.utilities.CommandAssembler.manageDiningRoomSelection;

public class Boards {

    private Boards() {
    }

    public static Map<String, BoardContainer> get(GameModel gameModel) {

        BoardContainer boardContainer;
        Map<String, BoardContainer> map = new HashMap<>();

        for (Player player : gameModel.getPlayers()) {
            GridPane gPane = new GridPane();
            gPane.setMinSize(900, 160);
            gPane.setMaxSize(1000, 160);
            gPane.setPadding(new Insets(0, 0, 0, 0));
            gPane.setHgap(5);
            gPane.setVgap(5);
            gPane.setAlignment(Pos.TOP_CENTER);

            for (Assistant availableAssistant : gameModel.getPlayerByName(player.getName()).getHand()) {
                final int assistantId = availableAssistant.getId();
                Button assistantButton = new Button("", Images.assistant(assistantId));
                assistantButton.setStyle("-fx-border-width: 2px;" +
                        "-fx-padding: 2px;" +
                        "-fx-border-color: #FCFFAD;" +
                        "-fx-max-height: 133px;" +
                        "-fx-max-width: 90px;" +
                        "-fx-background-color: transparent;" +
                        "-fx-background-color: radial-gradient(focus-distance 0%, center 50% 50%, radius 100% ,transparent, #FCFFAD);");
                assistantButton.setOnAction(mouseEvent -> manageAssistantSelection(assistantId));
                gPane.add(assistantButton, assistantId, 0);
            }

            boardContainer = new BoardContainer();
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
            diningRoomButton.setVisible(false);
            diningRoomButton.setTranslateX(174);
            diningRoomButton.setTranslateY(70);
            diningRoomButton.setOnAction(mouseEvent -> manageDiningRoomSelection());

            vBox.setAlignment(Pos.CENTER);
            Label name = Labels.playerName(player.getName());

            HBox board = Boxes.board(gameModel, player.getName(), boardContainer);
            vBox.getChildren().addAll(name, board, gPane);
            VBox.setMargin(name, new Insets(10, 0, 10, 0));
            VBox.setMargin(board, new Insets(0, 0, 10, 0));

            group.getChildren().addAll(vBox, diningRoomButton);
        }

        return Collections.unmodifiableMap(map);
    }
}
