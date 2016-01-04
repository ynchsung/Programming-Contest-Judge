package SharedGuiElement;

import SharedGuiElement.ConfirmationAlertBuilder;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Created by aalexx on 1/2/16.
 */
public class JudgeConfirmationAlert {
    private Alert alert;
    public static ButtonType autoJudge = new ButtonType("Auto");
    public static ButtonType manualJudge = new ButtonType("Manual");
    public static ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

    public JudgeConfirmationAlert() {
        ConfirmationAlertBuilder builder = ConfirmationAlertBuilder.create();
        builder.setTitle("Rejudge")
                .setHeaderText("Confirmation")
                .setContentText("Choose a way to judge the submission.");
        alert = builder.build();

        alert.getButtonTypes().setAll(autoJudge, manualJudge, cancel);
    }

    public Optional<ButtonType> showAndWait () {
        return alert.showAndWait();
    }

    public void setTitle (String title) {
        alert.setTitle(title);
    }

    public void setHeaderText (String text) {
        alert.setHeaderText(text);
    }

    public  void setContentText (String text) {
        alert.setContentText(text);
    }
}
