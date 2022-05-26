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

    public static void infoGenerator(String command) {
        String[] parsedCommand;
        String result;
        int id;

        parsedCommand = command.split(" ");
        id = Integer.parseInt(parsedCommand[1].replaceAll("\\D", ""));
        // TODO: decide signature
        if (parsedCommand[1].startsWith("AST")) result = "The Assistant #" + id +
                " allow you to do " + (id / 2 + 1) + "steps";
        else{
            switch (id) {
                case 1 -> result = "SpecialCharacter #" + id;
                case 2 -> result = "SpecialCharacter #" + id;
                case 3 -> result = "SpecialCharacter #" + id;
                case 4 -> result = "SpecialCharacter #" + id;
                case 5 -> result = "SpecialCharacter #" + id;
                case 6 -> result = "SpecialCharacter #" + id;
                case 7 -> result = "SpecialCharacter #" + id;
                case 8 -> result = "SpecialCharacter #" + id;
                case 9 -> result = "SpecialCharacter #" + id;
                case 10 -> result = "SpecialCharacter #" + id;
                case 11 -> result = "SpecialCharacter #" + id;
                default -> result = "SpecialCharacter #" + id;
            }
        }
    }

    public static JsonObject commandManager(String command, String playerName) {
        JsonObject jsonCommand;
        String[] parsedCommand = command.split(" ");

        switch (parsedCommand[0]) {
            case "play" -> jsonCommand = MessageCreator.playAssistant(playerName, Integer.parseInt(parsedCommand[1].replaceAll("\\D", "")));
            case "move" -> jsonCommand = manageMove(command, playerName);
            case "refill" ->
                    jsonCommand = MessageCreator.refillEntrance(playerName, Integer.parseInt(parsedCommand[1].replaceAll("\\D", "")));
            default ->
                    jsonCommand = MessageCreator.payCharacter(playerName, Integer.parseInt(parsedCommand[1].replaceAll("\\D", "")));

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
        // 0    1       2   3    4       5  6
        // move student red from idPlace to idPlace

        // 0    1       2   3    4     5  6
        // move student red from place to idPlace

        // 0    1       2   3    4       5  6
        // move student red from idPlace to place

        // 0    1       2   3    4     5  6
        // move student red from place to place
        String[] parsedCommand = command.split(" ");
        if (parsedCommand[1].equals("student")) {
            // Move Student
            String color;
            String tmp;
            String from = null;
            String fromId = null;
            String to = null;
            String toId = null;

            color = parsedCommand[2];
            tmp = parsedCommand[4];
            if (tmp.equals("dining-room") || tmp.equals("entance")) from = parsedCommand[4];
            else fromId = parsedCommand[4].replaceAll("\\D", "");

            tmp = parsedCommand[6];
            if (tmp.equals("dining-room") || tmp.equals("entance")) to = parsedCommand[6];
            else toId = parsedCommand[6].replaceAll("\\D", "");

            return MessageCreator.moveStudent(playerName, HouseColor.valueOf(color), from, to, Integer.parseInt(fromId), Integer.parseInt(toId));
        } else {
            //Move Mother Nature
            return MessageCreator.moveMotherNature(Integer.parseInt(parsedCommand[6]));
        }
    }
}
