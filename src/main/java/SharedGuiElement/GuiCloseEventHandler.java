package SharedGuiElement;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.util.Optional;

/**
 * Created by aalexx on 1/7/16.
 */
public class GuiCloseEventHandler {
    private EventHandler<WindowEvent> handler = event -> {};
    private Stage stage;
    private Event event;

    public GuiCloseEventHandler(Stage stage, Event event) {
        this.stage = stage;
        this.event = event;
    }

    public void setOncloseAction (EventHandler<WindowEvent> handler) {
        this.handler = handler;
    }
    public void onClose () {
        ConfirmationAlertBuilder builder = new ConfirmationAlertBuilder();
        Alert alert = builder.setTitle("Confirmation")
                        .setHeaderText("Closing")
                        .setContentText("Confirm to close.")
                        .build();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            handler.handle(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        } else {
            event.consume();
        }
    }

}
