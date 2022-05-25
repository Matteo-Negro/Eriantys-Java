package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.ClientCli;
import it.polimi.ingsw.client.model.Assistant;
import it.polimi.ingsw.client.model.Cloud;
import it.polimi.ingsw.client.model.SpecialCharacter;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Phase;
import org.jline.builtins.Completers.TreeCompleter;
import org.jline.builtins.Completers.TreeCompleter.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.jline.builtins.Completers.TreeCompleter.node;

public class Autocompletion {

    private static List<String> assistants = null;
    private static List<String> islands = null;
    private static List<String> clouds = null;
    private static List<String> specialCharacters = null;

    private Autocompletion() {
    }

    public static List<Node> get(ClientCli cli) {
        List<TreeCompleter.Node> nodes = new ArrayList<>();
        initialize(cli);
        nodes.add(node("exit"));
        nodes.add(node("logout"));
        if (cli.getGameModel().getPhase().equals(Phase.PLANNING))
            nodes.addAll(playableAssistants());
        else {
            switch (cli.getGameModel().getSubphase()) {
                case CHOOSE_CLOUD -> nodes.addAll(refillFromClouds());
                case MOVE_MOTHER_NATURE -> nodes.addAll(motherNatureMoves());
                case MOVE_STUDENT_1, MOVE_STUDENT_2, MOVE_STUDENT_3, MOVE_STUDENT_4 ->
                        nodes.addAll(studentsMoves(false));
            }
            if (cli.getGameModel().isExpert() && cli.getGameModel().getGameBoard().getSpecialCharacters().stream().anyMatch(SpecialCharacter::isActive))
                nodes.addAll(studentsMoves(true));
        }
        return nodes;
    }

    private static void initialize(ClientCli cli) {
        initializeAssistants(cli.getGameModel().getPlayerByName(cli.getUserName()).getHand());
        initializeIslands();
        initializeClouds(cli.getGameModel().getGameBoard().getClouds());
        if (cli.getGameModel().isExpert())
            initializeSpecialCharacters(cli.getGameModel().getGameBoard().getSpecialCharacters());
    }

    private static void initializeAssistants(List<Assistant> assistants) {
        if (Autocompletion.assistants == null)
            Autocompletion.assistants = new ArrayList<>();
        else
            Autocompletion.assistants.clear();
        for (Assistant assistant : assistants)
            Autocompletion.assistants.add(String.format("AST%02d", assistant.getId()));
    }

    private static void initializeIslands() {
        if (islands != null)
            return;
        islands = new ArrayList<>();
        for (int index = 1; index <= 12; index++)
            islands.add(String.format("ISL%02d", index));
    }

    private static void initializeClouds(List<Cloud> clouds) {
        if (Autocompletion.clouds == null)
            Autocompletion.clouds = new ArrayList<>();
        else
            Autocompletion.clouds.clear();
        for (int index = 0; index < clouds.size(); index++)
            if (clouds.get(index).getStudents(false) != null)
                Autocompletion.clouds.add(String.format("CL%1d", index + 1));
    }

    private static void initializeSpecialCharacters(List<SpecialCharacter> specialCharacters) {
        if (Autocompletion.specialCharacters != null)
            return;
        Autocompletion.specialCharacters = new ArrayList<>();
        for (SpecialCharacter specialCharacter : specialCharacters)
            Autocompletion.specialCharacters.add(String.format("CHR%02d", specialCharacter.getId()));
    }

    private static List<Node> playableAssistants() {
        List<Node> nodes = new ArrayList<>();
        for (String assistant : assistants)
            nodes.add(node("play", node("assistant", node(assistant))));
        return nodes;
    }

    private static List<Node> refillFromClouds() {
        List<Node> nodes = new ArrayList<>();
        for (String cloud : clouds)
            nodes.add(node("refill", node("entrance", node("from", node(cloud)))));
        return nodes;
    }

    private static List<Node> motherNatureMoves() {
        List<Node> nodes = new ArrayList<>();
        for (String island : islands)
            nodes.add(node("move", node("mother", node("nature", node("to", node(island))))));
        return nodes;
    }

    private static List<Node> studentsMoves(boolean expert) {
        List<Node> nodes = new ArrayList<>();
        List<String> fromList = List.of("entrance");
        List<String> toList = new ArrayList<>(List.of("dining-room"));
        List<String> expertList = new ArrayList<>(List.of("dining-room", "entrance"));
        if (expert) {
            expertList.addAll(islands);
            expertList.addAll(specialCharacters);
        } else
            toList.addAll(islands);
        for (HouseColor color : HouseColor.values())
            for (String from : !expert ? fromList : expertList)
                for (String to : !expert ? toList : expertList)
                    nodes.add(node("move",
                            node("student",
                                    node(color.name().toLowerCase(Locale.ROOT),
                                            node("from",
                                                    node(from,
                                                            node("to",
                                                                    node(to))))))));
        return nodes;
    }
}
