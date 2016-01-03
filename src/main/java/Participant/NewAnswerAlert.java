package Participant;

import SharedGuiElement.InformationAlertBuilder;
import javafx.scene.control.Alert;

/**
 * Created by aalexx on 1/3/16.
 */
public class NewAnswerAlert {
    Alert alert;
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
