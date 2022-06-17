package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.WizardType;
import it.polimi.ingsw.utilities.exceptions.IllegalActionException;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static it.polimi.ingsw.client.view.gui.utilities.CommandAssembler.manageEntranceSelection;

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

    BoardContainer() {
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
                entranceImages.get(index).setMouseTransparent(true);
                entranceImages.get(index).setOnAction(mouseEvent -> manageEntranceSelection(students.get(studentNumber)));
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
            }
            else
                pane.setStyle("-fx-background-color: rgba(255, 255, 255, 0%)");
        });
    }

    void setAssistant(ImageView assistant) {
        this.assistant = assistant;
    }

    public void setAssistant(Integer id) {
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

    public void setCoins(int coins) {
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

    public void addToDiningRoom(HouseColor houseColor) throws IllegalActionException {
        if (diningRoom.get(houseColor) == 10)
            throw new IllegalActionException("Already enough students of color " + houseColor.name().toLowerCase(Locale.ROOT));
        diningRoom.replace(houseColor, diningRoom.get(houseColor) + 1);
        updateDiningRoom(true);
    }

    public void removeFromDiningRoom(HouseColor houseColor) throws IllegalActionException {
        if (diningRoom.get(houseColor) == 0)
            throw new IllegalActionException("No more students of color " + houseColor.name().toLowerCase(Locale.ROOT));
        diningRoom.replace(houseColor, diningRoom.get(houseColor) - 1);
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

    public void addToEntrance(HouseColor houseColor) {
        entrance.replace(houseColor, entrance.get(houseColor) + 1);
        updateEntrance(true);
    }

    public void removeFromEntrance(HouseColor houseColor) throws IllegalActionException {
        if (entrance.get(houseColor) == 0)
            throw new IllegalActionException("No more students of color " + houseColor.name().toLowerCase(Locale.ROOT));
        entrance.replace(houseColor, entrance.get(houseColor) - 1);
        updateEntrance(true);
    }

    void setProfessors(Map<HouseColor, ImageView> professors) {
        this.professors = Collections.unmodifiableMap(professors);
    }

    public void addProfessor(HouseColor houseColor) {
        Platform.runLater(() -> professors.get(houseColor).setVisible(true));
    }

    public void removeProfessor(HouseColor houseColor) {
        Platform.runLater(() -> professors.get(houseColor).setVisible(false));
    }

    void setTowers(List<ImageView> towers) {
        this.towers = Collections.unmodifiableList(towers);
    }

    public void addTower() throws IllegalActionException {
        Optional<ImageView> tower = towers.stream().filter(imageView -> !imageView.isVisible()).findFirst();
        if (tower.isEmpty())
            throw new IllegalActionException("All the towers are already here.");
        else
            Platform.runLater(() -> tower.get().setVisible(true));
    }

    public void removeTower() throws IllegalActionException {
        List<ImageView> reverse = new ArrayList<>(towers);
        Collections.reverse(reverse);
        Optional<ImageView> tower = reverse.stream().filter(ImageView::isVisible).findFirst();
        if (tower.isEmpty())
            throw new IllegalActionException("There is no tower left.");
        else
            Platform.runLater(() -> tower.get().setVisible(false));
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

        for(Button entranceButton : this.entranceImages) {
            if(enable) {
                entranceButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-border-radius: 50em;" +
                        "-fx-border-width: 1px;" +
                        "-fx-min-width: 25px;" +
                        "-fx-min-height: 25px;" +
                        "-fx-padding: 2px;" +
                        "-fx-border-color: #FCFFAD;" +
                        "-fx-background-color: radial-gradient(focus-distance 0% ,center 50% 50%, radius 99%, transparent, #FCFFAD);");
            }
            else {
                entranceButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-max-width: 10px;" +
                        "-fx-max-height: 10px;" +
                        "-fx-padding: 0px;");
            }
        }
    }

    public void enableDiningRoomButton(boolean enable) {
        try{
            Button diningRoomButton = (Button) pane.getChildrenUnmodifiable().get(1);
            diningRoomButton.setVisible(enable);
            Log.debug("diningRoom set to " + enable);
        }catch(Exception e) {
            Log.warning(e);
        }

    }

    public void enableAssistantButtons(boolean enable) {
        try{
            VBox vBox = (VBox) pane.getChildrenUnmodifiable().get(0);
            GridPane gPane = (GridPane) vBox.getChildren().get(2);
            gPane.setVisible(enable);
            Log.debug("Assistants set to " + enable);
        }catch(Exception e){
            Log.warning(e);
        }

    }

    public List<Button> getEntranceButtons() {
        return this.entranceImages;
    }
}
