package Judge;

import SharedGuiElement.ConfirmationAlertBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Created by aalexx on 1/3/16.
 */
public class ConfirmJudgeResultAlert {
    private Alert alert;
    private EventHandler<ActionEvent> confirmHandler = event -> {};
    private EventHandler<ActionEvent> cancelHandler = event -> {};
    public ConfirmJudgeResultAlert(String submissionId, String result) {
        ConfirmationAlertBuilder builder = ConfirmationAlertBuilder.create();
        alert = builder.setTitle("Confirm Judge Result")
                .setHeaderText(submissionId + " / " + result)
                .setContentText("Confirm to send result.")
                .build();
    }

    public void setOnConfirmAction (EventHandler<ActionEvent> handler) {
        this.confirmHandler = handler;
    }

    public void setOnCancelAction (EventHandler<ActionEvent> handler) {
        this.cancelHandler = handler;
    }

    public void show() {
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            confirmHandler.handle(new ActionEvent());
        } else {
            cancelHandler.handle(new ActionEvent());
        }
    }
}
