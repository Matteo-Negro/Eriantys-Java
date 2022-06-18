package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Log;

import java.util.Locale;

public class CommandAssembler {
    private final ClientController controller;
    private String command;

    public CommandAssembler(ClientController controller) {
        this.controller = controller;
        flushCommand();
    }

    private void flushCommand() {
        this.command = "";
    }

    public void manageIslandSelection(int islandId) {
        Log.debug("Island selected with id " + islandId);
        String island = islandId >= 0 && islandId <= 11 ? String.format("isl%02d", islandId + 1) : "";
        switch (controller.getGameModel().getSubphase()) {
            case MOVE_MOTHER_NATURE -> {
                flushCommand();
                command = String.format("move mother-nature to %s", island);
            }
            case MOVE_STUDENT_1, MOVE_STUDENT_2, MOVE_STUDENT_3, MOVE_STUDENT_4 ->
                    command = String.format("%s%s", command, island);
        }
        sendCommand();
        flushCommand();
    }

    public void manageCloudSelection(int cloudId) {
        Log.debug("Cloud selected with id " + cloudId);
        if (!command.equals(""))
            flushCommand();
        command = String.format("refill entrance from %s", cloudId >= 0 && cloudId <= 3 ? String.format("cl%d", cloudId + 1) : "");
        sendCommand();
        flushCommand();
    }

    public void manageDiningRoomSelection() {
        Log.debug("Dining-room selected");
        command = command.concat("dining-room");
        sendCommand();
        flushCommand();
    }

    public void manageEntranceSelection(HouseColor color) {
        Log.debug("Selected student from entrance of color " + color.toString());
        if (!command.equals(""))
            flushCommand();
        command = String.format("move student %s from entrance to ", color.name().toLowerCase(Locale.ROOT));
    }

    public void manageAssistantSelection(int assistantId) {
        if (!command.equals(""))
            flushCommand();
        Log.debug("Selected assistant with id " + assistantId);
        command = String.format("play %s", assistantId >= 1 && assistantId <= 10 ? String.format("ast%02d", assistantId) : "");
        sendCommand();
        flushCommand();
    }

    private void sendCommand() {
        Log.debug("Sending command " + command);
        this.controller.manageGameRunning(command);
    }
}
