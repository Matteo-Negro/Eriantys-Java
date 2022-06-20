package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.view.gui.CommandAssembler;
import it.polimi.ingsw.utilities.HouseColor;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;

import java.util.*;
import java.util.function.Consumer;

public class SpecialCharacterContainer {

    private final Consumer<List<HouseColor>> updateStudents;
    private final int idSpecialCharacter;
    private final CommandAssembler commandAssembler;
    private List<Button> bansImages;
    private Line connection;
    private Parent pane;
    private Map<HouseColor, Integer> students;
    private List<Button> studentsImages;
    private int bansNum;
    private ImageView extraPrice;

    SpecialCharacterContainer(int idSpecialCharacter, CommandAssembler assembler) {
        this.commandAssembler = assembler;
        this.idSpecialCharacter = idSpecialCharacter;
        this.connection = null;
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
                    switch (idSpecialCharacter) {
                        case 1 -> commandAssembler.manageStudentSCFromCardToIslandSelection(students.get(studentIndex));
                        case 7 -> commandAssembler.manageStudentSCSwapCardEntranceSelection(students.get(studentIndex));
                        case 9 -> commandAssembler.manageStudentSCIgnoreColorSelection(students.get(studentIndex));
                        case 11 ->
                                commandAssembler.manageStudentSCFromCardToDiningRoomSelection(students.get(studentIndex));
                        case 12 -> commandAssembler.manageStudentSCReturnColorSelection(students.get(studentIndex));
                    }
                });
                enableStudentButtons(false);
            }
        };
    }

    void setConnection(Line connection) {
        this.connection = connection;
    }

    public void connect() {
        Platform.runLater(() -> this.connection.setVisible(true));
    }

    public Parent getPane() {
        return pane;
    }

    void setPane(Parent pane) {
        this.pane = pane;
    }

    void setStudents(Map<HouseColor, Integer> students) {
        this.students = new EnumMap<>(students);
        if (studentsImages != null)
            updateStudents(false);
    }

    public List<Button> getStudentsImages() {
        return this.studentsImages;
    }

    void setStudentsImages(List<Button> studentButtons) {
        this.studentsImages = Collections.unmodifiableList(studentButtons);
        if (students != null)
            updateStudents(false);
    }

    public void updateStudents(Map<HouseColor, Integer> entrance) {
        for (HouseColor color : HouseColor.values())
            this.students.replace(color, entrance.get(color));
        updateStudents(true);
    }

    private void updateStudents(boolean safe) {
        List<HouseColor> studentsList = studentsToList();
        if (safe)
            Platform.runLater(() -> updateStudents.accept(studentsList));
        else
            updateStudents.accept(studentsList);
    }

    void setBansNum(int bansNum) {
        this.bansNum = bansNum;
    }

    void setBansImages(List<Button> banButtons) {
        this.bansImages = Collections.unmodifiableList(banButtons);
        if (bansNum != 0)
            Platform.runLater(() -> updateBans(this.bansNum));
        else
            updateBans(this.bansNum);
    }

    public void updateBans(int bansNum) {
        if (bansNum > 0) Platform.runLater(() -> {
            for (int index = 0; index < bansImages.size(); index++) {
                this.bansImages.get(index).setGraphic(Images.banIcon());
                this.bansImages.get(index).setVisible(index < bansNum);
                this.bansImages.get(index).setOnMouseClicked(mouseEvent -> {
                    if (this.idSpecialCharacter == 5) this.commandAssembler.manageStudentSCBanSelection();
                });
                enableBanButtons(false);
            }
        });
    }

    void setExtraPrice(ImageView extraPrice) {
        this.extraPrice = extraPrice;
    }

    public void updateExtraPrice(boolean extraPrice) {
        Platform.runLater(() -> this.extraPrice.setVisible(extraPrice));
    }

    public void enableStudentButtons(boolean enable) {
        if (this.studentsImages == null)
            return;

        for (Button studentButton : this.studentsImages) {
            if (enable)
                // TODO: check for it
                studentButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-border-radius: 50em;" +
                        "-fx-border-width: 1px;" +
                        "-fx-min-width: 25px;" +
                        "-fx-min-height: 25px;" +
                        "-fx-padding: 2px;" +
                        "-fx-border-color: #FCFFAD;" +
                        "-fx-background-color: radial-gradient(focus-distance 0% ,center 50% 50%, radius 99%, transparent, #FCFFAD);");
            else
                // TODO: check for it
                studentButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-max-width: 10px;" +
                        "-fx-max-height: 10px;" +
                        "-fx-padding: 0px;");
            studentButton.setMouseTransparent(!enable);
        }
    }

    public void enableBanButtons(boolean enable) {
        if (this.bansImages == null)
            return;

        for (Button banButton : this.bansImages) {
            if (enable)
                // TODO: check for it
                banButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-border-radius: 50em;" +
                        "-fx-border-width: 1px;" +
                        "-fx-min-width: 25px;" +
                        "-fx-min-height: 25px;" +
                        "-fx-padding: 2px;" +
                        "-fx-border-color: #FCFFAD;" +
                        "-fx-background-color: radial-gradient(focus-distance 0% ,center 50% 50%, radius 99%, transparent, #FCFFAD);");
            else
                // TODO: check for it
                banButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-max-width: 10px;" +
                        "-fx-max-height: 10px;" +
                        "-fx-padding: 0px;");
            banButton.setMouseTransparent(!enable);
        }
    }

    public void enableCharacterButton(boolean activePlayer, boolean enable, boolean effectActive, boolean active) {
        Button characterButton = (Button) this.getPane().getChildrenUnmodifiable().get(0);

        if (activePlayer && enable && !effectActive && !active) {
            characterButton.setStyle("-fx-border-color: #FCFFAD;" +
                    "-fx-border-width: 1px;" +
                    "-fx-background-color: radial-gradient(focus-distance 0%, center 50% 50%, radius 90%, transparent, #FCFFAD);");
            characterButton.setMouseTransparent(false);
        } else if (enable && effectActive && active) {
            characterButton.setStyle("-fx-border-color: #38DC77;" +
                    "-fx-border-width: 2px;" +
                    "-fx-background-color: radial-gradient(focus-distance 0%, center 50% 50%, radius 90%, transparent, #38DC77);");
            characterButton.setMouseTransparent(true);
        } else {
            characterButton.setStyle("-fx-border-width: 0px;" +
                    "-fx-background-color: transparent;" +
                    "-fx-min-width: 130px;" +
                    "-fx-min-height: 130px;");
            characterButton.setMouseTransparent(true);
        }
        enableStudentButtons(activePlayer && enable && effectActive && active);
        enableBanButtons(activePlayer && enable && effectActive && active);
        characterButton.setMouseTransparent(!activePlayer || !enable || effectActive || active);
    }

    private List<HouseColor> studentsToList() {
        List<HouseColor> list = new ArrayList<>();
        for (Map.Entry<HouseColor, Integer> entry : students.entrySet())
            for (int index = 0; index < entry.getValue(); index++)
                list.add(entry.getKey());
        return list;
    }
}
