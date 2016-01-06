package Participant;

import SharedGuiElement.InformationAlertBuilder;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Created by aalexx on 1/3/16.
 */
public class NewAnswerAlert {
    Alert alert;
    public NewAnswerAlert(Stage owner) {
        InformationAlertBuilder builder = InformationAlertBuilder.create();
        alert = builder.setTitle("New Answer")
                .setHeaderText("Hey, listen!!")
                .setContentText("Judges have answered you question.")
                .build();
        ((Stage) alert.getDialogPane().getChildren().get(0).getScene().getWindow()).initOwner(owner);
    }

    public NewAnswerAlert() {
        InformationAlertBuilder builder = InformationAlertBuilder.create();
        alert = builder.setTitle("New Answer")
                .setHeaderText("Hey, listen!!")
                .setContentText("Judges have answered you question.")
                .build();
    }

    public void show () {
        alert.show();
    }
}
