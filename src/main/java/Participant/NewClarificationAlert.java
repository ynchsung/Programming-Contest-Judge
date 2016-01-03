package Participant;

import SharedGuiElement.InformationAlertBuilder;
import javafx.scene.control.Alert;

/**
 * Created by aalexx on 1/2/16.
 */
public class NewClarificationAlert {
    Alert alert;
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
