package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.view.gui.GameCreation;
import it.polimi.ingsw.client.view.gui.JoinGame;
import it.polimi.ingsw.client.view.gui.MainMenu;
import it.polimi.ingsw.client.view.gui.StartScreen;
import it.polimi.ingsw.utilities.ClientStates;
import it.polimi.ingsw.utilities.Log;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientGui extends Application {

    private Stage stage;

    /**
     * @param primaryStage the primary stage for this application, onto which the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be primary stages.
     */
    @Override
    public void start(Stage primaryStage) {

        stage = primaryStage;

        Log.info("Initializing scenes");
        try {
            StartScreen.initialize(this);
            MainMenu.initialize(this);
            GameCreation.initialize(this);
            JoinGame.initialize(this);
        } catch (IOException e) {
            Log.error("Cannot initialize scenes: ", e);
            return;
        }
        Log.info("Initialization completed");

        primaryStage.sizeToScene();
        primaryStage.setResizable(false);

        changeScene(ClientStates.START_SCREEN);

        primaryStage.show();
    }

    /**
     * Changes the scene according to the state.
     *
     * @param state State of the game.
     */
    public void changeScene(ClientStates state) {
        Log.info("Displaying " + state);
        final String defaultTitle = "Eriantys";
        stage.setScene(switch (state) {
            case CONNECTION_LOST -> null;
            case END_GAME -> null;
            case EXIT -> null;
            case GAME_CREATION -> GameCreation.getScene();
            case GAME_LOGIN -> null;
            case GAME_RUNNING -> null;
            case GAME_WAITING_ROOM -> null;
            case JOIN_GAME -> JoinGame.getScene();
            case MAIN_MENU -> MainMenu.getScene();
            case START_SCREEN -> StartScreen.getScene();
        });
        stage.setTitle(switch (state) {
            case CONNECTION_LOST -> defaultTitle;
            case END_GAME -> defaultTitle;
            case EXIT -> defaultTitle;
            case GAME_CREATION -> defaultTitle + " | Create a new game";
            case GAME_LOGIN -> defaultTitle + " | Game code";
            case GAME_RUNNING -> defaultTitle;
            case GAME_WAITING_ROOM -> defaultTitle;
            case JOIN_GAME -> defaultTitle;
            case MAIN_MENU -> defaultTitle + " | Menu";
            case START_SCREEN -> defaultTitle;
        });
    }
}
