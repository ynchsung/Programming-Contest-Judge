package Judge.EventHandler;

import Judge.DatabaseManager.QAManager;

import java.util.Map;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class AnswerHandler extends EventHandler {
    public AnswerHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("answer")) {
                QAManager qaManager = new QAManager();
                String questionID = msg.getString("question_id");
                String answer = msg.getString("answer");
                String timeStamp = msg.getString("time_stamp");
                Map<String, String> store = new HashMap<String, String>();

                store.put("type", "answer");
                store.put("question_id", questionID);
                store.put("answer", answer);
                store.put("time_stamp", timeStamp);
                qaManager.addEntry(store);
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
