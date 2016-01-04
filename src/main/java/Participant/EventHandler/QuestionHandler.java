package Participant.EventHandler;

import Participant.InfoManager.QAManager;
import Participant.QuestionInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class QuestionHandler extends EventHandler {
    public QuestionHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("question")) {
                QAManager qaManager = new QAManager();
                int questionID = Integer.valueOf(msg.getString("question_id"));
                String problemID = msg.getString("problem_id");
                String teamID = msg.getString("team_id");
                String content = msg.getString("content");
                int timeStamp = Integer.valueOf(msg.getString("time_stamp"));

                qaManager.addQuestion(new QuestionInfo(questionID, problemID, teamID, content, timeStamp));
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
