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

    /**
     * @param primaryStage the primary stage for this application, onto which the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be primary stages.
     */
    @Override
    public void start(Stage primaryStage) {

        stage = primaryStage;
        controller = new ClientController(this);

        Log.info("Initializing scenes...");
        try {
            StartScreen.initialize(this);
            MainMenu.initialize(this);
            GameCreation.initialize(this);
            JoinGame.initialize(this);
            Login.initialize(this);
            Log.info("Initialization completed.");
        } catch (IOException e) {
            Log.error("Cannot initialize scenes because of the following error: ", e);
            return;
        }

        primaryStage.sizeToScene();
        primaryStage.setResizable(false);

        changeScene(ClientStates.START_SCREEN);

        primaryStage.show();
    }

    /**
     * Changes the scene according to the state.
     */
    public void changeScene() {
        changeScene(getController().getClientState());
    }

    /**
     * Changes the scene according to the state.
     *
     * @param state State of the game.
     */
    public void changeScene(ClientStates state) {

        if (!getController().getClientState().equals(ClientStates.CONNECTION_LOST))
            getController().setClientState(state);
        else
            getController().manageConnectionLost();

        Log.info("Displaying " + state);
        final String defaultTitle = "Eriantys";

        stage.setScene(switch (state) {
            case CONNECTION_LOST, START_SCREEN -> StartScreen.getScene();
            case END_GAME -> null;
            case EXIT -> null;
            case GAME_CREATION -> GameCreation.getScene();
            case GAME_LOGIN -> Login.getScene();
            case GAME_RUNNING -> null;
            case GAME_WAITING_ROOM -> null;
            case JOIN_GAME -> JoinGame.getScene();
            case MAIN_MENU -> MainMenu.getScene();
        });

        stage.setTitle(switch (state) {
            case CONNECTION_LOST -> defaultTitle;
            case END_GAME -> defaultTitle;
            case EXIT -> defaultTitle;
            case GAME_CREATION -> defaultTitle + " | Create a new game";
            case GAME_LOGIN -> defaultTitle + " | Login";
            case GAME_RUNNING -> defaultTitle;
            case GAME_WAITING_ROOM -> defaultTitle;
            case JOIN_GAME -> defaultTitle + " | Join a game";
            case MAIN_MENU -> defaultTitle + " | Menu";
            case START_SCREEN -> defaultTitle;
        });
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
}
