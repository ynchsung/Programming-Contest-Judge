package Controller;

import SharedGuiElement.QuestionAndAnswerTable;
import javafx.fxml.FXML;

import java.util.List;
import java.util.Map;

/**
 * Created by aalexx on 12/31/15.
 */
public class ViewQuestionAndAnswerController {
    @FXML private QuestionAndAnswerTable questionAndAnswerTable;

    public void setQuestionAndAnswer (List<Map<String, String>> questionAndAnswer) {
        questionAndAnswerTable.setQuestionAndAnswer(questionAndAnswer);
    }
}
