package Participant;

import CustomNode.MyChoiceBox;
import CustomNode.MyConfirmationButton;
import Shared.AnswerInfo;
import Shared.QuestionInfo;
import SharedGuiElement.QuestionAndAnswerTable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.*;

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

    public void setProblemId (List<String> choice) {
        questionProblemChoice.setChoice(choice);
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

    public void setQuestionAndAnswer(Map<Integer, QuestionInfo> integerQuestionInfoMap) {
        List<Map<String, String>> res = new ArrayList<>();
        for (QuestionInfo info: integerQuestionInfoMap.values()) {
            Map<String, String> entry = new HashMap<>();
            entry.put("type", "question");
            entry.put("question_id", Integer.toString(info.getID()));
            entry.put("problem_id", info.getProblemID());
            entry.put("content", info.getContent());
            entry.put("time_stamp", Integer.toString(info.getTimeStamp()));
            res.add(entry);
            for (AnswerInfo answerInfo: info.getAnswers().values()) {
                Map<String, String> answerEntry = new HashMap<>();
                answerEntry.put("type", "answer");
                answerEntry.put("question_id", Integer.toString(info.getID()));
                answerEntry.put("problem_id", info.getProblemID());
                answerEntry.put("content", answerInfo.getContent());
                answerEntry.put("time_stamp", Integer.toString(answerInfo.getTimeStamp()));
                res.add(answerEntry);
            }
        }
        setQuestionAndAnswer(res);
    }
}
