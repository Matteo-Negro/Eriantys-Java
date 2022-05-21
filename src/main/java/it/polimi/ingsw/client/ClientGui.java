package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.gui.MainMenu;
import it.polimi.ingsw.client.view.gui.StartScreen;
import it.polimi.ingsw.utilities.ClientStates;
import it.polimi.ingsw.utilities.Log;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class ClientGui extends Application {

    private Stage stage;

    /**
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     */
    @Override
    public void start(Stage primaryStage) {

        stage = primaryStage;

        Log.info("Initializing scenes");
        try {
            StartScreen.initialize(this);
            MainMenu.initialize(this);
        } catch (IOException e) {
            Log.error("Cannot initialize scenes: ", e);
            return;
        }
        Log.info("Initialization completed");

        primaryStage.setTitle("Eriantys");
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);

        changeScene(ClientStates.START_SCREEN);

        primaryStage.show();
    }

    public void changeScene(ClientStates state) {
        Log.info("Displaying " + state);
        switch (state) {
            case CONNECTION_LOST -> {
            }
            case END_GAME -> {
            }
            case EXIT -> {
            }
            case GAME_CREATION -> {
            }
            case GAME_LOGIN -> {
            }
            case GAME_RUNNING -> {
            }
            case GAME_WAITING_ROOM -> {
            }
            case JOIN_GAME -> {
            }
            case MAIN_MENU -> {
                stage.setScene(MainMenu.getScene());
                MainMenu.addEvents();
            }
            case START_SCREEN -> {
                stage.setScene(StartScreen.getScene());
                StartScreen.addEvents();
            }
        }
    }
}
