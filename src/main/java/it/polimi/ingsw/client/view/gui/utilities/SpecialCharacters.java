package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.SpecialCharacter;
import it.polimi.ingsw.client.view.gui.CommandAssembler;
import javafx.scene.Group;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpecialCharacters {

    private SpecialCharacters() {
    }

    // TODO: check for it in general
    public static List<SpecialCharacterContainer> get(List<SpecialCharacter> specialCharacters, CommandAssembler assembler) {
        SpecialCharacterContainer specialCharacterContainer;
        List<SpecialCharacterContainer> list = new ArrayList<>();

        for (SpecialCharacter specialCharacter : specialCharacters) {
            final int idSpecialCharacter = specialCharacter.getId();
            specialCharacterContainer = new SpecialCharacterContainer(idSpecialCharacter, assembler);
            list.add(specialCharacterContainer);

            Group group = new Group();
            specialCharacterContainer.setPane(group);

            Button specialCharacterButton = new Button("");
            // TODO: check for it
            specialCharacterButton.setStyle("-fx-border-color: #FCFFAD;" +
                    "-fx-border-width: 1px;" +
                    "-fx-border-radius: 50em;" +
                    "-fx-background-radius: 50em;" +
                    "-fx-background-color: radial-gradient(focus-distance 0%, center 50% 50%, radius 90%, transparent, #FCFFAD);" +
                    "-fx-min-width: 130px;" +
                    "-fx-min-height: 130px;");
            specialCharacterButton.setVisible(false);
            specialCharacterButton.setGraphic(Images.specialCharacter(idSpecialCharacter));
            specialCharacterButton.setOnAction(mouseEvent -> assembler.managePaymentsSpecialCharacterSelection(idSpecialCharacter));
            specialCharacterButton.setVisible(false);
            group.getChildren().addAll(Images.specialCharacter(idSpecialCharacter), Grids.specialCharacter(specialCharacter, specialCharacterContainer), specialCharacterButton);
        }
        return Collections.unmodifiableList(list);
    }
}
