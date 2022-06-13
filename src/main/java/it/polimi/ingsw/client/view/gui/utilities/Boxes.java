package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.GameModel;
import it.polimi.ingsw.client.model.Player;
import it.polimi.ingsw.utilities.HouseColor;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Boxes {

    private Boxes() {
    }

    static HBox board(GameModel gameModel, String player, BoardContainer boardContainer) {

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);

        Group group = new Group();
        group.getChildren().add(Images.board());
        if (gameModel.isExpert())
            group.getChildren().add(Images.boardCoins());
        group.getChildren().add(Grids.board(
                gameModel.getPlayerByName(player).getSchoolBoard(),
                gameModel.getPlayersNumber() == 3 ? 9 : 7,
                boardContainer)
        );
        hBox.getChildren().add(group);

        Node node;
        if (gameModel.isExpert())
            node = boardRightBox(gameModel.getPlayerByName(player), boardContainer);
        else
            node = getAssistant(gameModel.getPlayerByName(player), boardContainer);
        HBox.setMargin(node, new Insets(0, 0, 0, 30));
        hBox.getChildren().add(node);

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

    private static ImageView getAssistant(Player player, BoardContainer boardContainer) {
        ImageView assistant;
        if (player.getCurrentPlayedAssistant() == null)
            assistant = Images.wizard(player.getWizard());
        else
            assistant = Images.assistant(player.getCurrentPlayedAssistant().getId());
        boardContainer.setAssistant(assistant);
        boardContainer.setWizard(player.getWizard());
        return assistant;
    }

    private static VBox boardRightBox(Player player, BoardContainer boardContainer) {
        VBox vBox = new VBox();
        ImageView assistant = getAssistant(player, boardContainer);
        VBox.setMargin(assistant, new Insets(0, 0, 30, 0));
        vBox.getChildren().addAll(assistant, coins(player.getCoins(), boardContainer));
        return vBox;
    }

    private static HBox coins(int coins, BoardContainer boardContainer) {
        HBox hBox = new HBox();
        ImageView coin = Images.coin();
        HBox.setMargin(coin, new Insets(0, 5, 0, 0));
        Label number = Labels.coinsNumber(coins);
        boardContainer.setCoins(number);
        hBox.getChildren().addAll(coin, number);
        return hBox;
    }
}
