package it.polimi.ingsw.utilities.parsers;

import com.google.gson.JsonObject;
import it.polimi.ingsw.client.model.Player;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.MessageCreator;
import it.polimi.ingsw.utilities.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

// play assistant ast00
// refill entrance from cloud IdCloud
// pay special character idCharcter

/**
 * Method to manage the command.
 */
public class CommandParser {
    private CommandParser() {
    }

    /*
    info ast00                                                  OK
    info chr00                                                  OK

    play ast00                                                  OK
    move student red from fromPlace to toPlace                  OK
    move mother-nature to isl00                                 OK
    refill entrance from cl00                                   OK

    pay chr00                                                   OK
    move student red from chr00 to isl00                        OK
    resolve isl00                                               OK
    ban isl00                                                   OK
    ignore red                                                  OK
    swap entrance-student red with chr00-student red (6)        OK
    swap chr00-student red with entrance-student  red (6)       OK
    swap entrance-student red with dining-room-student red (6)  OK
    swap dining-room-student red with entrance-student red (6)  OK
    take chr00-student red (to dining-room)                     OK
    !! return students red !!                                   OK

    toPlace: dining-room, isl00
    fromPlace: entrance
     */
    public static boolean checker(String command) {
        String[] parsedCommmand = command.split(" ");
        switch (parsedCommmand.length) {
            case 2 -> {
                if (Pattern.matches("info|play|ban|resolve\sisl\\d\\d", command)) return true;
                else if (Pattern.matches("info|pay\schr\\d\\d", command)) return true;
                else return Pattern.matches("ignored\sblue|fuchsia|green|red|yellow", command);
            }
            case 3 -> {
                if (Pattern.matches("take\schr-\\d\\d-students\sblue|fuchsia|green|red|yellow", command)) return true;
                else return Pattern.matches("return\sstudents\sblue|fuchsia|green|red|yellow", command);
            }
            case 4 -> {
                if (Pattern.matches("move\smother-nature\sto\sisl\\d\\d", command)) return true;
                else return Pattern.matches("refill\sentrance\sfrom\scl\\d\\d", command);
            }
            case 6 -> {
                if (Pattern.matches("swap\sentrance-student\sblue|fuchsia|green|red|yellow\swith\sdining-room-student|chr\\d\\d-student\sblue|fuchsia|green|red|yellow", command))
                    return true;
                else
                    return Pattern.matches("swap\sdining-room-student|chr\\d\\d-student\sblue|fuchsia|green|red|yellow\swith\sentrance-student\sblue|fuchsia|green|red|yellow", command);
            }
            default -> {
                return Pattern.matches("move\sstudent\sblue|fuchsia|green|red|yellow\sfrom\sentrance|chr\\d\\d\sto\sdining-room|isl\\d\\d", command);
            }
        }
    }

    public static Pair<String, String> infoGenerator(String command) {
        String[] parsedCommand;
        int id;

        parsedCommand = command.split(" ");
        id = Integer.parseInt(parsedCommand[1].replaceAll("^[a-z]+", ""));

        if (parsedCommand[1].startsWith("ast"))
            return new Pair<>(String.format("Assistant #%d", id), String.format("allows to do %d steps.", id / 2 + 1));

        return new Pair<>(String.format("Special character #%d", id), switch (id) {
            case 1 -> "take 1 Student from this card and place it on an Island of your choice.";
            case 2 ->
                    "take control of any number of Professors, even if you have the same number of Students as the the player who controls them.";
            case 3 -> "choose an Island and resolve it as if Mother Nature had ended her movement there.";
            case 4 ->
                    "you may move Mother Nature up to 2 additional islands than is indicated on your played Assistant.";
            case 5 ->
                    "place a Ban on an Island of your choice. The first time that Mother Nature ends her movements there the Island won't be resolved.";
            case 6 -> "when resolving a Conquering on an Island, Tower do not count toward influence.";
            case 7 ->
                    "you may take up to 3 Students from this card and replace them with the same number of Students from your Entrance.";
            case 8 -> "during the influence calculation this turn, you count as having 2 more influence.";
            case 9 ->
                    "choose a color of Student: during the influence calculation this turn, that color adds no influence.";
            case 10 -> "you may exchange up to 2 Students between your Entrance and your Dining Room.";
            case 11 -> "Take 1 Student from this card and place it in your Dining Room.";
            default ->
                    "Choose a color of Student: every player (you too) must return 3 Students of that color from their Dining Room to the Bag.";
        });
    }

