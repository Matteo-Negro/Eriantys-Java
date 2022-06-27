package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.model.Assistant;
import it.polimi.ingsw.client.model.GameBoard;
import it.polimi.ingsw.client.model.SpecialCharacter;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Phase;
import org.jline.builtins.Completers.TreeCompleter;
import org.jline.builtins.Completers.TreeCompleter.Node;

import java.util.*;

import static org.jline.builtins.Completers.TreeCompleter.node;

/**
 * During the game phase, this class contains all the methods to get a correct suggestion while playing.
 */
public class Autocompletion {
    private static ClientController controller = null;
    private static List<String> islands = null;
    private static List<String> toList = null;
    private static List<String> assistants = null;
    private static List<String> specialCharacters = null;

    private Autocompletion() {
    }

    /**
     * Initializes the class parameters.
     *
     * @param controller The client controller from where all the data are taken.
     */
    public static void initialize(ClientController controller) {
        Autocompletion.controller = controller;
        initializeIslands();
        initializeToList();
        initializeAssistants();
    }

    /**
     * Initializes the list of islands.
     */
    private static void initializeIslands() {
        if (islands != null)
            return;
        List<String> islands = new ArrayList<>();
        for (int index = 1; index <= 12; index++)
            islands.add(String.format("ISL%02d", index));
        Autocompletion.islands = Collections.unmodifiableList(islands);
    }

    /**
     * Initializes the list of destinations.
     */
    private static void initializeToList() {
        if (toList != null)
            return;
        List<String> toList = new ArrayList<>();
        toList.add("dining-room");
        toList.addAll(islands);
        Autocompletion.toList = Collections.unmodifiableList(toList);
    }

    /**
     * Initializes the general list of assistants.
     */
    private static void initializeAssistants() {
        if (assistants != null)
            return;
        List<String> assistants = new ArrayList<>();
        for (int index = 1; index <= 10; index++)
            assistants.add(String.format("AST%02d", index));
        Autocompletion.assistants = Collections.unmodifiableList(assistants);
    }

    /**
     * Initializes the list of special characters in the game.
     */
    private static void initializeSpecialCharacters() {
        if (!controller.getGameModel().isExpert())
            specialCharacters = List.of();
        List<String> specialCharacters = new ArrayList<>();
        for (SpecialCharacter specialCharacter : controller.getGameModel().getGameBoard().getSpecialCharacters())
            specialCharacters.add(String.format("CHR%02d", specialCharacter.getId()));
        Autocompletion.specialCharacters = Collections.unmodifiableList(specialCharacters);
    }

    /**
     * Returns a list of nodes for suggestions according to the game state.
     *
     * @return List of nodes for suggestions.
     */
    public static List<Node> get() {
        List<TreeCompleter.Node> nodes = new ArrayList<>();

        nodes.add(node("exit"));
        nodes.add(node("logout"));

        nodes.addAll(info());

        if (controller.getGameModel().getPhase().equals(Phase.PLANNING))
            nodes.addAll(playableAssistants());
        else {
            switch (controller.getGameModel().getSubPhase()) {
                case CHOOSE_CLOUD -> nodes.addAll(refillFromClouds());
                case MOVE_MOTHER_NATURE -> nodes.addAll(motherNatureMoves());
                case MOVE_STUDENT_1, MOVE_STUDENT_2, MOVE_STUDENT_3, MOVE_STUDENT_4 ->
                        nodes.addAll(studentsMoves(controller.getGameModel().getPlayerByName(controller.getUserName()).getSchoolBoard().getEntrance()));
            }
            if (controller.getGameModel().isExpert()) {
                if (controller.getGameModel().getGameBoard().getSpecialCharacters().stream().anyMatch(
                        specialCharacter -> specialCharacter.isActive() && specialCharacter.getUsesNumber() != 0))
                    nodes.addAll(specialCharactersActions());
                else
                    nodes.addAll(payableSpecialCharacters());
            }
        }

        return nodes;
    }

    /**
     * Gets all the info commands.
     *
     * @return All the info commands.
     */
    private static List<Node> info() {
        List<Node> nodes = new ArrayList<>();
        for (String assistant : assistants)
            nodes.add(node("info",
                    node(assistant)));
        if (specialCharacters == null)
            initializeSpecialCharacters();
        for (String specialCharacter : specialCharacters)
            nodes.add(node("info",
                    node(specialCharacter)));
        return Collections.unmodifiableList(nodes);
    }

    /**
     * Gets all the playable assistants commands.
     *
     * @return All the playable assistants commands.
     */
    private static List<Node> playableAssistants() {
        List<Node> nodes = new ArrayList<>();
        for (String assistant : getPlayableAssistants())
            nodes.add(node("play", node(assistant)));
        return nodes;
    }

    /**
     * Gets all the refill commands.
     *
     * @return All the refill commands.
     */
    private static List<Node> refillFromClouds() {
        List<Node> nodes = new ArrayList<>();
        for (String cloud : getAvailableClouds())
            nodes.add(node("refill", node("entrance", node("from", node(cloud)))));
        return nodes;
    }

