package Participant.EventHandler;

import Participant.DatabaseManager.QAManager;

import java.util.Map;
import java.util.HashMap;
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
                String questionID = msg.getString("question_id");
                String teamID = msg.getString("team_id");
                String problemID = msg.getString("problem_id");
                String content = msg.getString("content");
                String timeStamp = msg.getString("time_stamp");
                Map<String, String> store = new HashMap<String, String>();

                store.put("type", "question");
                store.put("question_id", questionID);
                store.put("team_id", teamID);
                store.put("problem_id", problemID);
                store.put("content", content);
                store.put("time_stamp", timeStamp);

                qaManager.addEntry(store);
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
