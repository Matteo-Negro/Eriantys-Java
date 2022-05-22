package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.gui.GameCreation;
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
        stage.setScene(switch (state) {
            case CONNECTION_LOST -> null;
            case END_GAME -> null;
            case EXIT -> null;
            case GAME_CREATION -> GameCreation.getScene();
            case GAME_LOGIN -> null;
            case GAME_RUNNING -> null;
            case GAME_WAITING_ROOM -> null;
            case JOIN_GAME -> null;
            case MAIN_MENU -> MainMenu.getScene();
            case START_SCREEN -> StartScreen.getScene();
        });
    }
}