    /*
    swap entrance-student red with chr00-student red (6)        OK
    swap chr00-student red with entrance-student  red (6)       OK
    swap entrance-student red with dining-room-student red (6)  OK
    swap dining-room-student red with entrance-student red (6)  OK
    take chr00-student red (to dining-room)                     OK
    !! return students red !!                                   OK

    toPlace: dining-room, isl00
    fromPlace: entrance
    */
    public static List<JsonObject> commandManager(String command, List<Player> players) {
        String playerName = null;

        if (command == null || command.isBlank())
            return new ArrayList<>();

        String[] parsedCommand = command.split(" ");

        for (Player p : players)
            if (p.isActive())
                playerName = p.getName();

        return switch (parsedCommand[0]) {
            case "play" ->
                    List.of(MessageCreator.playAssistant(playerName, Integer.parseInt(parsedCommand[1].replaceAll("\\D", ""))));
            case "move" -> List.of(manageMove(command, players));
            case "refill" ->
                    List.of(MessageCreator.refillEntrance(playerName, Integer.parseInt(parsedCommand[3].replaceAll("\\D", "")) - 1));
            case "pay" ->
                    List.of(MessageCreator.payCharacter(playerName, Integer.parseInt(parsedCommand[1].replaceAll("\\D", ""))));
            case "resolve" ->
                    List.of(MessageCreator.moveMotherNature(Integer.parseInt(parsedCommand[3].replaceAll("\\D", "")), false));
            case "ignore" -> List.of(MessageCreator.ignoreColor(parsedCommand[1]));
            case "swap" -> manageSwap();
            case "ban" -> List.of(MessageCreator.ban(Integer.parseInt(parsedCommand[1].replaceAll("\\D", ""))));
            default -> List.of();
        };
    }

    /**
     * Helper for commandManager.
     *
     * @param command String that contains the command to execute.
     * @param players List of players.
     * @return JsonObject which represents the message.
     */
    private static JsonObject manageMove(String command, List<Player> players) {
        String playerName = null;
        String[] parsedCommand = command.split(" ");

        for (Player p : players) if (p.isActive()) playerName = p.getName();
        // 0    1       2   3    4       5  6
        // move student red from idPlace to idPlace

        // 0    1       2   3    4     5  6
        // move student red from place to idPlace

        // 0    1       2   3    4       5  6
        // move student red from idPlace to place

        // 0    1       2   3    4     5  6
        // move student red from place to place
        if (parsedCommand[1].equals("student")) {
            // Move Student
            String color;
            String tmp;
            String from;
            String fromId = null;
            String to;
            String toId = null;

            color = parsedCommand[2];
            tmp = parsedCommand[4];
            if (tmp.equals("entrance"))
                from = parsedCommand[4];
            else {
                from = "character";
                fromId = parsedCommand[4].replaceAll("\\D", "");
            }

            tmp = parsedCommand[6];
            if (tmp.equals("dining-room"))
                to = parsedCommand[6];
            else {
                to = "island";
                toId = parsedCommand[6].replaceAll("\\D", "");
            }

            return MessageCreator.moveStudent(
                    playerName,
                    HouseColor.valueOf(color.toUpperCase(Locale.ROOT)),
                    from,
                    to,
                    fromId == null ? null : Integer.parseInt(fromId) - 1,
                    toId == null ? null : Integer.parseInt(toId) - 1);
        } else {
            //Move Mother Nature
            return MessageCreator.moveMotherNature(Integer.parseInt(parsedCommand[3].replaceAll("\\D", "")) - 1, true);
        }
    }

    private static List<JsonObject> manageSwap() {
        return List.of();
    }
}
