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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
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
    private AnchorPane boardsTab;
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
    private ScrollPane realmTab;
    @FXML
    private Label round;

    private CommandAssembler commandAssembler;
    private List<Button> islandButtons;
    private List<Button> cloudButtons;
    private List<BoardContainer> boardsList;

    /**
     * Initializes the scene.
     */
    public void initialize() {
        client = ClientGui.getInstance();
        ClientGui.link(ClientState.GAME_RUNNING, this);
        this.commandAssembler = new CommandAssembler(this.client.getController());
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
            phase.setText(client.getController().getGameModel().getPhase().name().toLowerCase(Locale.ROOT));
        });
        new Thread(this::prepareGraphicElements).start();
        new Thread(new it.polimi.ingsw.client.view.gui.updates.Game(client)).start();
    }

    /**
     * Goes back to main menu.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void exit(Event event) {
        if (EventProcessing.boardsToggle(event)) {
            toggleView(event);
            return;
        }
        EventProcessing.exit(event, client);
    }

    /**
     * Calls the methods that prepare the graphic elements and the buttons.
     */
    private void prepareGraphicElements() {
        Thread boardsThread = new Thread(this::addBoards);
        boardsThread.start();
        Thread cloudsThread = new Thread(this::addClouds);
        cloudsThread.start();
        Thread islandsThread = new Thread(this::addIslands);
        islandsThread.start();
        synchronized (this) {
            while (boardsThread.isAlive() || cloudsThread.isAlive() || islandsThread.isAlive()) {
                try {
                    this.wait(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        activateButtons();
    }

    /**
     * Toggles the views.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    public void toggleView(Event event) {
        if (EventProcessing.boardsToggle(event))
            Platform.runLater(() -> {
                boardsTab.setVisible(!boardsTab.isVisible());
                realmTab.setVisible(!realmTab.isVisible());
                if (boardsTab.isVisible()) {
                }
            });
    }

    /**
     * Adds all the boards to the GUI.
     */
    private void addBoards() {
        boards = Boards.get(client.getController().getGameModel(), commandAssembler);
        this.boardsList = reorder();
        Platform.runLater(() -> {
            Rectangle rectangle;
            boardsLayout.getChildren().clear();
            for (BoardContainer board : boardsList) {
                boardsLayout.getChildren().add(board.getPane());
                VBox.setMargin(board.getPane(), new Insets(10));
                if (boardsList.get(boardsList.size() - 1) != board) {
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
        clouds = Clouds.get(client.getController().getGameModel().getGameBoard().getClouds(), client.getController().getGameModel().getPlayersNumber(), commandAssembler);
        Platform.runLater(() -> {
            cloudsLayout.getChildren().clear();
            for (CloudContainer cloud : clouds) {
                HBox.setMargin(cloud.getPane(), new Insets(5));
                cloudsLayout.getChildren().add(cloud.getPane());
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
        ), commandAssembler);
        this.islands = tmp.key();
        Platform.runLater(() -> {
            islandsLayout.getChildren().clear();
            islandsLayout.add(this.islands.get(0).getPane(), 0, 1);
            for (int index = 1; index < 5; index++) {
                islandsLayout.add(this.islands.get(index).getPane(), index, 0);
            }
            islandsLayout.add(this.islands.get(5).getPane(), 5, 1);

            islandsLayout.add(this.islands.get(6).getPane(), 5, 3);

            for (int index = 7; index < 11; index++) {
                islandsLayout.add(this.islands.get(index).getPane(), 11 - index, 4);

            }
            islandsLayout.add(this.islands.get(11).getPane(), 0, 3);

            for (int index = 0; index < 11; index++)
                if (client.getController().getGameModel().getGameBoard().getIslandById(index).hasNext())
                    this.islands.get(index).connect();
        });
    }

    /**
     * Initializes the buttons.
     */
    private void initializeButtons() {
        for (BoardContainer board : this.boardsList) {
            List<Button> entranceButtons = board.getEntranceButtons();

            board.enableEntranceButtons(false);
            board.enableAssistantButtons(false);
            board.enableDiningRoomButton(false);
        }

        this.cloudButtons = new ArrayList<>();
        for (CloudContainer cloud : this.clouds)
            cloudButtons.add((Button) cloud.getPane().getChildrenUnmodifiable().get(2));

        this.islandButtons = new ArrayList<>();
        for (int islandId = 0; islandId < 12; islandId++)
            islandButtons.add((Button) this.islands.get(islandId).getPane().getChildrenUnmodifiable().get(2));
    }

    /**
     * Activates the due buttons for the current phase.
     */
    private void activateButtons() {
        BoardContainer firstBoard = this.boardsList.get(0);
        Platform.runLater(() -> {
            initializeButtons();
            if (!this.client.getController().hasCommunicationToken()) {
                firstBoard.enableDiningRoomButton(false);
                firstBoard.enableAssistantButtons(false);
                firstBoard.enableEntranceButtons(false);
                enableIslandButtons(false);
                enableCloudButtons(false);
            } else {
                switch (this.client.getController().getGameModel().getSubphase()) {
                    case PLAY_ASSISTANT -> {
                        firstBoard.enableDiningRoomButton(false);
                        firstBoard.enableAssistantButtons(true);
                        firstBoard.enableEntranceButtons(false);
                        enableIslandButtons(false);
                        enableCloudButtons(false);
                    }
                    case MOVE_STUDENT_1, MOVE_STUDENT_2, MOVE_STUDENT_3, MOVE_STUDENT_4 -> {
                        firstBoard.enableAssistantButtons(false);
                        firstBoard.enableDiningRoomButton(true);
                        firstBoard.enableEntranceButtons(true);
                        enableIslandButtons(true);
                        enableCloudButtons(false);
                    }
                    case MOVE_MOTHER_NATURE -> {
                        firstBoard.enableAssistantButtons(false);
                        firstBoard.enableDiningRoomButton(false);
                        firstBoard.enableEntranceButtons(false);
                        enableIslandButtons(true);
                        enableCloudButtons(false);
                    }
                    case CHOOSE_CLOUD -> {
                        firstBoard.enableAssistantButtons(false);
                        firstBoard.enableDiningRoomButton(false);
                        firstBoard.enableEntranceButtons(false);
                        enableIslandButtons(false);
                        enableCloudButtons(true);
                    }
                }
            }
        });

    }

    /**
     * Set the island buttons visibility to the given value.
     *
     * @param enable The value to set.
     */
    private void enableIslandButtons(boolean enable) {
        for (Button islandButton : this.islandButtons)
            islandButton.setVisible(enable);
    }

    /**
     * Sets the cloud buttons visibility to the given value.
     *
     * @param enable The value to set.
     */
    private void enableCloudButtons(boolean enable) {
        for (Button cloudButton : this.cloudButtons)
            cloudButton.setVisible(enable);
    }
}
