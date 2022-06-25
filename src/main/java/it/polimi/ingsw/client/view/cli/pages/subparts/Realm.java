package it.polimi.ingsw.client.view.cli.pages.subparts;

import it.polimi.ingsw.client.model.GameBoard;
import it.polimi.ingsw.client.model.Island;
import it.polimi.ingsw.client.view.cli.Utilities;
import it.polimi.ingsw.client.view.cli.coordinates.*;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.util.List;

/**
 * Prints the whole realm.
 *
 * @author Riccardo Motta
 */
public class Realm {

    private Realm() {
    }

    /**
     * Method that prints all the islands and then all the clouds.
     *
     * @param terminal  Terminal where to write.
     * @param gameBoard The GameBoard form which take all the required data.
     */
    public static void print(Terminal terminal, GameBoard gameBoard) {
        terminal.writer().print(printIslands(gameBoard));
        terminal.flush();
        terminal.writer().print(printClouds(gameBoard.getClouds()));
        terminal.flush();
    }

    /**
     * Prints every island.
     *
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi printIslands(GameBoard gameboard) {

        Ansi ansi = new Ansi();

        // First half

        for (int index = 1; index <= 6; index++) {
            ansi.a(Utilities.moveCursor(
                    switch (index) {
                        case 1 -> IslandFirst.getInstance();
                        case 2 -> IslandNE.getInstance();
                        case 6 -> IslandSE.getInstance();
                        default -> IslandE.getInstance();
                    }
            ));
            ansi.a(printIsland(index, gameboard.getIslandById(index - 1)));
        }

        // Second half

        for (int index = 12; index >= 7; index--) {
            ansi.a(Utilities.moveCursor(
                    switch (index) {
                        case 12 -> IslandLast.getInstance();
                        case 11 -> IslandSE.getInstance();
                        case 7 -> IslandNE.getInstance();
                        default -> IslandE.getInstance();
                    }
            ));
            ansi.a(printIsland(index, gameboard.getIslandById(index - 1)));
        }

        ansi.a(Utilities.moveCursor(IslandsReset.getInstance()));

        return ansi;
    }

    /**
     * Gets the parameters of the island and prints it.
     *
     * @param id     The id of the island
     * @param island The island to print
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi printIsland(int id, Island island) {
        return it.polimi.ingsw.client.view.cli.pages.subparts.Island.print(id,
                island.getStudents(),
                island.getTower(),
                island.hasMotherNature(),
                island.isBanned(),
                island.hasNext(),
                island.hasPrev()
        );
    }

    /**
     * Prints every cloud.
     *
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi printClouds(List<it.polimi.ingsw.client.model.Cloud> clouds) {

        Ansi ansi = new Ansi();

        ansi.a(switch (clouds.size()) {
            case 2 -> Utilities.moveCursor(CloudFirst2Players.getInstance());
            case 3 -> Utilities.moveCursor(CloudFirst3Players.getInstance());
            default -> Utilities.moveCursor(CloudFirst4Players.getInstance());
        });

        for (int index = 0; index < clouds.size(); index++) {
            if (index != 0)
                ansi.a(Utilities.moveCursor(CloudE.getInstance()));
            ansi.a(Cloud.print(index + 1, clouds.get(index).getStudents(false), clouds.size() == 3 ? 4 : 3));
        }

        ansi.a(
                switch (clouds.size()) {
                    case 2 -> Utilities.moveCursor(CloudsReset2Players.getInstance());
                    case 3 -> Utilities.moveCursor(CloudsReset3Players.getInstance());
                    default -> Utilities.moveCursor(CloudsReset4Players.getInstance());
                }
        );

        return ansi;
    }
}