    /**
     * Gets all mother nature moves.
     *
     * @return All mother nature moves.
     */
    private static List<Node> motherNatureMoves() {
        List<Node> nodes = new ArrayList<>();
        for (String island : getMotherNatureIslands())
            nodes.add(node("move", node("mother-nature", node("to", node(island)))));
        return nodes;
    }

    /**
     * Gets all student's moves according to the entrance.
     *
     * @param entrance Player's entrance.
     * @return All possible student's moves.
     */
    private static List<Node> studentsMoves(Map<HouseColor, Integer> entrance) {
        List<Node> nodes = new ArrayList<>();
        for (Map.Entry<HouseColor, Integer> entry : entrance.entrySet())
            if (entry.getValue() != 0)
                for (String to : toList)
                    nodes.add(node("move",
                            node("student",
                                    node(entry.getKey().name().toLowerCase(Locale.ROOT),
                                            node("from",
                                                    node("entrance",
                                                            node("to",
                                                                    node(to))))))));
        return nodes;
    }

    /**
     * Gets all the playable special characters.
     *
     * @return All the playable special characters.
     */
    private static List<Node> payableSpecialCharacters() {
        List<Node> nodes = new ArrayList<>();
        int availableCoins = controller.getGameModel().getPlayerByName(controller.getUserName()).getCoins();
        for (SpecialCharacter specialCharacter : controller.getGameModel().getGameBoard().getSpecialCharacters())
            if (specialCharacter.getCost() <= availableCoins)
                nodes.add(node("pay", node(String.format("CHR%02d", specialCharacter.getId()))));
        return nodes;
    }

    /**
     * Gets all the special characters commands.
     *
     * @return All the special characters commands.
     */
    private static List<Node> specialCharactersActions() {

        Optional<SpecialCharacter> optional = controller.getGameModel().getGameBoard().getSpecialCharacters().stream().filter(SpecialCharacter::isActive).findFirst();

        if (optional.isEmpty())
            return List.of();

        SpecialCharacter specialCharacter = optional.get();

        return Collections.unmodifiableList(switch (specialCharacter.getId()) {
            case 1 -> specialCharacter1(specialCharacter.getStudents());
            case 3 -> specialCharacter3();
            case 5 -> specialCharacter5(specialCharacter.getAvailableBans());
            case 7 -> specialCharacter7(
                    controller.getGameModel().getPlayerByName(controller.getUserName()).getSchoolBoard().getEntrance(),
                    specialCharacter.getStudents()
            );
            case 9 -> specialCharacter9();
            case 10 -> specialCharacter10(
                    controller.getGameModel().getPlayerByName(controller.getUserName()).getSchoolBoard().getEntrance(),
                    controller.getGameModel().getPlayerByName(controller.getUserName()).getSchoolBoard().getDiningRoom()
            );
            case 11 -> specialCharacter11(specialCharacter.getStudents());
            case 12 -> specialCharacter12();
            default -> new ArrayList<Node>();
        });
    }

    /**
     * Gets all the possible commands from special character 1.
     *
     * @param students The students on the card.
     * @return All the possible commands from special character 1.
     */
    private static List<Node> specialCharacter1(Map<HouseColor, Integer> students) {
        List<Node> nodes = new ArrayList<>();
        for (Map.Entry<HouseColor, Integer> entry : students.entrySet())
            for (String island : islands)
                nodes.add(node("move",
                        node("student",
                                node(entry.getKey().name().toLowerCase(Locale.ROOT),
                                        node("from",
                                                node("CHR01",
                                                        node("to",
                                                                node(island))))))));
        return nodes;
    }

    /**
     * Gets all the possible commands from special character 3.
     *
     * @return All the possible commands from special character 3.
     */
    private static List<Node> specialCharacter3() {
        List<Node> nodes = new ArrayList<>();
        for (String island : islands)
            nodes.add(node("resolve",
                    node(island)));
        return nodes;
    }

    /**
     * Gets all the possible commands from special character 5.
     *
     * @param availableBans The number of remaining bans on the card.
     * @return All the possible commands from special character 5.
     */
    private static List<Node> specialCharacter5(int availableBans) {
        List<Node> nodes = new ArrayList<>();
        if (availableBans > 0)
            for (String island : islands)
                nodes.add(node("ban",
                        node(island)));
        return nodes;
    }

    /**
     * Gets all the possible commands from special character 7.
     *
     * @param entrance The students on player's entrance.
     * @param card     The students on the card.
     * @return All the possible commands from special character 7.
     */
    private static List<Node> specialCharacter7(Map<HouseColor, Integer> entrance, Map<HouseColor, Integer> card) {
        List<Node> nodes = new ArrayList<>();
        for (Map.Entry<HouseColor, Integer> entranceEntry : entrance.entrySet())
            for (Map.Entry<HouseColor, Integer> cardEntry : card.entrySet())
                if (entranceEntry.getValue() != 0 && cardEntry.getValue() != 0) {
                    nodes.add(node("swap",
                            node("entrance-student",
                                    node(entranceEntry.getKey().name().toLowerCase(Locale.ROOT),
                                            node("with",
                                                    node("CHR07-student",
                                                            node(cardEntry.getKey().name().toLowerCase(Locale.ROOT))))))));
                    nodes.add(node("swap",
                            node("CHR07-student",
                                    node(cardEntry.getKey().name().toLowerCase(Locale.ROOT),
                                            node("with",
                                                    node("entrance-student",
                                                            node(entranceEntry.getKey().name().toLowerCase(Locale.ROOT))))))));
                }
        return nodes;
    }

