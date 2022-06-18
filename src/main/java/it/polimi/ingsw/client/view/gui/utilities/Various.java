package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.Assistant;
import it.polimi.ingsw.client.model.GameModel;
import it.polimi.ingsw.client.model.Player;
import it.polimi.ingsw.client.view.gui.CommandAssembler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Various {

    private Various() {
    }

    public static FlowPane assistantsPane(CommandAssembler assembler, GameModel gameModel, Player player) {

        FlowPane flowPane = new FlowPane();
        flowPane.setPrefWidth(900);
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setPrefHeight(Region.USE_COMPUTED_SIZE);

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
            assistantButton.setOnMouseClicked(mouseEvent -> assembler.manageAssistantSelection(assistantId));
            FlowPane.setMargin(assistantButton, new Insets(5));
            flowPane.getChildren().add(assistantButton);
        }

        flowPane.setVisible(false);
        flowPane.setManaged(false);

        return flowPane;
    }

    public static Rectangle rectangle() {
        Rectangle rectangle = new Rectangle(900, 5);
        rectangle.setArcWidth(5);
        rectangle.setArcHeight(5);
        rectangle.setSmooth(true);
        rectangle.setFill(Color.WHITE);
        rectangle.setStrokeWidth(0);
        return rectangle;
    }
}
