package it.polimi.ingsw.network.client;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.WizardType;
import it.polimi.ingsw.view.cli.SchoolBoard;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

import static it.polimi.ingsw.view.cli.Utilities.clearScreen;
import static org.fusesource.jansi.Ansi.ansi;

public class Client {

    /**
     * Graphics types: CLI or GUI.
     */
    public enum GraphicsType {CLI, GUI}

    /**
     * Default constructor.
     *
     * @param graphicsType The type of interface that has to be instanced.
     */
    public Client(GraphicsType graphicsType) throws IOException {
        if (graphicsType.equals(GraphicsType.CLI)) {
            Terminal terminal = TerminalBuilder.terminal();
            Scanner input = new Scanner(terminal.input());
            clearScreen(terminal, false);
            // Realm printing
            // Realm.print(terminal);

            // Splash screen printing and control
//            SplashScreen.print(terminal);
//            input.nextLine();
//            terminal.writer().print(ansi().restoreCursorPosition());
//            terminal.writer().print(ansi().cursorMove(-18, 1));
//            terminal.writer().print(ansi().saveCursorPosition());
//            terminal.flush();
//            input.nextLine();

            // SchoolBoard
            test(terminal);

            // clearScreen(terminal, true);
        }
    }

    private void test(Terminal terminal) {
        Ansi ansi = new Ansi();

        Map<HouseColor, Integer> entrance = new EnumMap<>(HouseColor.class);
        initializeMapInteger(entrance);
        entrance.put(HouseColor.YELLOW, 2);
        entrance.put(HouseColor.GREEN, 1);

        Map<HouseColor, Integer> diningRoom = new EnumMap<>(HouseColor.class);
        initializeMapInteger(diningRoom);
        diningRoom.put(HouseColor.FUCHSIA, 10);
        diningRoom.put(HouseColor.BLUE, 3);

        Map<HouseColor, Boolean> professors = new EnumMap<>(HouseColor.class);
        initializeMapBoolean(professors);
        professors.put(HouseColor.FUCHSIA, true);

        SchoolBoard.print(ansi, entrance, diningRoom, professors, TowerType.WHITE, 8, 3, 1, "Player 1", WizardType.FUCHSIA, true, true);

        terminal.writer().print(ansi);
        terminal.flush();

        terminal.writer().print(ansi().cursorDown(terminal.getHeight()));
        terminal.flush();
    }

    private void initializeMapInteger(Map<HouseColor, Integer> map) {
        for (HouseColor color : HouseColor.values())
            map.put(color, 0);
    }

    private void initializeMapBoolean(Map<HouseColor, Boolean> map) {
        for (HouseColor color : HouseColor.values())
            map.put(color, false);
    }
}
