package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.client.view.gui.utilities.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Game {

    private static Scene scene = null;
    private static ClientGui client = null;

    private static List<CloudContainer> clouds = null;
    private static List<IslandContainer> islands = null;

    @FXML
    private static VBox boardsLayout;
    @FXML
    private static HBox cloudsLayout;
    @FXML
    private static Label id;
    @FXML
    private static GridPane islandsLayout;
    @FXML
    private static Button exit;
    @FXML
    private static Label phase;
    @FXML
    private static Label round;

    private Game() {
    }

    /**
     * Initializes the scene.
     *
     * @param client The client to which change the state.
     * @throws IOException Thrown if there is an error somewhere.
     */
    public static void initialize(ClientGui client) throws IOException {
        if (client == null)
            return;
        Game.client = client;
        scene = new Scene(FXMLLoader.load(Objects.requireNonNull(Game.class.getResource("/fxml/game.fxml"))));
        lookup();
        addEvents();
    }

    /**
     * Returns the scene.
     *
     * @return The scene.
     */
    public static Scene getScene() {
        id.requestFocus();
        id.setText(client.getController().getGameCode());
        round.setText(String.valueOf(client.getController().getGameModel().getRound()));
        phase.setText(client.getController().getClientState().name().toLowerCase(Locale.ROOT));
        new Thread(Game::addIslands).start();
        new Thread(Game::addClouds).start();
//        new Thread(Game::addBoards).start();
        return scene;
    }

    /**
     * Looks for every used element in the scene.
     */
    private static void lookup() {
        boardsLayout = (VBox) scene.lookup("#boards");
        cloudsLayout = (HBox) scene.lookup("#clouds");
        id = (Label) scene.lookup("#id");
        islandsLayout = (GridPane) scene.lookup("#islands");
        exit = (Button) scene.lookup("#exit");
        phase = (Label) scene.lookup("#phase");
        round = (Label) scene.lookup("#round");
    }

    /**
     * Adds all the events to the scene.
     */
    private static void addEvents() {

        ExitEvent.addEvent(exit, client);
    }

    private static void addBoards() {
        Platform.runLater(() -> boardsLayout.getChildren().clear());
    }

    private static void addClouds() {
        clouds = Clouds.get(client.getController().getGameModel().getGameBoard().getClouds(), client.getController().getGameModel().getPlayersNumber());
        Platform.runLater(() -> {
            cloudsLayout.getChildren().clear();
            for (CloudContainer cloud : clouds)
                cloudsLayout.getChildren().add(cloud.getPane());
        });
    }

    private static void addIslands() {
        islands = Islands.get(client.getController().getGameModel().getGameBoard());
        Platform.runLater(() -> {
            islandsLayout.getChildren().clear();
            islandsLayout.add(islands.get(0).getPane(), 0, 1);
            for (int index = 1; index < 5; index++)
                islandsLayout.add(islands.get(index).getPane(), index, 0);
            islandsLayout.add(islands.get(5).getPane(), 5, 1);
            islandsLayout.add(islands.get(6).getPane(), 5, 2);
            for (int index = 7; index < 11; index++)
                islandsLayout.add(islands.get(index).getPane(), 11 - index, 3);
            islandsLayout.add(islands.get(11).getPane(), 0, 2);
        });
    }
}
