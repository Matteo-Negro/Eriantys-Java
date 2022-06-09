package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.gui.*;
import it.polimi.ingsw.utilities.ClientStates;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.Pair;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientGui extends Application implements View {

    private Stage stage;
    private ClientController controller;
    private static final String defaultTitle = "Eriantys";

    /**
     * @param primaryStage the primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {

        stage = primaryStage;
        controller = new ClientController(this);

        Log.info("Initializing scenes...");
        try {
            Start.initialize(this);
        } catch (IOException e) {
            Log.error("Cannot initialize scenes because of the following error: ", e);
            return;
        }

        changeScene();

        primaryStage.show();

        new Thread(() -> {
            try {
                Create.initialize(this);
                End.initialize(this);
                Game.initialize(this);
                Join.initialize(this);
                Login.initialize(this);
                Menu.initialize(this);
                Start.initialize(this);
                WaitingRoom.initialize(this);
                Log.info("Initialization completed.");
            } catch (IOException e) {
                Log.error("Cannot initialize scenes because of the following error: ", e);
                throw new RuntimeException();
            }
        }).start();
    }

    /**
     * Changes the scene according to the state.
     */
    public void changeScene() {
        ClientStates state = getController().getClientState();
        if (getController().getClientState().equals(ClientStates.CONNECTION_LOST))
            getController().manageConnectionLost();

        Log.info("Displaying " + state);

        stage.setScene(switch (state) {
            case CONNECTION_LOST, START_SCREEN -> Start.getScene();
            case END_GAME -> End.getScene();
            case EXIT -> null;
            case GAME_CREATION -> Create.getScene();
            case GAME_LOGIN -> Login.getScene();
            case GAME_RUNNING -> Game.getScene();
            case GAME_WAITING_ROOM -> WaitingRoom.getScene();
            case JOIN_GAME -> Join.getScene();
            case MAIN_MENU -> Menu.getScene();
        });

        stage.sizeToScene();
        stage.setResizable(state.equals(ClientStates.GAME_RUNNING));

        stage.setTitle(switch (state) {
            case CONNECTION_LOST, START_SCREEN -> "Connect to a game server";
            case END_GAME -> "This is the end";
            case EXIT -> "";
            case GAME_CREATION -> "Create a new game";
            case GAME_LOGIN -> "Login";
            case GAME_RUNNING -> "Do your best! Good luck!";
            case GAME_WAITING_ROOM -> "Waiting for the other players";
            case JOIN_GAME -> "Join a game";
            case MAIN_MENU -> "Menu";
        } + " | " + defaultTitle);
    }

    /**
     * Prints an error on screen.
     *
     * @param message The message to show.
     */
    @Override
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.show();
    }

    /**
     * Prints an info on screen.
     *
     * @param info The info to show.
     */
    @Override
    public void showInfo(Pair<String, String> info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(info.key());
        alert.setHeaderText(info.value());
        alert.show();
    }

    /**
     * Gets the controller.
     *
     * @return The controller.
     */
    public ClientController getController() {
        return controller;
    }

    /**
     * Changes the current scene.
     *
     * @param def Boolean parameter used by CLI.
     */
    public void updateScreen(boolean def) {
        changeScene();
    }
}
