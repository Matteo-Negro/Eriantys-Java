package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utilities.Pair;

import java.io.IOException;

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

    void showError(String message);

    void showInfo(Pair<String, String> info);

}
