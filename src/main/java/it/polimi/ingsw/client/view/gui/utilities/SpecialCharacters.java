package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.SpecialCharacter;
import it.polimi.ingsw.client.view.gui.CommandAssembler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Parses the model to obtain the special characters.
 */
public class SpecialCharacters {

    private SpecialCharacters() {
    }

    /**
     * Returns all the special characters.
     *
     * @param specialCharacters The List of Special Characters to display.
     * @param assembler         CommandAssembler for generating the commands to send to the server.
     * @return A List of SpecialCharacterContainers to display.
     */
    public static List<SpecialCharacterContainer> get(List<SpecialCharacter> specialCharacters, CommandAssembler assembler) {
        SpecialCharacterContainer specialCharacterContainer;
        List<SpecialCharacterContainer> list = new ArrayList<>();

        for (SpecialCharacter specialCharacter : specialCharacters) {
            final int idSpecialCharacter = specialCharacter.getId();
            specialCharacterContainer = new SpecialCharacterContainer(idSpecialCharacter, assembler);

            list.add(specialCharacterContainer);

            Group group = new Group();
            specialCharacterContainer.setPane(group);

            GridPane gPane = Grids.specialCharacter(specialCharacter, specialCharacterContainer);
            ImageView coin = Images.coin();
            coin.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 10, 0.5, 0.0, 1);");
            gPane.add(coin, 1, 0);
            if (specialCharacter.getStudents() != null || specialCharacter.getAvailableBans() != null || specialCharacter.getId() == 10) {
                gPane.setStyle("-fx-background-color: radial-gradient(focus-distance 0%, center 50% 60%, radius 99%, white, transparent);");
            }
            gPane.setVisible(true);

            Button characterButton = new Button();
            characterButton.setStyle("-fx-border-color: #FCFFAD;" +
                    "-fx-border-width: 2px;" +
                    "-fx-background-color: radial-gradient(focus-distance 0%, center 50% 50%, radius 99%, transparent, #FCFFAD);" +
                    "-fx-padding: 0px;" +
                    "-fx-min-width: 261px;" +
                    "-fx-min-height: 384px;"
            );
            characterButton.setVisible(false);
            characterButton.setOnMouseClicked(mouseEvent -> assembler.managePaymentsSpecialCharacterSelection(idSpecialCharacter));
            group.getChildren().addAll(Images.specialCharacter(idSpecialCharacter), gPane, characterButton);
            specialCharacterContainer.setExtraPrice(specialCharacter.isAlreadyPaid(), coin);
        }
        return Collections.unmodifiableList(list);
    }
}
