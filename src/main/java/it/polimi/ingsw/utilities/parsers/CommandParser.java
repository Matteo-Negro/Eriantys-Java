package it.polimi.ingsw.utilities.parsers;

import com.google.gson.JsonObject;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.MessageCreator;

// play assistant ast00
// refill entrance from cloud IdCloud
// pay special character idCharcter

/**
 * Method to manage the command.
 */
public class CommandParser {
    private CommandParser() {
    }

    public static JsonObject commandManager(String command, String playerName) {
        JsonObject jsonCommand;
        String[] parsedCommand = command.split(" ");

        switch (parsedCommand[0]) {
            case "play" -> jsonCommand = MessageCreator.playAssistant(playerName, Integer.parseInt(parsedCommand[2]));
            case "move" -> jsonCommand = manageMove(command, playerName);
            case "refill" -> jsonCommand = MessageCreator.refillEntrance(playerName, Integer.parseInt(parsedCommand[parsedCommand.length - 1]));
            default -> jsonCommand = MessageCreator.payCharacter(playerName, Integer.parseInt(parsedCommand[parsedCommand.length - 1]));

        }
        return jsonCommand;
    }

    /**
     * Helper for commandManager.
     *
     * @param command    String that contains the command to execute.
     * @param playerName Name of the player that executes the command.
     * @return JsonObject which represents the message.
     */
    private static JsonObject manageMove(String command, String playerName) {
        String[] parsedCommand = command.split(" ");
        // 0    1       2   3    4     5       6  7     8
        // move student red from place idPlace to place idPlace

        // 0    1       2   3    4     5  6     7
        // move student red from place to place idPlace

        // 0    1       2   3    4     5       6  7
        // move student red from place idPlace to place

        // 0    1       2   3    4     5  6
        // move student red from place to place
        if (parsedCommand[1].equals("student")) {
            // Move Student
            String color;
            String from;
            String to;
            String fromId = null;
            String toId = null;

            color = parsedCommand[2];
            from = parsedCommand[4];
            if (from.equals("dining-room")) {
                to = parsedCommand[6];
                if (!to.equals("entrance")) toId = parsedCommand[7];
            } else {
                fromId = parsedCommand[5];
                to = parsedCommand[7];
                if (!to.equals("entrance")) toId = parsedCommand[8];
            }

            return MessageCreator.moveStudent(playerName, HouseColor.valueOf(color), from, to, Integer.parseInt(fromId), Integer.parseInt(toId));
        } else {
            //Move Mother Nature
            return MessageCreator.moveMotherNature(Integer.parseInt(parsedCommand[parsedCommand.length - 1]));
        }
    }
}
