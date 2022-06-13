package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.GameModel;
import it.polimi.ingsw.client.model.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.HashMap;
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
            HBox board = Boxes.board(gameModel, player.getName(), boardContainer);
            vBox.getChildren().addAll(name, board);
            VBox.setMargin(name, new Insets(10, 0, 10, 0));
            VBox.setMargin(board, new Insets(0, 0, 10, 0));
        }

        return Collections.unmodifiableMap(map);
    }
}
