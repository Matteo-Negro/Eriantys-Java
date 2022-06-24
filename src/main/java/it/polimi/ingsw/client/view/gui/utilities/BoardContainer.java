package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.Assistant;
import it.polimi.ingsw.client.view.gui.CommandAssembler;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.WizardType;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This class is a simple interface to manage a GUI board. Provides all the required methods to create and update it.
 */
public class BoardContainer {

    private final Supplier<Void> updateDiningRoom;
    private final Consumer<List<HouseColor>> updateEntrance;
    private final CommandAssembler assembler;
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
    private List<Assistant> hand;

    /**
     * Default class constructor.
     *
     * @param commandAssembler CommandAssembler for building the game messages to send to the server.
     */
    BoardContainer(CommandAssembler commandAssembler) {
        assembler = commandAssembler;
        hand = null;
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
                entranceImages.get(index).setOnMouseClicked(mouseEvent -> {
                    enableEntranceButtonExcept(studentNumber);
                    commandAssembler.manageEntranceSelection(students.get(studentNumber));
                });
                enableEntranceButtons(false);
            }
        };
        wizard = null;
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
     * Initializes the assistant image.
     *
     * @param assistant The ImageView of the assistant or wizard.
     */
    void setAssistant(ImageView assistant) {
        this.assistant = assistant;
    }

    /**
     * Initializes the player's hand with the available assistant cards.
     *
     * @param hand The hand to set.
     */
    void setHand(List<Assistant> hand) {
        this.hand = new ArrayList<>(hand);
    }

    /**
     * Updates the assistant.
     *
     * @param id The id of the assistant to show, or null for the wizard.
     */
    public void updateAssistant(Integer id) {
        Platform.runLater(() -> {
            if (id == null)
                assistant.setImage(Images.getWizardByType(wizard));
            else {
                assistant.setImage(Images.getAssistantById(id));

                VBox vBox = (VBox) pane.getChildrenUnmodifiable().get(0);
                if (this.hand != null) {
                    this.hand.removeIf(assistantCard -> assistantCard.getId() == id);
                    vBox.getChildren().remove(2);
                    vBox.getChildren().add(Various.assistantsPane(assembler, this.hand));
                }
            }
        });
    }

    /**
     * Sets the label for the coins.
     *
     * @param coins The label.
     */
    void setCoins(Label coins) {
        this.coins = coins;
    }

    /**
     * Updates the number of coins.
     *
     * @param coins The number of coins to display.
     */
    public void updateCoins(int coins) {
        if (this.coins != null && coins >= 0)
            Platform.runLater(() -> this.coins.setText(String.format("x%d", coins)));
    }

    /**
     * Sets the number of students in the dining room.
     *
     * @param diningRoom A Map with all the colors and their respective number of students.
     */
    void setDiningRoom(Map<HouseColor, Integer> diningRoom) {
        this.diningRoom = new EnumMap<>(diningRoom);
        if (diningRoomImages != null)
            updateDiningRoom(false);
    }

    /**
     * Sets the images of the students in the dining room.
     *
     * @param diningRoom A Map with all the colors and their respective list of students (ImageViews).
     */
    void setDiningRoomImages(Map<HouseColor, List<ImageView>> diningRoom) {
        this.diningRoomImages = Collections.unmodifiableMap(diningRoom);
        if (this.diningRoom != null)
            updateDiningRoom(false);
    }

    /**
     * Upodates the dining room with the new number of students.
     *
     * @param diningRoom A Map with all the colors and their respective number of students.
     */
    public void updateDiningRoom(Map<HouseColor, Integer> diningRoom) {
        for (HouseColor color : HouseColor.values())
            this.diningRoom.replace(color, diningRoom.get(color));
        updateDiningRoom(true);
    }

    /**
     * Sets the number of students in the entrance.
     *
     * @param entranceColors A Map with all the colors and their respective number of students.
     */
    void setEntrance(Map<HouseColor, Integer> entranceColors) {
        this.entrance = new EnumMap<>(entranceColors);
        if (entranceImages != null)
            updateEntrance(false);
    }

    /**
     * Sets all the entrance students.
     *
     * @param entranceButtons List of buttons with images.
     */
    void setEntranceImages(List<Button> entranceButtons) {
        this.entranceImages = Collections.unmodifiableList(entranceButtons);
        if (entrance != null)
            updateEntrance(false);
    }

    /**
     * Updates the entrance with the new students.
     *
     * @param entrance A Map with all the colors and their respective number of students.
     */
    public void updateEntrance(Map<HouseColor, Integer> entrance) {
        for (HouseColor color : HouseColor.values())
            this.entrance.replace(color, entrance.get(color));
        updateEntrance(true);
    }

    /**
     * Sets the images of the professors in the dining room.
     *
     * @param professors A Map with all the colors and their respective professor (ImageViews).
     */
    void setProfessors(Map<HouseColor, ImageView> professors) {
        this.professors = Collections.unmodifiableMap(professors);
    }

    /**
     * Updates the dining room with the new professors.
     *
     * @param professors A Map with a boolean for each color that is true if that professor is present.
     */
    public void updateProfessors(Map<HouseColor, Boolean> professors) {
        Platform.runLater(() -> {
            for (HouseColor color : HouseColor.values())
                this.professors.get(color).setVisible(professors.get(color));
        });
    }

    /**
     * Sets the images of all the towers in the garden.
     *
     * @param towers List of ImageViews containing the player.
     */
    void setTowers(List<ImageView> towers) {
        this.towers = Collections.unmodifiableList(towers);
    }

    /**
     * Updates all the towers in the garden.
     *
     * @param towersNumber Number of towers to display.
     */
    public void updateTowers(int towersNumber) {
        Platform.runLater(() -> {
            for (int index = 0; index < towers.size(); index++)
                towers.get(index).setVisible(index < towersNumber);
        });
    }

    /**
     * Sets the WizardType of the player.
     *
     * @param wizard WizardType of the player.
     */
    void setWizard(WizardType wizard) {
        this.wizard = wizard;
    }

    /**
     * Converts the entrance into an ordered list of students
     *
     * @return A list containing all the students in the entrance, ordered by color.
     */
    private List<HouseColor> entranceToList() {
        List<HouseColor> list = new ArrayList<>();
        for (Map.Entry<HouseColor, Integer> entry : entrance.entrySet())
            for (int index = 0; index < entry.getValue(); index++)
                list.add(entry.getKey());
        return list;
    }

    /**
     * Updates the dining room.
     *
     * @param safe true if it has to be run on the GUI thread.
     */
    private void updateDiningRoom(boolean safe) {
        if (safe)
            Platform.runLater(updateDiningRoom::get);
        else
            updateDiningRoom.get();
    }

    /**
     * Updates the entrance.
     *
     * @param safe true if it has to be run on the GUI thread.
     */
    private void updateEntrance(boolean safe) {
        List<HouseColor> entranceStudents = entranceToList();
        if (safe)
            Platform.runLater(() -> updateEntrance.accept(entranceStudents));
        else
            updateEntrance.accept(entranceStudents);
    }

    /**
     * Enables the entrance so that the player can interact with it.
     *
     * @param enable true if it has to be enabled, false otherwise.
     */
    public void enableEntranceButtons(boolean enable) {

        for (Button entranceButton : this.entranceImages) {
            entranceButton.setDisable(false);
            if (enable)
                entranceButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-border-radius: 50em;" +
                        "-fx-border-width: 1px;" +
                        "-fx-min-width: 25px;" +
                        "-fx-min-height: 25px;" +
                        "-fx-padding: 3px;" +
                        "-fx-border-color: #FCFFAD;" +
                        "-fx-background-color: radial-gradient(focus-distance 0% ,center 50% 50%, radius 99%, transparent, #FCFFAD);");
            else
                entranceButton.setStyle("-fx-background-radius: 50em;" +
                        "-fx-max-width: 10px;" +
                        "-fx-max-height: 10px;" +
                        "-fx-padding: 0px;");
            entranceButton.setMouseTransparent(!enable);
        }
    }

    /**
     * Enables all the entrance buttons except for the given one.
     *
     * @param index The index of the entrance button to disable.
     */
    private void enableEntranceButtonExcept(int index) {
        enableEntranceButtons(true);
        entranceImages.get(index).setDisable(true);
    }

    /**
     * Enables the dining room so that the player can interact with it.
     *
     * @param enable true if it has to be enabled, false otherwise.
     */
    public void enableDiningRoomButton(boolean enable) {
        try {
            Button diningRoomButton = (Button) pane.getChildrenUnmodifiable().get(1);
            diningRoomButton.setVisible(enable);
        } catch (Exception e) {
            Log.warning(e);
        }
    }

    /**
     * Enables the assistants so that the player can interact with it.
     *
     * @param enable true if it has to be enabled, false otherwise.
     */
    public void enableAssistantButtons(boolean enable) {
        try {
            FlowPane scrollPane = (FlowPane) ((VBox) pane.getChildrenUnmodifiable().get(0)).getChildren().get(2);
            scrollPane.setVisible(enable);
            scrollPane.setManaged(enable);
        } catch (Exception e) {
            Log.warning(e);
        }
    }
}
