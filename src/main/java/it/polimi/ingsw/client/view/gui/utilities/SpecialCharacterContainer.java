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
    private final List<Button> bansImages;
    private final int idSpecialCharacter;
    private Line connection;
    private Parent pane;
    private Map<HouseColor, Integer> students;
    private List<Button> studentsImages;
    private int bansNum;
    private ImageView extraPrice;
    private final CommandAssembler commandAssembler;

    SpecialCharacterContainer(int idSpecialCharacter) {
        this.commandAssembler = null;
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
                studentsImages.get(index).setGraphic(Images.student2d(index < students.size() ? students.get(index) : null));
                studentsImages.get(index).setVisible(index < students.size() && students.get(index) != null);
                studentsImages.get(index).setOnMouseClicked(mouseEvent -> {
                    switch (idSpecialCharacter) {
                        case 1 -> commandAssembler.manageStudentSCFromCardToIslandSelection();
                        case 7 -> commandAssembler.manageStudentSCSwapCardEntranceSelection();
                        case 9 -> commandAssembler.manageStudentSCIgnoreColorSelection();
                        case 11 -> commandAssembler.manageStudentSCFromCardToDiningRoomSelection();
                        case 12 -> commandAssembler.manageStudentSCReturnColorSelection();
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

    private List<HouseColor> studentsToList() {
        List<HouseColor> list = new ArrayList<>();
        for (Map.Entry<HouseColor, Integer> entry : students.entrySet())
            for (int index = 0; index < entry.getValue(); index++)
                list.add(entry.getKey());
        return list;
    }
}
