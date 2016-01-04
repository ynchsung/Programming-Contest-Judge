package Judge;

import SharedGuiElement.QuestionAndAnswerTable;
import javafx.fxml.FXML;
import javafx.util.Callback;

import java.util.List;
import java.util.Map;

/**
 * Created by aalexx on 1/2/16.
 */
public class ViewQuestionAndAnswerController {
    @FXML private QuestionAndAnswerTable questionAndAnswerTable;

    public void setQuestionAndAnswer (List<Map<String, String>> questionAndAnswer) {
        questionAndAnswerTable.setQuestionAndAnswer(questionAndAnswer);
    }

    public void setAnswerQuestionCallBack (Callback callback) {
        questionAndAnswerTable.setAnswerQuestionCallBack(callback);
    }
}
