package it.polimi.ingsw.client.view;

import com.google.gson.JsonObject;
import it.polimi.ingsw.utilities.Pair;
import it.polimi.ingsw.utilities.exceptions.ExitException;

import java.io.IOException;
import java.net.Socket;

public interface View {

    void runStartScreen() throws IOException;

    void runMainMenu();

    void runGameCreation();

    void runJoinGame();

    void runGameLogin();

    void runWaitingRoom();

    void runGameRunning();

    void runEndGame();

    void updateScreen(boolean def);

    void printError(String message);

    void printInfo(Pair<String, String> info);

}
