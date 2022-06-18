package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.view.gui.CommandAssembler;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.WizardType;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BoardContainer {

    private final Supplier<Void> updateDiningRoom;
    private final Consumer<List<HouseColor>> updateEntrance;
    private ImageView assistant;
    private Label coins;
    private Map<HouseColor, Integer> diningRoom;
    private Map<HouseColor, List<ImageView>> diningRoomImages;
    private Map<HouseColor, Integer> entrance;
    private List<Button> entranceImages;
    private Parent pane;
    private Map<HouseColor, ImageView> professors;
    private List<ImageView> towers;
    private WizardType wizard;

    BoardContainer(CommandAssembler commandAssembler) {
        assistant = null;
        coins = null;
        diningRoom = null;
        diningRoomImages = null;
        entrance = null;
        entranceImages = null;
        pane = null;
        professors = null;
        towers = null;
        updateDiningRoom = () -> {
            for (HouseColor houseColor : HouseColor.values())
                for (int index = 0; index < 10; index++)
                    diningRoomImages.get(houseColor).get(index).setVisible(index < diningRoom.get(houseColor));
            return null;
        };
        updateEntrance = students -> {
            for (int index = 0; index < entranceImages.size(); index++) {
                final int studentNumber = index;
                entranceImages.get(index).setGraphic(Images.student2d(index < students.size() ? students.get(index) : null));
                entranceImages.get(index).setVisible(index < students.size() && students.get(index) != null);
                entranceImages.get(index).setOnMouseClicked(mouseEvent -> commandAssembler.manageEntranceSelection(students.get(studentNumber)));
                enableEntranceButtons(false);
            }
        };
        wizard = null;
    }

    public Parent getPane() {
        return pane;
    }

    void setPane(Parent pane) {
        this.pane = pane;
    }

    public void enable(boolean value) {
        Platform.runLater(() -> {
            if (value) {
                pane.setStyle("-fx-background-color: #38a2ed");
            } else
                pane.setStyle("-fx-background-color: rgba(255, 255, 255, 0%)");
        });
    }

    void setAssistant(ImageView assistant) {
        this.assistant = assistant;
    }

    public void updateAssistant(Integer id) {
        Platform.runLater(() -> {
            if (id == null)
                assistant.setImage(Images.getWizardByType(wizard));
            else
                assistant.setImage(Images.getAssistantById(id));
        });
    }

    void setCoins(Label coins) {
        this.coins = coins;
    }

    public void updateCoins(int coins) {
        if (this.coins != null && coins >= 0)
            this.coins.setText(String.format("x%d", coins));
    }

    void setDiningRoom(Map<HouseColor, Integer> diningRoom) {
        this.diningRoom = new EnumMap<>(diningRoom);
        if (diningRoomImages != null)
            updateDiningRoom(false);
    }

    void setDiningRoomImages(Map<HouseColor, List<ImageView>> diningRoom) {
        this.diningRoomImages = Collections.unmodifiableMap(diningRoom);
        if (this.diningRoom != null)
            updateDiningRoom(false);
    }

    public void updateDiningRoom(Map<HouseColor, Integer> diningRoom) {
        for (HouseColor color : HouseColor.values())
            this.diningRoom.replace(color, diningRoom.get(color));
        updateDiningRoom(true);
    }

    void setEntrance(Map<HouseColor, Integer> entranceColors) {
        this.entrance = new EnumMap<>(entranceColors);
        if (entranceImages != null)
            updateEntrance(false);
    }

    public List<Button> getEntranceImages() {
        return this.entranceImages;
    }

    void setEntranceImages(List<Button> entranceButtons) {
        this.entranceImages = Collections.unmodifiableList(entranceButtons);
        if (entrance != null)
            updateEntrance(false);
    }

    public void updateEntrance(Map<HouseColor, Integer> entrance) {
        for (HouseColor color : HouseColor.values())
            this.entrance.replace(color, entrance.get(color));
        updateEntrance(true);
    }

    void setProfessors(Map<HouseColor, ImageView> professors) {
        this.professors = Collections.unmodifiableMap(professors);
    }

    public void updateProfessors(Map<HouseColor, Boolean> professors) {
        Platform.runLater(() -> {
            for (HouseColor color : HouseColor.values())
                this.professors.get(color).setVisible(professors.get(color));
        });
    }

    void setTowers(List<ImageView> towers) {
        this.towers = Collections.unmodifiableList(towers);
    }

    public void updateTowers(int towersNumber) {
        Platform.runLater(() -> {
            for (int index = 0; index < towers.size(); index++)
                towers.get(index).setVisible(index < towersNumber);
        });
    }

    void setWizard(WizardType wizard) {
        this.wizard = wizard;
    }

    private List<HouseColor> entranceToList() {
        List<HouseColor> list = new ArrayList<>();
        for (Map.Entry<HouseColor, Integer> entry : entrance.entrySet())
            for (int index = 0; index < entry.getValue(); index++)
                list.add(entry.getKey());
        return list;
    }

    private void updateDiningRoom(boolean safe) {
        if (safe)
            Platform.runLater(updateDiningRoom::get);
        else
            updateDiningRoom.get();
    }

    private void updateEntrance(boolean safe) {
        List<HouseColor> entranceStudents = entranceToList();
        if (safe)
            Platform.runLater(() -> updateEntrance.accept(entranceStudents));
        else
            updateEntrance.accept(entranceStudents);
    }

    public void enableEntranceButtons(boolean enable) {

        for (Button entranceButton : this.entranceImages) {
            if (enable) {
                entranceButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-border-radius: 50em;" +
                        "-fx-border-width: 1px;" +
                        "-fx-min-width: 25px;" +
                        "-fx-min-height: 25px;" +
                        "-fx-padding: 2px;" +
                        "-fx-border-color: #FCFFAD;" +
                        "-fx-background-color: radial-gradient(focus-distance 0% ,center 50% 50%, radius 99%, transparent, #FCFFAD);");
                entranceButton.setMouseTransparent(false);
            } else {
                entranceButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-max-width: 10px;" +
                        "-fx-max-height: 10px;" +
                        "-fx-padding: 0px;");
                entranceButton.setMouseTransparent(true);
            }
        }
    }

    public void enableDiningRoomButton(boolean enable) {
        try {
            Button diningRoomButton = (Button) pane.getChildrenUnmodifiable().get(1);
            diningRoomButton.setVisible(enable);
        } catch (Exception e) {
            Log.warning(e);
        }

    }

    public void enableAssistantButtons(boolean enable) {
        try {
            VBox vBox = (VBox) pane.getChildrenUnmodifiable().get(0);
            GridPane gPane = (GridPane) vBox.getChildren().get(2);
            gPane.setVisible(enable);
        } catch (Exception e) {
            Log.warning(e);
        }

    }

    public List<Button> getEntranceButtons() {
        return this.entranceImages;
    }
}
