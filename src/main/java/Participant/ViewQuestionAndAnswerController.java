package Participant;

import CustomNode.MyChoiceBox;
import CustomNode.MyConfirmationButton;
import SharedGuiElement.QuestionAndAnswerTable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by aalexx on 1/3/16.
 */
public class ViewQuestionAndAnswerController implements Initializable {
    @FXML private MyConfirmationButton confirmAskQuestionButton;
    @FXML private MyChoiceBox questionProblemChoice;
    @FXML private TextArea askQuestionTextArea;
    @FXML private QuestionAndAnswerTable questionAndAnswerTable;

    public void setQuestionAndAnswer (List<Map<String, String>> questionAndAnswer) {
        questionAndAnswerTable.setQuestionAndAnswer(questionAndAnswer);
    }

    public Object getSelectedProblem () {
        return questionProblemChoice.getChoice();
    }

    public int getSelectedProblemNumber () {
        return questionProblemChoice.getChoiceNumber();
    }

    public String getAskQuestionTextArea () {
        return askQuestionTextArea.getText();
    }

    public void setConfirmNewQuestionAction (EventHandler<ActionEvent> handler) {
        confirmAskQuestionButton.setOkAction(handler);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        askQuestionTextArea.clear();
        confirmAskQuestionButton.setDisable(true);
        askQuestionTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(""))
                confirmAskQuestionButton.setDisable(true);
            else
                confirmAskQuestionButton.setDisable(false);
        });
    }
}
