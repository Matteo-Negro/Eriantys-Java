package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.gui.Start;
import it.polimi.ingsw.utilities.ClientStates;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.Pair;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class ClientGui extends Application implements View {

    private Stage stage;
    private ClientController controller;
    private Map<ClientStates, Scene> scenes;
    private static final Object lock = new Object();
    private static ClientGui instance = null;
    private static final String DEFAULT_TITLE = "Eriantys";

    /**
     * @param primaryStage the primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {

        synchronized (lock) {
            instance = this;
        }

        stage = primaryStage;
        controller = new ClientController(this);
        scenes = new EnumMap<>(ClientStates.class);

        Log.info("Initializing scenes...");
        try {
            scenes.put(ClientStates.START_SCREEN, loadScene("splash_screen"));
        } catch (IOException e) {
            Log.error("Cannot initialize scenes because of the following error: ", e);
            return;
        }

        changeScene();

        primaryStage.show();

        new Thread(() -> {
            try {
                scenes.put(ClientStates.GAME_CREATION, loadScene("game_creation"));
                scenes.put(ClientStates.END_GAME, loadScene("end"));
                scenes.put(ClientStates.GAME_RUNNING, loadScene("game"));
                scenes.put(ClientStates.JOIN_GAME, loadScene("join"));
                scenes.put(ClientStates.GAME_LOGIN, loadScene("login"));
                scenes.put(ClientStates.MAIN_MENU, loadScene("menu"));
                scenes.put(ClientStates.GAME_WAITING_ROOM, loadScene("waiting_room"));
                Log.info("Initialization completed.");
            } catch (IOException e) {
                Log.error("Cannot initialize scenes because of the following error: ", e);
            }
        }).start();
    }

    /**
     * Gets the scene from the name of the FXML file to load.
     *
     * @param scene Name of the FXML file.
     * @return The generated scene.
     * @throws IOException If an error occurs during loading.
     */
    public static Scene loadScene(String scene) throws IOException {
        return new Scene(
                FXMLLoader.load(
                        Objects.requireNonNull(
                                Start.class.getResource(
                                        String.format("/fxml/%s.fxml", scene)))));
    }

    /**
     * Changes the scene according to the state.
     */
    public void changeScene() {
        ClientStates state = getController().getClientState();
        if (getController().getClientState().equals(ClientStates.CONNECTION_LOST))
            getController().manageConnectionLost();

        Log.info("Displaying " + state);
        stage.setScene(scenes.get(state));

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
        } + " | " + DEFAULT_TITLE);
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
     * Gets the instance of the currently running ClientGUI for interaction between GUI and backend.
     *
     * @return The instance of the currently running ClientGUI
     */
    public static ClientGui getInstance() {
        synchronized (lock) {
            return instance;
        }
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
     * Changes the current scene.
     *
     * @param def Boolean parameter used by CLI.
     */
    @Override
    public void updateScreen(boolean def) {
        changeScene();
    }
}
