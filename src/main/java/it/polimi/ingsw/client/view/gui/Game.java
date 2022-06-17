package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.client.view.gui.utilities.*;
import it.polimi.ingsw.utilities.ClientState;
import it.polimi.ingsw.utilities.Pair;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
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
    private Line next1;
    @FXML
    private Line next2;
    @FXML
    private Line next3;
    @FXML
    private Line next4;
    @FXML
    private Line next5;
    @FXML
    private Line next6;
    @FXML
    private Line next7;
    @FXML
    private Line next8;
    @FXML
    private Line next9;
    @FXML
    private Line next10;
    @FXML
    private Line next11;
    @FXML
    private Line next12;
    @FXML
    private Label phase;
    @FXML
    private Label round;

    private List<Button> islandButtons;
    private List<Button> cloudButtons;
    /*private Button diningRoomButton;
    private List<Button> entranceButtons;
    private List<Button> assistantButtons;*/

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
        list.get(0).enableEntranceButtons();
        list.get(0).enableAssistantButtons();
        list.get(0).enableDiningRoomButton();
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
            this.cloudButtons = new ArrayList<>();
            cloudsLayout.getChildren().clear();
            for (CloudContainer cloud : clouds) {
                HBox.setMargin(cloud.getPane(), new Insets(5));
                cloudsLayout.getChildren().add(cloud.getPane());
                cloudButtons.add((Button) cloud.getPane().getChildrenUnmodifiable().get(2));
            }
        });
    }

    /**
     * Adds all the islands to the GUI.
     */
    private void addIslands() {
        Pair<List<IslandContainer>, List<Boolean>> tmp = Islands.get(client.getController().getGameModel().getGameBoard(), List.of(
                next1, next2, next3, next4, next5, next6,
                next7, next8, next9, next10, next11, next12
        ));
        this.islands = tmp.key();
        Platform.runLater(() -> {
            this.islandButtons = new ArrayList<>();
            islandsLayout.getChildren().clear();
            islandsLayout.add(this.islands.get(0).getPane(), 0, 1);
            islandButtons.add((Button) this.islands.get(0).getPane().getChildrenUnmodifiable().get(2));
            for (int index = 1; index < 5; index++) {
                islandsLayout.add(this.islands.get(index).getPane(), index, 0);
                islandButtons.add((Button) this.islands.get(index).getPane().getChildrenUnmodifiable().get(2));
            }
            islandsLayout.add(this.islands.get(5).getPane(), 5, 1);
            islandButtons.add((Button) this.islands.get(5).getPane().getChildrenUnmodifiable().get(2));

            islandsLayout.add(this.islands.get(6).getPane(), 5, 3);
            islandButtons.add((Button) this.islands.get(6).getPane().getChildrenUnmodifiable().get(2));

            for (int index = 7; index < 11; index++) {
                islandsLayout.add(this.islands.get(index).getPane(), 11 - index, 4);
                islandButtons.add((Button) this.islands.get(index).getPane().getChildrenUnmodifiable().get(2));

            }
            islandsLayout.add(this.islands.get(11).getPane(), 0, 3);
            islandButtons.add((Button) this.islands.get(11).getPane().getChildrenUnmodifiable().get(2));

            for (int index = 0; index < 11; index++)
                if (client.getController().getGameModel().getGameBoard().getIslandById(index).hasNext())
                    this.islands.get(index).connect();
        });
    }
}
