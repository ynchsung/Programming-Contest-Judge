package Controller;

import SharedGuiElement.QuestionAndAnswerTableController;
import javafx.fxml.FXML;

import java.util.List;
import java.util.Map;

/**
 * Created by aalexx on 12/31/15.
 */
public class ViewQuestionAndAnswerController {
    @FXML private QuestionAndAnswerTableController questionAndAnswerTableController;

    public void setQuestionAndAnswer (List<Map<String, String>> questionAndAnswer) {
        questionAndAnswerTableController.setQuestionAndAnswer(questionAndAnswer);
    }
}