package it.polimi.ingsw.client.view.gui.scenes;

import it.polimi.ingsw.client.model.*;
import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.client.view.gui.CommandAssembler;
import it.polimi.ingsw.client.view.gui.utilities.*;
import it.polimi.ingsw.utilities.ClientState;
import it.polimi.ingsw.utilities.Log;
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

/**
 * GUI scene for playing the game.
 */
public class Game implements Prepare {

    private ClientGui client;

    private Map<String, BoardContainer> boards;
    private List<CloudContainer> clouds;
    private List<IslandContainer> islands;
    private List<SpecialCharacterContainer> characters;

    @FXML
    private VBox boardsLayout;
    @FXML
    private ScrollPane boardsTab;
    @FXML
    private HBox charactersTab;
    @FXML
    private Label charactersTabButtonNotice;
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
    private Label player;
    @FXML
    private Label phase;
    @FXML
    private AnchorPane realmTab;
    @FXML
    private Label round;

    private CommandAssembler commandAssembler;
    private List<Button> islandButtons;
    private List<Button> cloudButtons;
    private List<BoardContainer> boardsList;
    private List<Button> characterButtons;

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
            realmTab.setVisible(true);
            boardsTab.setVisible(false);
            charactersTab.setVisible(false);
            charactersTabButtonNotice.setVisible(client.getController().getGameModel().isExpert());
            id.setText(client.getController().getGameCode());
            round.setText(String.valueOf(client.getController().getGameModel().getRound()));
            phase.setText(client.getController().getGameModel().getSubPhase().name().toLowerCase(Locale.ROOT));
            player.setText(client.getController().getGameModel().getCurrentPlayer());
        });
        new Thread(this::prepareGraphicElements).start();
    }

    /**
     * Updates all the view.
     */
    public void update() {
        if (client.getController().getGameModel() == null)
            return;
        while (boards == null || boardsList == null || islands == null || clouds == null || (this.client.getController().getGameModel().isExpert() && characters == null)) {
            synchronized (this.client.getController().getLock()) {
                try {
                    this.client.getController().getLock().wait(50);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    Log.debug("Gui: Update thread in Game scene has been interrupted.");
                    return;
                }
            }
        }
        GameModel gameModel = client.getController().getGameModel();
        if (gameModel == null)
            return;
        GameBoard gameBoard = gameModel.getGameBoard();

        Thread infoThread = new Thread(() -> updateInfo(gameModel));
        Thread boardsThread = new Thread(() -> updateBoards(gameModel));
        Thread cloudsThread = new Thread(() -> updateClouds(gameBoard));
        Thread islandsThread = new Thread(() -> updateIslands(gameBoard));
        Thread specialCharactersThread = new Thread(() -> updateSpecialCharacters(gameBoard));

        infoThread.start();
        boardsThread.start();
        cloudsThread.start();
        islandsThread.start();
        if (client.getController().getGameModel().isExpert())
            specialCharactersThread.start();

        try {
            infoThread.join();
            boardsThread.join();
            cloudsThread.join();
            islandsThread.join();
            if (client.getController().getGameModel().isExpert())
                specialCharactersThread.join();
        } catch (InterruptedException e) {
            Log.error(e);
            Thread.currentThread().interrupt();
        }
        activateButtons(false);
    }

    /**
     * Goes back to main menu.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void exit(Event event) {
        if (EventProcessing.boardsToggle(event) || EventProcessing.specialCharactersToggle(event))
            toggleView(event);
        else
            EventProcessing.exit(event, client);
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
                if (charactersTab.isVisible()) {
                    charactersTab.setVisible(false);
                    boardsTab.setVisible(true);
                    realmTab.setVisible(false);
                } else {
                    boardsTab.setVisible(!boardsTab.isVisible());
                    realmTab.setVisible(!realmTab.isVisible());
                }
            });
        else if (EventProcessing.specialCharactersToggle(event) && client.getController().getGameModel().isExpert())
            Platform.runLater(() -> {
                if (boardsTab.isVisible()) {
                    charactersTab.setVisible(true);
                    boardsTab.setVisible(false);
                    realmTab.setVisible(false);
                } else {
                    charactersTab.setVisible(!charactersTab.isVisible());
                    realmTab.setVisible(!realmTab.isVisible());
                }
            });
    }

    /**
     * Calls the methods that prepare the graphic elements and the buttons.
     */
    private void prepareGraphicElements() {

        Thread boardsThread = new Thread(this::addBoards);
        Thread cloudsThread = new Thread(this::addClouds);
        Thread islandsThread = new Thread(this::addIslands);
        Thread specialCharactersThread = new Thread(this::addSpecialCharacters);

        boardsThread.start();
        cloudsThread.start();
        islandsThread.start();
        if (client.getController().getGameModel().isExpert())
            specialCharactersThread.start();

        try {
            boardsThread.join();
            cloudsThread.join();
            islandsThread.join();
            if (client.getController().getGameModel().isExpert())
                specialCharactersThread.join();
        } catch (InterruptedException e) {
            Log.error(e);
            Thread.currentThread().interrupt();
        }

        activateButtons(true);
        Log.debug("Graphic elements initialized.");
    }

    /**
     * Adds all the boards to the GUI.
     */
    private void addBoards() {

        boards = Boards.get(client.getController().getGameModel(), commandAssembler, client.getController().getUserName());
        List<Rectangle> divisors = new ArrayList<>();
        Rectangle rectangle;

        this.boardsList = reorder();

        for (int index = 0; index < boardsList.size() - 1; index++) {
            rectangle = Various.divisor();
            VBox.setMargin(rectangle, new Insets(10, 0, 0, 0));
            divisors.add(rectangle);
        }

        for (BoardContainer board : boardsList)
            VBox.setMargin(board.getPane(), new Insets(10));

        Platform.runLater(() -> {
            boardsLayout.getChildren().clear();
            for (int index = 0; index < boardsList.size(); index++) {
                boardsLayout.getChildren().add(boardsList.get(index).getPane());
                if (index != boardsList.size() - 1)
                    boardsLayout.getChildren().add(divisors.get(index));
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
        for (CloudContainer cloud : clouds)
            HBox.setMargin(cloud.getPane(), new Insets(5));
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

        Pair<List<IslandContainer>, List<Boolean>> tmp = Islands.get(client.getController().getGameModel().getGameBoard(), List.of(
                next1, next2, next3, next4, next5, next6,
                next7, next8, next9, next10, next11, next12
        ), commandAssembler);

        this.islands = tmp.first();

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

            for (int index = 0; index < 12; index++)
                if (client.getController().getGameModel().getGameBoard().getIslandById(index).hasNext())
                    this.islands.get(index).connect();
        });
    }

    /**
     * Adds the special characters to the GUI.
     */
    private void addSpecialCharacters() {
        this.characters = SpecialCharacters.get(client.getController().getGameModel().getGameBoard().getSpecialCharacters(), commandAssembler);
        Platform.runLater(() -> {
            charactersTab.getChildren().clear();
            for (SpecialCharacterContainer specialCharacter : characters) {
                HBox.setMargin(specialCharacter.getPane(), new Insets(10));
                charactersTab.getChildren().add(specialCharacter.getPane());
            }
        });
    }

    /**
     * Initializes the buttons.
     */
    private void initializeButtons() {

        this.cloudButtons = new ArrayList<>();
        for (CloudContainer cloud : this.clouds)
            cloudButtons.add((Button) cloud.getPane().getChildrenUnmodifiable().get(2));

        this.islandButtons = new ArrayList<>();
        for (int islandId = 0; islandId < 12; islandId++)
            islandButtons.add((Button) this.islands.get(islandId).getPane().getChildrenUnmodifiable().get(3));

        if (this.client.getController().getGameModel().isExpert()) {
            this.characterButtons = new ArrayList<>();
            for (SpecialCharacterContainer character : this.characters) {
                characterButtons.add((Button) character.getPane().getChildrenUnmodifiable().get(2));
            }
        }
    }

    /**
     * Activates the due buttons for the current phase.
     */
    private void activateButtons(boolean initialize) {
        BoardContainer firstBoard = this.boardsList.get(0);
        Platform.runLater(() -> {
            if (initialize)
                initializeButtons();
            if (!this.client.getController().hasCommunicationToken()) {
                firstBoard.enableAssistantButtons(false);
                firstBoard.enableDiningRoomButton(false);
                firstBoard.enableEntranceButtons(false);
                enableIslandButtons(false);
                enableCloudButtons(false);
                if (this.client.getController().getGameModel().isExpert())
                    enableCharacterButtons(false);
            } else {
                switch (this.client.getController().getGameModel().getSubPhase()) {
                    case PLAY_ASSISTANT -> {
                        firstBoard.enableAssistantButtons(true);
                        firstBoard.enableDiningRoomButton(false);
                        firstBoard.enableEntranceButtons(false);
                        enableIslandButtons(false);
                        enableCloudButtons(false);
                        if (this.client.getController().getGameModel().isExpert())
                            enableCharacterButtons(false);
                    }
                    case MOVE_STUDENT_1, MOVE_STUDENT_2, MOVE_STUDENT_3, MOVE_STUDENT_4 -> {
                        firstBoard.enableAssistantButtons(false);
                        firstBoard.enableDiningRoomButton(true);
                        firstBoard.enableEntranceButtons(true);
                        enableIslandButtons(true);
                        enableCloudButtons(false);
                        checkCharactersEffectOnButtons(firstBoard);
                    }
                    case MOVE_MOTHER_NATURE -> {
                        firstBoard.enableAssistantButtons(false);
                        firstBoard.enableDiningRoomButton(false);
                        firstBoard.enableEntranceButtons(false);
                        enableIslandButtons(true);
                        enableCloudButtons(false);
                        checkCharactersEffectOnButtons(firstBoard);
                    }
                    case CHOOSE_CLOUD -> {
                        firstBoard.enableAssistantButtons(false);
                        firstBoard.enableDiningRoomButton(false);
                        firstBoard.enableEntranceButtons(false);
                        enableIslandButtons(false);
                        enableCloudButtons(true);
                        checkCharactersEffectOnButtons(firstBoard);
                    }
                }
            }
        });
    }

    /**
     * Checks the active special character and activates the due buttons.
     *
     * @param firstBoard The client-player's board
     */
    private void checkCharactersEffectOnButtons(BoardContainer firstBoard) {
        if (this.client.getController().getGameModel().isExpert()) {
            enableCharacterButtons(true);
            if (this.client.getController().getGameModel().isEffectActive()) {
                int activeCharacterId = 0;
                for (SpecialCharacter character : this.client.getController().getGameModel().getGameBoard().getSpecialCharacters())
                    if (character.isActive())
                        activeCharacterId = character.getId();

                if (this.client.getController().getGameModel().getGameBoard().getSpecialCharacterById(activeCharacterId).getUsesNumber() > 0)
                    switch (activeCharacterId) {
                        case 1, 3, 5 -> enableIslandButtons(true);
                        case 7 -> firstBoard.enableEntranceButtons(true);
                        case 11 -> firstBoard.enableDiningRoomButton(true);
                    }
            }
        }
    }

    /**
     * Updates all the game information.
     *
     * @param gameModel The GameModel connected to the game.
     */
    private void updateInfo(GameModel gameModel) {
        Platform.runLater(() -> {
            round.setText(String.valueOf(gameModel.getRound()));
            phase.setText(gameModel.getSubPhase().name().toLowerCase(Locale.ROOT));
            player.setText(gameModel.getCurrentPlayer());
        });
        Log.debug("info updated");
    }

    /**
     * Updates all the boards.
     *
     * @param gameModel The GameModel connected to the game.
     */
    private void updateBoards(GameModel gameModel) {
        Assistant currentAssistant;
        BoardContainer boardContainer;
        SchoolBoard schoolBoard;
        Player currentPlayer;
        for (Map.Entry<String, BoardContainer> board : boards.entrySet()) {
            currentPlayer = gameModel.getPlayerByName(board.getKey());
            currentAssistant = currentPlayer.getCurrentPlayedAssistant();
            schoolBoard = currentPlayer.getSchoolBoard();
            boardContainer = board.getValue();
            boardContainer.updateAssistant(currentAssistant != null ? currentAssistant.getId() : null);
            boardContainer.updateDiningRoom(schoolBoard.getDiningRoom());
            boardContainer.updateEntrance(schoolBoard.getEntrance());
            boardContainer.updateProfessors(schoolBoard.getProfessors());
            boardContainer.updateTowers(schoolBoard.getTowersNumber());
            if (gameModel.isExpert())
                boardContainer.updateCoins(currentPlayer.getCoins());
        }
        Log.debug("boards updated");
    }

    /**
     * Updates all the clouds.
     *
     * @param gameBoard The GameBoard connected to the game.
     */
    private void updateClouds(GameBoard gameBoard) {

        for (int index = 0; index < gameBoard.getClouds().size(); index++)
            clouds.get(index).updateStudents(gameBoard.getClouds().get(index).getStudents(false));
        Log.debug("clouds updated");
    }

    /**
     * Updates all the islands.
     *
     * @param gameBoard The GameBoard connected to the game.
     */
    private void updateIslands(GameBoard gameBoard) {
        Island island;
        IslandContainer islandContainer;
        for (int index = 0; index < 12; index++) {
            island = gameBoard.getIslandById(index);
            islandContainer = islands.get(index);
            islandContainer.updateMotherNatureVisibility(island.hasMotherNature());
            islandContainer.updateStudents(island.getStudents());
            islandContainer.updateTower(island.getTower());
            islandContainer.updateBan(island.isBanned());
            if (island.hasNext())
                islandContainer.connect();
        }
        Log.debug("Islands updated");
    }

    /**
     * Updates all the special characters.
     */
    private void updateSpecialCharacters(GameBoard gameBoard) {

        if (!this.client.getController().getGameModel().isExpert())
            return;

        SpecialCharacter character;
        SpecialCharacterContainer characterContainer;

        for (int index = 0; index < gameBoard.getSpecialCharacters().size(); index++) {
            character = gameBoard.getSpecialCharacters().get(index);
            characterContainer = this.characters.get(index);
            characterContainer.updateStudents(character.getStudents());
            characterContainer.updateBans(character.getAvailableBans());
            characterContainer.updateExtraPrice(character.isAlreadyPaid());
        }
        Log.debug("Special characters updated");
    }

    /**
     * Set the island buttons visibility to the given second.
     *
     * @param enable The second to set.
     */
    private void enableIslandButtons(boolean enable) {
        for (Button islandButton : this.islandButtons)
            islandButton.setVisible(enable);
    }

    /**
     * Sets the cloud buttons visibility to the given second.
     *
     * @param enable The second to set.
     */
    private void enableCloudButtons(boolean enable) {
        for (Button cloudButton : this.cloudButtons)
            cloudButton.setVisible(enable);
    }

    /**
     * Sets the special character buttons visibility to the given second.
     *
     * @param enable The second to set.
     */
    private void enableCharacterButtons(boolean enable) {
        boolean activePlayer = this.client.getController().hasCommunicationToken();
        boolean effectActive = false;
        int activeCharacterPosition = 0;
        for (SpecialCharacter character : this.client.getController().getGameModel().getGameBoard().getSpecialCharacters()) {
            if (character.isActive()) {
                effectActive = true;
                break;
            }
            activeCharacterPosition++;
        }

        for (Button characterButton : this.characterButtons) {
            characterButton.setVisible(enable && !effectActive && activePlayer);
        }
        for (SpecialCharacterContainer character : characters) {
            character.enableCharacterButton(effectActive && characters.indexOf(character) == activeCharacterPosition, activePlayer, client.getController().getGameModel());
        }
    }
}
