package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.model.GameModel;
import it.polimi.ingsw.client.model.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Boards {

    private Boards() {
    }

    public static Map<String, BoardContainer> get(GameModel gameModel) {

        BoardContainer boardContainer;
        Map<String, BoardContainer> map = new HashMap<>();

        for (Player player : gameModel.getPlayers()) {

            boardContainer = new BoardContainer();
            map.put(player.getName(), boardContainer);

            VBox vBox = new VBox();
            boardContainer.setPane(vBox);
            vBox.setAlignment(Pos.CENTER);

            Label name = Labels.playerName(player.getName());
            vBox.getChildren().addAll(name, Boxes.board(gameModel));
            VBox.setMargin(name, new Insets(0, 0, 10, 0));
        }

        return Collections.unmodifiableMap(map);
    }
}
