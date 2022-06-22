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

/**
 * Static class for getting all the required boxes for the game.
 */
public class Boxes {

    private Boxes() {
    }

    /**
     * General box for containing the board and the assistant.
     *
     * @param gameModel      GameModel for getting all the required information.
     * @param player         The name of the player who owns the board.
     * @param boardContainer The BoardContainer for storing all required information.
     * @return The generated HBox.
     */
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

    /**
     * General box for containing a student on an island.
     *
     * @param houseColor      The color of the student to put.
     * @param students        The number of students of that color.
     * @param islandContainer The IslandContainer for storing all required information.
     * @return The generated HBox.
     */
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

    /**
     * General box for containing the assistant and, eventually, the number of owned coins.
     *
     * @param player         The required player.
     * @param boardContainer The BoardContainer for storing all required information.
     * @return The generated VBox.
     */
    private static VBox boardRightBox(Player player, BoardContainer boardContainer) {
        VBox vBox = new VBox();
        ImageView assistant = getAssistant(player, boardContainer);
        VBox.setMargin(assistant, new Insets(0, 0, 30, 0));
        vBox.getChildren().addAll(assistant, coins(player.getCoins(), boardContainer));
        return vBox;
    }

    /**
     * Gets the played assistant or wizard according to the situation.
     *
     * @param player         The player who owns the played assistant.
     * @param boardContainer The BoardContainer for storing all required information.
     * @return The obtained ImageView.
     */
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

    /**
     * General box for containing the number of coins of a player.
     *
     * @param coins          The number of coins to display.
     * @param boardContainer The BoardContainer for storing all required information.
     * @return The generated HBox.
     */
    private static HBox coins(int coins, BoardContainer boardContainer) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        ImageView coin = Images.coin();
        HBox.setMargin(coin, new Insets(0, 5, 0, 0));
        Label number = Labels.coinsNumber(coins);
        boardContainer.setCoins(number);
        hBox.getChildren().addAll(coin, number);
        return hBox;
    }
}
