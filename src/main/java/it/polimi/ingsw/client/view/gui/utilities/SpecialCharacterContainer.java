package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.GameModel;
import it.polimi.ingsw.client.view.gui.CommandAssembler;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Log;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.util.*;
import java.util.function.Consumer;

/**
 * This class is a simple interface to manage a GUI special character. Provides all the required methods to create and update it.
 */
public class SpecialCharacterContainer {

    private final Consumer<List<HouseColor>> updateStudents;
    private final int idSpecialCharacter;
    private final CommandAssembler commandAssembler;
    private List<Button> bansImages;
    private Parent pane;
    private Map<HouseColor, Integer> students;
    private List<Button> studentsImages;
    private int bansNum;
    private ImageView extraPrice;

    /**
     * Default class constructor.
     *
     * @param idSpecialCharacter The id of the special character.
     * @param assembler          CommandAssembler for generating the commands to send to the server.
     */
    SpecialCharacterContainer(int idSpecialCharacter, CommandAssembler assembler) {
        this.commandAssembler = assembler;
        this.idSpecialCharacter = idSpecialCharacter;
        this.pane = null;
        this.students = null;
        this.studentsImages = null;
        this.bansImages = null;
        this.bansNum = 0;
        this.extraPrice = null;
        updateStudents = students -> {
            for (int index = 0; index < studentsImages.size(); index++) {
                int studentIndex = index;
                studentsImages.get(index).setGraphic(Images.student2d(index < students.size() ? students.get(index) : null));
                studentsImages.get(index).setVisible(index < students.size() && students.get(index) != null);
                studentsImages.get(index).setOnMouseClicked(mouseEvent -> {
                    enableStudentsButtonExcept(studentIndex);
                    switch (idSpecialCharacter) {
                        case 1 -> commandAssembler.manageStudentSCFromCardToIslandSelection(students.get(studentIndex));
                        case 7 -> commandAssembler.manageStudentSCSwapCardEntranceSelection(students.get(studentIndex));
                        case 9 -> commandAssembler.manageStudentSCIgnoreColorSelection(students.get(studentIndex));
                        case 10 ->
                                commandAssembler.manageStudentSCSwapCardEntranceDiningRoomSelection(students.get(studentIndex), studentIndex);
                        case 11 ->
                                commandAssembler.manageStudentSCFromCardToDiningRoomSelection(students.get(studentIndex));
                        case 12 -> commandAssembler.manageStudentSCReturnColorSelection(students.get(studentIndex));
                    }
                });
                enableStudentButtons(false);
            }
        };
    }

    /**
     * Gets the pane to print on the screen.
     *
     * @return The pane with all the graphics.
     */
    public Parent getPane() {
        return pane;
    }

    /**
     * Initializes the pane.
     *
     * @param pane The pane to add.
     */
    void setPane(Parent pane) {
        this.pane = pane;
    }

    /**
     * Sets the student on the character.
     *
     * @param students The students on the character.
     */
    void setStudents(Map<HouseColor, Integer> students) {
        this.students = new EnumMap<>(students);
        if (studentsImages != null)
            updateStudents(false);
    }

    /**
     * Sets the students button for selecting the students.
     *
     * @param studentButtons The buttons.
     */
    void setStudentsImages(List<Button> studentButtons) {
        this.studentsImages = Collections.unmodifiableList(studentButtons);
        if (students != null)
            updateStudents(false);
    }

    /**
     * Updates the students number.
     *
     * @param students The new number of students.
     */
    public void updateStudents(Map<HouseColor, Integer> students) {
        if (students == null)
            return;
        for (HouseColor color : HouseColor.values())
            this.students.replace(color, students.get(color));
        updateStudents(true);
    }

    /**
     * Updates the students on the card.
     *
     * @param safe true if it has to be run on the GUI thread, false otherwise.
     */
    private void updateStudents(boolean safe) {
        List<HouseColor> studentsList = studentsToList();
        if (safe)
            Platform.runLater(() -> updateStudents.accept(studentsList));
        else
            updateStudents.accept(studentsList);
    }

    /**
     * Sets the number of bans.
     *
     * @param bansNum The number of bans.
     */
    void setBansNum(int bansNum) {
        this.bansNum = bansNum;
    }

    /**
     * Sets all the bans.
     *
     * @param banButtons The list of bans.
     */
    void setBansImages(List<Button> banButtons) {
        this.bansImages = Collections.unmodifiableList(banButtons);
        if (bansNum != 0)
            Platform.runLater(() -> updateBans(this.bansNum));
        else
            updateBans(this.bansNum);
    }

