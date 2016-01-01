package SharedGuiElement;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Created by aalexx on 12/27/15.
 */
public class OpenCodeController {
    @FXML private TextArea code;

    public void setCode (String code) {
        this.code.setText(code);
    }
}
