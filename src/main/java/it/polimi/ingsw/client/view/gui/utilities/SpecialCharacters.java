package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.SpecialCharacter;
import it.polimi.ingsw.client.view.gui.CommandAssembler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

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

            GridPane gPane = Grids.specialCharacter(specialCharacter, specialCharacterContainer);
            gPane.setMouseTransparent(true);
            gPane.setVisible(true);
            gPane.setStyle(
                    "-fx-border-color: transparent;" +
                            "-fx-padding: 0px;" +
                            "-fx-background-color: transparent;" +
                            "-fx-min-width: 130px;" +
                            "-fx-min-height: 130px;"
            );
            gPane.setOnMouseClicked(mouseEvent -> assembler.managePaymentsSpecialCharacterSelection(idSpecialCharacter));
            group.getChildren().addAll(Images.specialCharacter(idSpecialCharacter), gPane);
        }
        return Collections.unmodifiableList(list);
    }
}
