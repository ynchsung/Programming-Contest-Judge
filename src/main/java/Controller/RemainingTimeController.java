package Controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 * Created by aalexx on 12/27/15.
 */
public class RemainingTimeController {
    @FXML private Text remainingTime;

    public void setRemainingTime (String remainingTime) {
        this.remainingTime.setText(remainingTime);
    }
}
