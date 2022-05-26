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
        if (parsedCommand[1].startsWith("AST"))
            result = "The Assistant #" + id + " allow you to do " + (id / 2 + 1) + "steps";
        else {
            switch (id) {
                case 1 -> result = "SpecialCharacter #" + id + "Take 1 Student from this card and place it on an Island of your choice.";
                case 2 -> result = "SpecialCharacter #" + id + "Take control of any number of Professors, even if you have the same number of Students as the the player who controls them.";
                case 3 -> result = "SpecialCharacter #" + id + "Choose an Island and resolve it as if Mother Nature had ended her movement there.";
                case 4 -> result = "SpecialCharacter #" + id + "You may move Mother Nature up to 2 additional islands than is indicated on your played Assistant.";
                case 5 -> result = "SpecialCharacter #" + id + "Place a Ban on an Island of your choice. The first time that Mother Nature ends her movements there the Island won't be resolved.";
                case 6 -> result = "SpecialCharacter #" + id + "When resolving a Conquering on an Island, Tower do not count toward influence.";
                case 7 -> result = "SpecialCharacter #" + id + "You may take up to 3 Students from this card and replace them with the same number of Students from your Entrance.";
                case 8 -> result = "SpecialCharacter #" + id + "During the influence calculation this turn, you count as having 2 more influence.";
                case 9 -> result = "SpecialCharacter #" + id + "Choose a color of Student: during the influence calculation this turn, that color adds no influence.";
                case 10 -> result = "SpecialCharacter #" + id + "You may exchange up to 2 Students between your Entrance and your Dining Room.";
                case 11 -> result = "SpecialCharacter #" + id + "Take 1 Student from this card and place it in your Dining Room.";
                default -> result = "SpecialCharacter #" + id + "Choose a color of Student: every player (you too) must return 3 Students of that color from their Dining Room to the Bag.";
            }
        }
    }

    public static JsonObject commandManager(String command, String playerName) {
        JsonObject jsonCommand;
        String[] parsedCommand = command.split(" ");

        switch (parsedCommand[0]) {
            case "play" ->
                    jsonCommand = MessageCreator.playAssistant(playerName, Integer.parseInt(parsedCommand[1].replaceAll("\\D", "")));
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
            if (tmp.equals("dining-room") || tmp.equals("entrance")) from = parsedCommand[4];
            else fromId = parsedCommand[4].replaceAll("\\D", "");

            tmp = parsedCommand[6];
            if (tmp.equals("dining-room") || tmp.equals("entrance")) to = parsedCommand[6];
            else toId = parsedCommand[6].replaceAll("\\D", "");

            return MessageCreator.moveStudent(playerName, HouseColor.valueOf(color), from, to, Integer.parseInt(fromId), Integer.parseInt(toId));
        } else {
            //Move Mother Nature
            return MessageCreator.moveMotherNature(Integer.parseInt(parsedCommand[4].replaceAll("\\D", "")));
        }
    }
}
