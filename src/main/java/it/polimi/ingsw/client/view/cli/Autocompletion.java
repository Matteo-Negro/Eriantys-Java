package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.ClientCli;
import it.polimi.ingsw.client.model.Assistant;
import it.polimi.ingsw.client.model.SpecialCharacter;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Phase;
import org.jline.builtins.Completers.TreeCompleter;
import org.jline.builtins.Completers.TreeCompleter.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.jline.builtins.Completers.TreeCompleter.node;

public class Autocompletion {
    private static ClientCli cli = null;
    private static List<String> islands = null;
    private static List<String> specialCharacters = null;

    private Autocompletion() {
    }

    public static void initialize(ClientCli cli) {
        if (Autocompletion.cli != null)
            return;
        Autocompletion.cli = cli;
        initializeIslands();
        initializeSpecialCharacters();
    }

    private static void initializeIslands() {
        List<String> islands = new ArrayList<>();
        for (int index = 1; index <= 12; index++)
            islands.add(String.format("ISL%02d", index));
        Autocompletion.islands = Collections.unmodifiableList(islands);
    }

    private static void initializeSpecialCharacters() {
        List<String> specialCharacters = new ArrayList<>();
        for (SpecialCharacter specialCharacter : cli.getGameModel().getGameBoard().getSpecialCharacters())
            specialCharacters.add(String.format("CHR%02d", specialCharacter.getId()));
        Autocompletion.specialCharacters = Collections.unmodifiableList(specialCharacters);
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
            if (cli.getGameModel().isExpert() && cli.getGameModel().getGameBoard().getSpecialCharacters().stream().anyMatch(SpecialCharacter::isActive))
                nodes.addAll(studentsMoves());
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
        for (String island : islands)
            nodes.add(node("move", node("mother", node("nature", node("to", node(island))))));
        return nodes;
    }

    private static List<Node> studentsMoves() {
        List<Node> nodes = new ArrayList<>();
        for (HouseColor color : HouseColor.values())
            for (String from : getFromList())
                for (String to : getToList())
                    nodes.add(node("move",
                            node("student",
                                    node(color.name().toLowerCase(Locale.ROOT),
                                            node("from",
                                                    node(from,
                                                            node("to",
                                                                    node(to))))))));
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

    private static List<String> getFromList() {
        List<String> result = new ArrayList<>();
        result.add("entrance");
        if (cli.getGameModel().isExpert()) {
            result.add("dining-room");
            result.addAll(islands);
            result.addAll(specialCharacters);
        }
        return Collections.unmodifiableList(result);
    }

    private static List<String> getToList() {
        List<String> result = new ArrayList<>();
        result.add("dining-room");
        result.addAll(islands);
        if (cli.getGameModel().isExpert()) {
            result.add("entrance");
            result.addAll(specialCharacters);
        }
        return Collections.unmodifiableList(result);
    }
}
