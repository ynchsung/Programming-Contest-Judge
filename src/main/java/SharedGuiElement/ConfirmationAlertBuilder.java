package SharedGuiElement;

import javafx.scene.control.Alert;
import javafx.util.Builder;

/**a
 * Created by aalexx on 1/2/16.
 */
public class ConfirmationAlertBuilder implements Builder {
    Alert re = new Alert(Alert.AlertType.CONFIRMATION);
    public static ConfirmationAlertBuilder create () {
        return new ConfirmationAlertBuilder();
    }

    public ConfirmationAlertBuilder setTitle (String title) {
        re.setTitle(title);
        return this;
    }

    public ConfirmationAlertBuilder setHeaderText (String text) {
        re.setHeaderText(text);
        return this;
    }

    public ConfirmationAlertBuilder setContentText (String text) {
        re.setContentText(text);
        return this;
    }

    public Alert build() {
        return re;
    }
}
