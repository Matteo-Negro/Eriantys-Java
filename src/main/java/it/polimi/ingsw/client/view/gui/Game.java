package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.client.view.gui.utilities.*;
import it.polimi.ingsw.utilities.ClientState;
import it.polimi.ingsw.utilities.Log;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Game implements Update {

    private ClientGui client;

    private Map<String, BoardContainer> boards;
    private List<CloudContainer> clouds;
    private List<IslandContainer> islands;

    @FXML
    private VBox boardsLayout;
    @FXML
    private HBox cloudsLayout;
    @FXML
    private Label id;
    @FXML
    private GridPane islandsLayout;
    @FXML
    private Label phase;
    @FXML
    private Label round;

    /**
     * Initializes the scene.
     */
    public void initialize() {
        client = ClientGui.getInstance();
        ClientGui.link(ClientState.GAME_RUNNING, this);
    }

    /**
     * Prepares the scene for displaying.
     */
    @Override
    public void prepare() {
        Platform.runLater(() -> {
            id.requestFocus();
            id.setText(client.getController().getGameCode());
            round.setText(String.valueOf(client.getController().getGameModel().getRound()));
            phase.setText(client.getController().getClientState().name().toLowerCase(Locale.ROOT));
        });
        new Thread(this::addBoards).start();
        new Thread(this::addClouds).start();
        new Thread(this::addIslands).start();
        new Thread(new it.polimi.ingsw.client.view.gui.updates.Game(client)).start();
    }

    /**
     * Goes back to main menu.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void exit(Event event) {
        EventProcessing.exit(event, client);
    }

    /**
     * Adds all the boards to the GUI.
     */
    private void addBoards() {
        boards = Boards.get(client.getController().getGameModel());
        List<BoardContainer> list = reorder();
        list.get(0).enable(true);
        Platform.runLater(() -> {
            Rectangle rectangle;
            boardsLayout.getChildren().clear();
            for (BoardContainer board : list) {
                boardsLayout.getChildren().add(board.getPane());
                VBox.setMargin(board.getPane(), new Insets(10));
                if (list.get(list.size() - 1) != board) {
                    rectangle = Various.rectangle();
                    boardsLayout.getChildren().add(rectangle);
                    VBox.setMargin(rectangle, new Insets(10, 0, 0, 0));
                }
            }
        });
    }

    /**
     * Reorders the boards in order to put in first position the current player's one.
     *
     * @return The sorted list.
     */
    private List<BoardContainer> reorder() {
        List<BoardContainer> list = new ArrayList<>();
        list.add(boards.get(client.getController().getUserName()));
        list.addAll(boards.values().stream().filter(board -> board != list.get(0)).toList());
        return list;
    }

    /**
     * Adds all the clouds to the GUI.
     */
    private void addClouds() {
        clouds = Clouds.get(client.getController().getGameModel().getGameBoard().getClouds(), client.getController().getGameModel().getPlayersNumber());
        Platform.runLater(() -> {
            cloudsLayout.getChildren().clear();
            for (CloudContainer cloud : clouds)
                cloudsLayout.getChildren().add(cloud.getPane());
        });
    }

    /**
     * Adds all the islands to the GUI.
     */
    private void addIslands() {
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
