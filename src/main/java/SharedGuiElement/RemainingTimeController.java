package SharedGuiElement;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 * Created by aalexx on 12/27/15.
 */
public class RemainingTimeController {
    @FXML private Text remainingTime;

    public void setRemainingTime (int remainingSecond) {
        int second = remainingSecond%60;
        remainingSecond /= 60;
        int minute = remainingSecond%60;
        remainingSecond /= 60;
        int hour = remainingSecond;
        this.remainingTime.setText(String.format("%d:%d:%d", hour, minute, second));
    }
}
