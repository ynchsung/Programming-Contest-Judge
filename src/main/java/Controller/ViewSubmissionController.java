package Controller;

import CustomNode.MyChoiceBox;
import CustomNode.MyConfirmationButton;
import SharedGuiElement.SubmissionTableController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.util.Callback;

import java.util.List;
import java.util.Map;

/**
 * Created by aalexx on 12/27/15.
 */
public class ViewSubmissionController {
    @FXML private MyChoiceBox rejudgeProblemChoiceBox;
    @FXML private MyConfirmationButton rejudgeButton;
    @FXML private SubmissionTableController submissionTableController;

    public void setRejudgeProblemChoice (List<String> choice) {
        rejudgeProblemChoiceBox.setChoice(choice);
    }

    public void setRejudgeButtonOnAction (EventHandler<ActionEvent> action) {
        rejudgeButton.setOkAction(action);
    }

    public void setOpenCodeCallBack (Callback callBack) {
        submissionTableController.setOpenCodeCallBack(callBack);
    }

    public void setSubmissions (List<Map<String, String>> submissions) {
        submissionTableController.setSubmissions(submissions);
    }

    public Object getRejudgeProblemChoice () {
        return rejudgeProblemChoiceBox.getChoice();
    }

    public int getRejudgeProblemChoiceNumber () {
        return rejudgeProblemChoiceBox.getChoiceNumber();
    }
}
