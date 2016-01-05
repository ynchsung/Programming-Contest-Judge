package Judge;

import Shared.AnswerInfo;
import Shared.QuestionInfo;
import SharedGuiElement.QuestionAndAnswerTable;
import javafx.fxml.FXML;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.HashMap;
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

    public void setQuestionAndAnswer (Map<Integer, QuestionInfo> questionAndAnswer) {
        List<Map<String, String>> res = new ArrayList<>();
        for (QuestionInfo info: questionAndAnswer.values()) {
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

    public void setAnswerQuestionCallBack (Callback callback) {
        questionAndAnswerTable.setAnswerQuestionCallBack(callback);
    }
}