    /**
     * Updates the number of available bans on the card.
     *
     * @param bansNum The number of bans.
     */
    public void updateBans(Integer bansNum) {

        if (bansNum == null)
            return;

        if (bansNum > 0) Platform.runLater(() -> {
            for (int index = 0; index < bansImages.size(); index++) {
                int banNumber = index;
                this.bansImages.get(index).setGraphic(Images.banIcon());
                this.bansImages.get(index).setVisible(index < bansNum);
                this.bansImages.get(index).setOnMouseClicked(mouseEvent -> {
                    enableBanButtonsExcept(banNumber);
                    if (this.idSpecialCharacter == 5) this.commandAssembler.manageStudentSCBanSelection();
                });
                enableBanButtons(false);
            }
        });
    }

    /**
     * Sets the increment to the price.
     *
     * @param alreadyPaid true if this card has already been played.
     * @param coin        The ImageView for the coin.
     */
    public void setExtraPrice(boolean alreadyPaid, ImageView coin) {
        this.extraPrice = coin;
        this.extraPrice.setVisible(alreadyPaid);
    }

    /**
     * Updates the price of the card.
     *
     * @param extraPrice true if it has to be increased.
     */
    public void updateExtraPrice(boolean extraPrice) {
        Platform.runLater(() -> this.extraPrice.setVisible(extraPrice));
        Log.debug("set extra price visibility to " + extraPrice);
    }

    /**
     * Enables or disables the students buttons.
     *
     * @param enable Enable signal.
     */
    public void enableStudentButtons(boolean enable) {

        if (this.studentsImages == null)
            return;

        for (Button studentButton : this.studentsImages) {
            studentButton.setDisable(false);
            if (enable)
                studentButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-border-radius: 50em;" +
                        "-fx-border-width: 1px;" +
                        "-fx-min-width: 25px;" +
                        "-fx-min-height: 25px;" +
                        "-fx-padding: 3px;" +
                        "-fx-border-color: #FCFFAD;" +
                        "-fx-background-color: radial-gradient(focus-distance 0% ,center 50% 50%, radius 99%, transparent, #FCFFAD);");
            else
                studentButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-max-width: 10px;" +
                        "-fx-max-height: 10px;" +
                        "-fx-padding: 0px;");
            studentButton.setMouseTransparent(!enable);
        }
    }

    /**
     * Enables all the student buttons except for the given one.
     *
     * @param index The index of the student button to disable.
     */
    private void enableStudentsButtonExcept(int index) {
        enableStudentButtons(true);
        this.studentsImages.get(index).setDisable(true);
    }

    /**
     * Enables or disables the bans buttons.
     *
     * @param enable Enable signal.
     */
    public void enableBanButtons(boolean enable) {

        if (this.bansImages == null)
            return;

        for (Button banButton : this.bansImages) {
            banButton.setDisable(false);
            if (enable)
                banButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-border-radius: 50em;" +
                        "-fx-border-width: 0px;" +
                        "-fx-min-width: 25px;" +
                        "-fx-min-height: 25px;" +
                        "-fx-padding: 3px;" +
                        "-fx-border-color: #FCFFAD;" +
                        "-fx-background-color: radial-gradient(focus-distance 0% ,center 50% 50%, radius 99%, transparent, #FCFFAD);");
            else
                banButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-max-width: 10px;" +
                        "-fx-max-height: 10px;" +
                        "-fx-padding: 0px;");
            banButton.setMouseTransparent(!enable);
        }
    }

    /**
     * Enables all the student buttons except for the given one.
     *
     * @param index The index of the student button to disable.
     */
    private void enableBanButtonsExcept(int index) {
        enableBanButtons(true);
        this.bansImages.get(index).setDisable(true);
    }

    /**
     * Enables or disables the button for the special character.
     *
     * @param enable       Enable signal.
     * @param activePlayer true of the player is active.
     * @param model        The GameModel from which all the required data are taken.
     */
    public void enableCharacterButton(boolean enable, boolean activePlayer, GameModel model) {
        if (enable) {
            pane.getChildrenUnmodifiable().get(1).setStyle(
                    "-fx-border-color: #66eb66;" +
                            "-fx-border-width: 2px;" +
                            "-fx-padding: 0px;" +
                            "-fx-background-color: radial-gradient(focus-distance 0% ,center 50% 50%, radius 99.9%, transparent, #66eb66);"
            );
        } else pane.getChildrenUnmodifiable().get(1).setStyle(
                "-fx-border-color: transparent;"
        );

        int usesNumber = model.getGameBoard().getSpecialCharacterById(this.idSpecialCharacter).getUsesNumber();
        enableBanButtons(activePlayer && enable && usesNumber > 0);
        enableStudentButtons(activePlayer && enable && usesNumber > 0);
    }

    /**
     * Converts the map of students into an ordered list.
     *
     * @return The generated list.
     */
    private List<HouseColor> studentsToList() {
        boolean end = false;
        List<HouseColor> list = new ArrayList<>();

        while (!end) {
            Arrays.stream(HouseColor.values()).forEach(color -> {
                if (list.stream().filter(c -> c.equals(color)).count() < students.get(color)) list.add(color);
            });
            if (list.size() == students.values().stream().mapToInt(i -> i).sum()) end = true;
        }

        return list;
    }
}
