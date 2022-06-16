package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Log;

public class CommandAssembler {

    public static void manageIslandSelection(int islandId) {
        Log.debug("Island selected with id " + islandId);
    }

    public static void manageCloudSelection(int cloudId) {
        Log.debug("Cloud selected with id " + cloudId);
    }

    public static void manageDiningRoomSelection() {
        Log.debug("Dining-room selected");
    }

    public static void manageEntranceSelection(HouseColor color) {
        Log.debug(("Selected student from entrance of color " + color.toString()));
    }
}
