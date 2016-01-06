package Participant;

import SharedGuiElement.InformationAlertBuilder;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Created by aalexx on 1/3/16.
 */
public class NewResultAlert {
    Alert alert;
    public NewResultAlert(Stage owner) {
        InformationAlertBuilder builder = InformationAlertBuilder.create();
        alert = builder.setTitle("New Result")
                .setHeaderText("New judge result.")
                .setContentText("Remember to check it out in submission tab.")
                .build();
        ((Stage) alert.getDialogPane().getChildren().get(0).getScene().getWindow()).initOwner(owner);
    }

    public NewResultAlert() {
        InformationAlertBuilder builder = InformationAlertBuilder.create();
        alert = builder.setTitle("New Result")
                .setHeaderText("New judge result.")
                .setContentText("Remember to check it out in submission tab.")
                .build();
    }

    public void show () {
        alert.show();
    }
}
