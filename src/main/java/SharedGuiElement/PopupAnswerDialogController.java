package SharedGuiElement;

import CustomNode.MyConfirmationButton;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by aalexx on 1/2/16.
 */
public class PopupAnswerDialogController implements Initializable {
    @FXML private TextArea questionTextArea;
    @FXML private TextArea answerTextArea;
    @FXML private MyConfirmationButton confirmationButton;
    private PopupAnswerDialog target;

    public void setQuestionTextArea (String question) {
        questionTextArea.setText(question);
    }

    public void registTarget(PopupAnswerDialog target) {
        this.target = target;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        answerTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(""))
                confirmationButton.setDisable(true);
            else
                confirmationButton.setDisable(false);
        });
        confirmationButton.setDisable(true);
        confirmationButton.setOkAction(event -> {
            target.setClickYes();
            target.closeStage();
        });
    }
    public String getAnswer () {
        return answerTextArea.getText();
    }
}
