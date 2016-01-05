package Participant.EventHandler;

import Participant.ParticipantCore;
import Shared.EventHandler.EventHandler;
import Shared.InfoManager.QAManager;
import Shared.QuestionInfo;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2016/1/5.
 */
public class QuestionAckHandler extends EventHandler{
    public QuestionAckHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            if (msg.get("msg_type").equals("question") && msg.has("status")) {
                JSONObject sentMsg = ParticipantCore.getInstance().ackQuestion();
                int questionId = Integer.valueOf(msg.getString("question_id"));
                String problemId = sentMsg.getString("problem_id");
                String content = sentMsg.getString("content");
                int timeStamp = Integer.valueOf(msg.getString("time_stamp"));
                QuestionInfo questionInfo = new QuestionInfo(questionId, problemId, "teamID", content, timeStamp);
                (new QAManager()).addQuestion(questionInfo);
            } else {
                doNext(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
