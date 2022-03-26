package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.utilities.HouseColor;
import it.polimi.ingsw.model.utilities.WizardType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player {

    public String getName() {
        return "";
    }

    public WizardType getWizard() {
        return new WizardType();
    }

    public int getCoins() {
        return 0;
    }

    private void takeCoin() {

    }

    public List<Assistant> getAssistants() {
        return new ArrayList<>();
    }

    public void playAssistant(Assistant assistant) {

    }

    public void moveStudentSchoolBoard(HouseColor houseColor, String string) {      // Perché la stringa

    }

    public void moveStudentIsland(HouseColor houseColor, Island island) {

    }

    public void refillEntrance(Map<HouseColor, Integer>) {

    }

    public void paySpecialCharacter(int id) {

    }

    public SchoolBoard getSchoolBoard() {
        return new SchoolBoard();
    }
}