    /**
     * Gets all the possible commands from special character 9.
     *
     * @return All the possible commands from special character 9.
     */
    private static List<Node> specialCharacter9() {
        List<Node> nodes = new ArrayList<>();
        for (HouseColor color : HouseColor.values())
            nodes.add(node("ignore",
                    node(color.name().toLowerCase(Locale.ROOT))));
        return nodes;
    }

    /**
     * Gets all the possible commands from special character 10.
     *
     * @param entrance   The students on player's entrance.
     * @param diningRoom The students in player's dining room.
     * @return All the possible commands from special character 10.
     */
    private static List<Node> specialCharacter10(Map<HouseColor, Integer> entrance, Map<HouseColor, Integer> diningRoom) {
        List<Node> nodes = new ArrayList<>();
        for (Map.Entry<HouseColor, Integer> entranceEntry : entrance.entrySet())
            for (Map.Entry<HouseColor, Integer> diningRoomEntry : diningRoom.entrySet())
                if (entranceEntry.getValue() != 0 && diningRoomEntry.getValue() != 0) {
                    nodes.add(node("swap",
                            node("entrance-student",
                                    node(entranceEntry.getKey().name().toLowerCase(Locale.ROOT),
                                            node("with",
                                                    node("dining-room-student",
                                                            node(diningRoomEntry.getKey().name().toLowerCase(Locale.ROOT))))))));
                    nodes.add(node("swap",
                            node("dining-room-student",
                                    node(diningRoomEntry.getKey().name().toLowerCase(Locale.ROOT),
                                            node("with",
                                                    node("entrance-student",
                                                            node(entranceEntry.getKey().name().toLowerCase(Locale.ROOT))))))));
                }
        return nodes;
    }

    /**
     * Gets all the possible commands from special character 11.
     *
     * @param students The students on the card.
     * @return All the possible commands from special character 1.
     */
    private static List<Node> specialCharacter11(Map<HouseColor, Integer> students) {
        List<Node> nodes = new ArrayList<>();
        for (Map.Entry<HouseColor, Integer> entry : students.entrySet())
            if (entry.getValue() != 0)
                nodes.add(node("take",
                        node("CHR11-student",
                                node(entry.getKey().name().toLowerCase(Locale.ROOT)))));
        return nodes;
    }

    /**
     * Gets all the possible commands from special character 12.
     *
     * @return All the possible commands from special character 12.
     */
    private static List<Node> specialCharacter12() {
        List<Node> nodes = new ArrayList<>();
        for (HouseColor color : HouseColor.values())
            nodes.add(node("return",
                    node("students",
                            node(color.name().toLowerCase(Locale.ROOT)))));
        return nodes;
    }

    /**
     * Gets all the playable assistants of the current player.
     *
     * @return All the playable assitants of the current player.
     */
    private static List<String> getPlayableAssistants() {
        List<String> result = new ArrayList<>();
        for (Assistant assistant : controller.getGameModel().getPlayerByName(controller.getUserName()).getHand())
            result.add(String.format("AST%02d", assistant.getId()));
        return Collections.unmodifiableList(result);
    }

    /**
     * Gets all the currently available clouds.
     *
     * @return All the currently available clouds.
     */
    private static List<String> getAvailableClouds() {
        List<String> result = new ArrayList<>();
        for (int index = 0; index < controller.getGameModel().getGameBoard().getClouds().size(); index++)
            if (controller.getGameModel().getGameBoard().getClouds().get(index).getStudents(false) != null)
                result.add(String.format("CL%1d", index + 1));
        return Collections.unmodifiableList(result);
    }

    /**
     * Gets all the islands on which mother nature can go on.
     *
     * @return All the islands on which mother nature can go on.
     */
    private static List<String> getMotherNatureIslands() {

        List<String> result = new ArrayList<>();
        GameBoard gameBoard = controller.getGameModel().getGameBoard();

        int index = controller.getGameModel().getGameBoard().getMotherNatureIsland();

        while (gameBoard.getIslandById(index).hasNext())
            index = (index + 1) % islands.size();

        for (int moves = 0; moves < controller.getGameModel().getPlayerByName(controller.getUserName()).getCurrentPlayedAssistant().getMaxDistance(); moves++) {

            index = (index + 1) % islands.size();

            if (gameBoard.getIslandById(index).hasMotherNature())
                return Collections.unmodifiableList(result);

            result.add(String.format("ISL%02d", index + 1));

            while (gameBoard.getIslandById(index).hasNext())
                index = (index + 1) % islands.size();
        }

        return Collections.unmodifiableList(result);
    }
}
