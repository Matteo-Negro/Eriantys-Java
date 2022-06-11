package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.Player;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Boards {

    private Boards() {
    }

    public static Map<String, BoardContainer> get(List<Player> players) {

        BoardContainer boardContainer;
        Map<String, BoardContainer> map = new HashMap<>();

        for (Player player : players) {

            boardContainer = new BoardContainer();
            map.put(player.getName(), boardContainer);

            VBox vBox = new VBox();
            boardContainer.setPane(vBox);
            vBox.getChildren().addAll();
        }

        return Collections.unmodifiableMap(map);
    }
}
