package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.utilities.GameControllerStates;
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
        String island;
        if(controller.getGameModel().getSubphase().equals(GameControllerStates.MOVE_MOTHER_NATURE)) {
            flushCommand();
            command = "move mother-nature to ";
        }

        switch (islandId) {
            case 0 -> island = "isl01";
            case 1 -> island = "isl02";
            case 2 -> island = "isl03";
            case 3 -> island = "isl04";
            case 4 -> island = "isl05";
            case 5 -> island = "isl06";
            case 6 -> island = "isl07";
            case 7 -> island = "isl08";
            case 8 -> island = "isl09";
            case 9 -> island = "isl10";
            case 10 -> island = "isl11";
            case 11 -> island = "isl12";
            default -> island = "";
        }
        command = command.concat(island);
        sendCommand();
        flushCommand();
    }

    public void manageCloudSelection(int cloudId) {
        Log.debug("Cloud selected with id " + cloudId);
        if(!command.equals("")) flushCommand();
        String cloud;

        switch (cloudId) {
            case 0 -> cloud = "cl1";
            case 1 -> cloud = "cl2";
            case 2 -> cloud = "cl3";
            case 3 -> cloud = "cl4";
            default -> cloud = "";
        }
        command = "refill entrance from ";
        command = command.concat(cloud);
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
        if(!command.equals("")) flushCommand();
        command = "move student ";
        command = command.concat(color.toString().toLowerCase(Locale.ROOT));
        command = command.concat(" from entrance to ");
    }

    public void manageAssistantSelection(int assistantId) {
        if(!command.equals("")) flushCommand();
        Log.debug("Selected assistant with id " + assistantId);
        String assistant;
        command = "play ";

        switch (assistantId) {
            case 1 -> assistant = "ast01";
            case 2 -> assistant = "ast02";
            case 3 -> assistant = "ast03";
            case 4 -> assistant = "ast04";
            case 5 -> assistant = "ast05";
            case 6 -> assistant = "ast06";
            case 7 -> assistant = "ast07";
            case 8 -> assistant = "ast08";
            case 9 -> assistant = "ast09";
            case 10 -> assistant = "ast10";
            default -> assistant = "";
        }
        command = command.concat(assistant);
        sendCommand();
        flushCommand();
    }

    private void sendCommand() {
        Log.debug("Sending command " + command);
        this.controller.manageGameRunning(command);
    }
}
