package SharedGuiElement;

import javafx.scene.control.Alert;
import javafx.util.Builder;

/**
 * Created by aalexx on 1/2/16.
 */
public class InformationAlertBuilder implements Builder {
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public static InformationAlertBuilder create () {
        return new InformationAlertBuilder();
    }

    public InformationAlertBuilder setTitle (String title) {
        alert.setTitle(title);
        return this;
    }

    public InformationAlertBuilder setHeaderText (String text) {
        alert.setHeaderText(text);
        return this;
    }

    public InformationAlertBuilder setContentText (String text) {
        alert.setContentText(text);
        return this;
    }

    public Alert build () {
        return alert;
    }
}
