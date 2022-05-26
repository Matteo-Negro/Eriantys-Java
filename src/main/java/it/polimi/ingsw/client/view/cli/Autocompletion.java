package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.ClientCli;
import it.polimi.ingsw.client.model.Assistant;
import it.polimi.ingsw.client.model.GameBoard;
import it.polimi.ingsw.client.model.Island;
import it.polimi.ingsw.client.model.SpecialCharacter;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Phase;
import org.jline.builtins.Completers.TreeCompleter;
import org.jline.builtins.Completers.TreeCompleter.Node;

import java.util.*;

import static org.jline.builtins.Completers.TreeCompleter.node;

public class Autocompletion {
    private static ClientCli cli = null;
    private static List<String> islands = null;
    private static List<String> toList = null;

    private Autocompletion() {
    }

    public static void initialize(ClientCli cli) {
        if (Autocompletion.cli != null)
            return;
        Autocompletion.cli = cli;
        initializeIslands();
        initializeToList();
    }

    private static void initializeIslands() {
        List<String> islands = new ArrayList<>();
        for (int index = 1; index <= 12; index++)
            islands.add(String.format("ISL%02d", index));
        Autocompletion.islands = Collections.unmodifiableList(islands);
    }

    private static void initializeToList() {
        List<String> toList = new ArrayList<>();
        toList.add("dining-room");
        toList.addAll(islands);
        Autocompletion.toList = Collections.unmodifiableList(toList);
    }

    public static List<Node> get() {
        List<TreeCompleter.Node> nodes = new ArrayList<>();
        nodes.add(node("exit"));
        nodes.add(node("logout"));
        if (cli.getGameModel().getPhase().equals(Phase.PLANNING))
            nodes.addAll(playableAssistants());
        else {
            switch (cli.getGameModel().getSubphase()) {
                case CHOOSE_CLOUD -> nodes.addAll(refillFromClouds());
                case MOVE_MOTHER_NATURE -> nodes.addAll(motherNatureMoves());
                case MOVE_STUDENT_1, MOVE_STUDENT_2, MOVE_STUDENT_3, MOVE_STUDENT_4 -> nodes.addAll(studentsMoves());
            }
            // TODO: add payment for special character
            // TODO: add every special character special commands (switch case)
            if (cli.getGameModel().isExpert()) {
                if (cli.getGameModel().getGameBoard().getSpecialCharacters().stream().anyMatch(SpecialCharacter::isActive))
                    nodes.addAll(specialCharactersActions());
                else
                    nodes.addAll(payableSpecialCharacters());
            }
        }
        return nodes;
    }

    private static List<Node> playableAssistants() {
        List<Node> nodes = new ArrayList<>();
        for (String assistant : getPlayableAssistants())
            nodes.add(node("play", node(assistant)));
        return nodes;
    }

    private static List<Node> refillFromClouds() {
        List<Node> nodes = new ArrayList<>();
        for (String cloud : getAvailableClouds())
            nodes.add(node("refill", node("entrance", node("from", node(cloud)))));
        return nodes;
    }

    private static List<Node> motherNatureMoves() {
        List<Node> nodes = new ArrayList<>();
        for (String island : getMotherNatureIslands())
            nodes.add(node("move", node("mother", node("nature", node("to", node(island))))));
        return nodes;
    }

    private static List<Node> studentsMoves() {
        List<Node> nodes = new ArrayList<>();
        for (HouseColor color : HouseColor.values())
            for (String to : toList)
                nodes.add(node("move",
                        node("student",
                                node(color.name().toLowerCase(Locale.ROOT),
                                        node("from",
                                                node("entrance",
                                                        node("to",
                                                                node(to))))))));
        return nodes;
    }

    private static List<Node> payableSpecialCharacters() {
        List<Node> nodes = new ArrayList<>();
        int availableCoins = cli.getGameModel().getPlayerByName(cli.getUserName()).getCoins();
        for (SpecialCharacter specialCharacter : cli.getGameModel().getGameBoard().getSpecialCharacters())
            if (specialCharacter.getCost() <= availableCoins)
                nodes.add(node("pay", node(String.format("CHR%02d", specialCharacter.getId()))));
        return nodes;
    }

