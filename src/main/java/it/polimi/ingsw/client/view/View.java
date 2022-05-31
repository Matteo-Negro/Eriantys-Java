package it.polimi.ingsw.client.view;

import com.google.gson.JsonObject;
import it.polimi.ingsw.utilities.Pair;
import it.polimi.ingsw.utilities.exceptions.ExitException;

import java.io.IOException;
import java.net.Socket;

public interface View {

    Socket runStartScreen() throws IOException;

    String runMainMenu();

    JsonObject runGameCreation() throws ExitException, IllegalArgumentException;

    String runJoinGame();

    String runGameLogin();

    void runWaitingRoom();

    String runGameRunning();

    String runEndGame();

    void updateScreen(boolean def);

    void printError(String message);

    void printInfo(Pair<String, String> info);

}
