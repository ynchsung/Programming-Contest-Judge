package Participant;

import SharedGuiElement.InformationAlertBuilder;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Created by aalexx on 1/2/16.
 */
public class NewClarificationAlert {
    Alert alert;
    public NewClarificationAlert(Stage owner) {
        InformationAlertBuilder builder = InformationAlertBuilder.create();
        alert = builder.setTitle("New Clarification")
                .setHeaderText("Hey, listen!!")
                .setContentText("Judges have something to say!!")
                .build();
        ((Stage) alert.getDialogPane().getChildren().get(0).getScene().getWindow()).initOwner(owner);
    }

    public NewClarificationAlert() {
        InformationAlertBuilder builder = InformationAlertBuilder.create();
        alert = builder.setTitle("New Clarification")
                .setHeaderText("Hey, listen!!")
                .setContentText("Judges have something to say!!")
                .build();
    }

    public void show () {
        alert.show();
    }
}
