package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Log;

import java.util.Locale;

/**
 * Assembles the commands to send to the controller from inputs received from the GUI.
 */
public class CommandAssembler {
    private final ClientController controller;
    private String command;

    /**
     * The main constructor.
     *
     * @param controller The backend controller to which send the commands.
     */
    public CommandAssembler(ClientController controller) {
        this.controller = controller;
        flushCommand();
    }

    /**
     * Resets the command.
     */
    private void flushCommand() {
        this.command = "";
    }

    /**
     * Creates the command or completes it when the user clicks on an island.
     *
     * @param islandId The id of the island the user clicked.
     */
    public void manageIslandSelection(int islandId) {
        Log.debug("Island selected with id " + islandId);
        String island = islandId >= 0 && islandId <= 11 ? String.format("isl%02d", islandId + 1) : "";

        if (command.contains("from chr01 to") || command.equals("ban "))
            command = String.format("%s%s", command, island);
        else if (command.equals("") && this.controller.getGameModel().getGameBoard().getSpecialCharacters().stream().anyMatch(sp ->
                sp.getId() == 3 && sp.isActive() && sp.getUsesNumber() > 0)) {
            command = String.format("resolve %s", island);
        } else {
            switch (controller.getGameModel().getSubPhase()) {
                case MOVE_MOTHER_NATURE -> {
                    flushCommand();
                    command = String.format("move mother-nature to %s", island);
                }
                case MOVE_STUDENT_1, MOVE_STUDENT_2, MOVE_STUDENT_3, MOVE_STUDENT_4 ->
                        command = String.format("%s%s", command, island);
            }
        }

        sendCommand();
        flushCommand();
    }

    /**
     * Creates the command when the user clicks on a cloud.
     *
     * @param cloudId The id of the cloud the user clicked.
     */
    public void manageCloudSelection(int cloudId) {
        Log.debug("Cloud selected with id " + cloudId);
        if (!command.equals(""))
            flushCommand();
        command = String.format("refill entrance from %s", cloudId >= 0 && cloudId <= 3 ? String.format("cl%d", cloudId + 1) : "");
        sendCommand();
        flushCommand();
    }

    /**
     * Completes the command when the user clicks on the dining room.
     */
    public void manageDiningRoomSelection() {
        Log.debug("Dining-room selected");
        command = command.concat("dining-room");
        sendCommand();
        flushCommand();
    }

    /**
     * Creates the command or completes it when the user clicks on an entrance student.
     *
     * @param color The color of the student the user clicked.
     */
    public void manageEntranceSelection(HouseColor color) {
        Log.debug("Selected student from entrance of color " + color.toString());
        if (command.contains("swap chr07-student")) {
            command = String.format("%s%s", command, color.name().toLowerCase(Locale.ROOT));
            sendCommand();
            flushCommand();
        } else {
            if (!command.equals(""))
                flushCommand();
            command = String.format("move student %s from entrance to ", color.name().toLowerCase(Locale.ROOT));
        }
    }

    /**
     * Creates the command when the user clicks on an assistant.
     *
     * @param assistantId The id of the assistant the user clicked.
     */
    public void manageAssistantSelection(int assistantId) {
        if (!command.equals(""))
            flushCommand();
        Log.debug("Selected assistant with id " + assistantId);
        command = String.format("play %s", assistantId >= 1 && assistantId <= 10 ? String.format("ast%02d", assistantId) : "");
        sendCommand();
        flushCommand();
    }

    /**
     * Creates the command when the user clicks on a special character.
     *
     * @param specialCharacterId The id of the special character the user clicked.
     */
    public void managePaymentsSpecialCharacterSelection(int specialCharacterId) {
        Log.debug("Selected character with id " + specialCharacterId);
        if (!command.equals(""))
            flushCommand();
        command = String.format("pay %s", specialCharacterId >= 1 && specialCharacterId <= 12 ? String.format("chr%02d", specialCharacterId) : "");
        sendCommand();
        flushCommand();
    }

    /**
     * Creates the command when the user clicks on a special character.
     *
     * @param color The color of the student the user clicked.
     */
    public void manageStudentSCFromCardToIslandSelection(HouseColor color) {
        Log.debug("Selected student from special character of color " + color.toString());
        if (!command.equals(""))
            flushCommand();
        command = String.format("move student %s from chr01 to ", color.name().toLowerCase(Locale.ROOT));
    }

    /**
     * Creates the command when the user clicks on a special character.
     *
     * @param color The color the of the student user clicked.
     */
    public void manageStudentSCSwapCardEntranceSelection(HouseColor color) {
        Log.debug("Selected student from special character of color " + color.toString());
        if (!command.equals(""))
            flushCommand();
        command = String.format("swap chr07-student %s with entrance-student ", color.name().toLowerCase(Locale.ROOT));
    }

    /**
     * Creates the command when the user clicks on a special character.
     *
     * @param color        The color of the student the user clicked.
     * @param studentIndex The index of the student the user clicked.
     */
    public void manageStudentSCSwapCardEntranceDiningRoomSelection(HouseColor color, int studentIndex) {
        Log.debug("Selected student from special character of color " + color.toString());
        if (command.equals("") && studentIndex < 5) {
            command = String.format("swap entrance-student %s with dining-room-student ", color.name().toLowerCase(Locale.ROOT));
        } else if (command.contains("swap entrance-student") && studentIndex >= 5) {
            command = String.format("%s%s", command, color.name().toLowerCase(Locale.ROOT));
            sendCommand();
            flushCommand();
        } else
            flushCommand();
    }

    /**
     * Creates the command when the user clicks on a special character.
     *
     * @param color The color of the student the user clicked.
     */
    public void manageStudentSCIgnoreColorSelection(HouseColor color) {
        Log.debug("Selected student from special character of color " + color.toString());
        if (!command.equals(""))
            flushCommand();
        command = String.format("ignore %s", color.name().toLowerCase(Locale.ROOT));
        sendCommand();
        flushCommand();
    }

    /**
     * Creates the command when the user clicks on a special character.
     *
     * @param color The color of the student the user clicked.
     */
    public void manageStudentSCFromCardToDiningRoomSelection(HouseColor color) {
        Log.debug("Selected student from special character of color " + color.toString());
        if (!command.equals(""))
            flushCommand();
        command = String.format("take chr11-student %s", color.name().toLowerCase(Locale.ROOT));
        sendCommand();
        flushCommand();
    }

    /**
     * Creates the command when the user clicks on a special character.
     *
     * @param color The color of the student the user clicked.
     */
    public void manageStudentSCReturnColorSelection(HouseColor color) {
        Log.debug("Selected student from special character of color " + color.toString());
        if (!command.equals(""))
            flushCommand();
        command = String.format("return students %s", color.name().toLowerCase(Locale.ROOT));
        sendCommand();
        flushCommand();
    }

    /**
     * Creates the command when the user clicks on a special character.
     */
    public void manageStudentSCBanSelection() {
        if (!command.equals(""))
            flushCommand();
        command = "ban ";
    }

    /**
     * Sends the command to the backend in order to process it and send it to the server.
     */
    private void sendCommand() {
        Log.debug("Sending command " + command);
        this.controller.manageGameRunning(command);
    }
}