    private static List<Node> specialCharactersActions() {
        List<Node> nodes = new ArrayList<>();
        SpecialCharacter specialCharacter = cli.getGameModel().getGameBoard().getSpecialCharacters().stream().filter(SpecialCharacter::isActive).findFirst().get();
        try {
            switch (specialCharacter.getId()) {
                case 1 -> {
                    for (Map.Entry<HouseColor, Integer> entry : specialCharacter.getStudents().entrySet())
                        for (String island : islands)
                            nodes.add(node("move",
                                    node("student",
                                            node(entry.getKey().name().toLowerCase(Locale.ROOT),
                                                    node("from",
                                                            node("CHR01",
                                                                    node("to",
                                                                            node(island))))))));
                }
                case 3 -> {
                    for (String island : islands)
                        nodes.add(node("resolve",
                                node(island)));
                }
                case 5 -> {
                    if (specialCharacter.getAvailableBans() > 0)
                        for (String island : islands)
                            nodes.add(node("ban",
                                    node(island)));
                }
                case 7 -> {
                    Map<HouseColor, Integer> entrance = cli.getGameModel().getPlayerByName(cli.getUserName()).getSchoolBoard().getEntrance();
                    Map<HouseColor, Integer> card = specialCharacter.getStudents();
                    for (Map.Entry<HouseColor, Integer> entranceEntry : entrance.entrySet())
                        for (Map.Entry<HouseColor, Integer> cardEntry : card.entrySet())
                            if (entranceEntry.getValue() != 0 && cardEntry.getValue() != 0)
                                nodes.add(node("swap",
                                        node("entrance-student",
                                                node(entranceEntry.getKey().name().toLowerCase(Locale.ROOT),
                                                        node("with",
                                                                node("AST07-student",
                                                                        node(cardEntry.getKey().name().toLowerCase(Locale.ROOT))))))));
                }
                case 9 -> {
                    for (HouseColor color : HouseColor.values())
                        nodes.add(node("ignore",
                                node("color",
                                        node(color.name().toLowerCase(Locale.ROOT)))));
                }
                case 10 -> {
                    Map<HouseColor, Integer> entrance = cli.getGameModel().getPlayerByName(cli.getUserName()).getSchoolBoard().getEntrance();
                    Map<HouseColor, Integer> diningRoom = cli.getGameModel().getPlayerByName(cli.getUserName()).getSchoolBoard().getDiningRoom();
                    for (Map.Entry<HouseColor, Integer> entranceEntry : entrance.entrySet())
                        for (Map.Entry<HouseColor, Integer> diningRoomEntry : diningRoom.entrySet())
                            if (entranceEntry.getValue() != 0 && diningRoomEntry.getValue() != 0)
                                nodes.add(node("swap",
                                        node("entrance-student",
                                                node(entranceEntry.getKey().name().toLowerCase(Locale.ROOT),
                                                        node("with",
                                                                node("dining-room-student",
                                                                        node(diningRoomEntry.getKey().name().toLowerCase(Locale.ROOT))))))));
                }
                case 11 -> {
                    for (Map.Entry<HouseColor, Integer> entry : specialCharacter.getStudents().entrySet())
                        if (entry.getValue() != 0)
                            nodes.add(node("take",
                                    node("AST11-student",
                                            node(entry.getKey().name().toLowerCase(Locale.ROOT)))));
                }
                case 12 -> {
                    for (HouseColor color : HouseColor.values())
                        nodes.add(node("return",
                                node("students",
                                        node(color.name().toLowerCase(Locale.ROOT)))));
                }
            }
        } catch (NoSuchElementException e) {
            return List.of();
        }
        return nodes;
    }

    private static List<String> getPlayableAssistants() {
        List<String> result = new ArrayList<>();
        for (Assistant assistant : cli.getGameModel().getPlayerByName(cli.getUserName()).getHand())
            result.add(String.format("AST%02d", assistant.getId()));
        return Collections.unmodifiableList(result);
    }

    private static List<String> getAvailableClouds() {
        List<String> result = new ArrayList<>();
        for (int index = 0; index < cli.getGameModel().getGameBoard().getClouds().size(); index++)
            if (cli.getGameModel().getGameBoard().getClouds().get(index).getStudents(false) != null)
                result.add(String.format("CL%1d", index + 1));
        return Collections.unmodifiableList(result);
    }

    private static List<String> getMotherNatureIslands() {
        List<String> result = new ArrayList<>();
        List<Island> islands = cli.getGameModel().getGameBoard().getIslands();
        int index = (getMotherNatureIsland() + 1) % islands.size();
        for (int moves = 0; moves < cli.getGameModel().getPlayerByName(cli.getUserName()).getCurrentPlayedAssistant().getMaxDistance(); moves++)
            do {
                if (islands.get(index).hasMotherNature())
                    return Collections.unmodifiableList(result);
                result.add(String.format("ISL%02d", index + 1));
                index = (index + 1) % islands.size();
            } while (islands.get(index).hasNext());
        return Collections.unmodifiableList(result);
    }

    private static int getMotherNatureIsland() {
        GameBoard gameBoard = cli.getGameModel().getGameBoard();
        for (int index = 0; index < gameBoard.getIslands().size(); index++)
            if (gameBoard.getIslands().get(index).hasMotherNature())
                return index;
        return 0;
    }
}
