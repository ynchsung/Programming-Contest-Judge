package Controller;

import CustomNode.MyChoiceBox;
import CustomNode.MyConfirmationButton;
import SharedGuiElement.SubmissionTable;
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
    @FXML private SubmissionTable submissionTable;

    public void setRejudgeProblemChoice (List<String> choice) {
        rejudgeProblemChoiceBox.setChoice(choice);
    }

    public void setRejudgeButtonOnAction (EventHandler<ActionEvent> action) {
        rejudgeButton.setOkAction(action);
    }

    public void setOpenCodeCallBack (Callback callBack) {
        submissionTable.setOpenCodeCallBack(callBack);
    }

    public void setSubmissions (List<Map<String, String>> submissions) {
        submissionTable.setSubmissions(submissions);
    }

    public Object getRejudgeProblemChoice () {
        return rejudgeProblemChoiceBox.getChoice();
    }

    public int getRejudgeProblemChoiceNumber () {
        return rejudgeProblemChoiceBox.getChoiceNumber();
    }
}
