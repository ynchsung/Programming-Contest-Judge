package Judge.EventHandler;

import Judge.DatabaseManager.ClarificationManager;

import java.util.Map;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class ClarificationHandler extends EventHandler {
    public ClarificationHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("clarification")) {
                ClarificationManager clarificationManager = new ClarificationManager();
                String clarificationID = msg.getString("clarification_id");
                String problemID = msg.getString("problem_id");
                String content = msg.getString("content");
                String timeStamp = msg.getString("time_stamp");
                Map<String, String> store = new HashMap<String, String>();

                store.put("clarification_id", clarificationID);
                store.put("problem_id", problemID);
                store.put("content", content);
                store.put("time_stamp", timeStamp);

                clarificationManager.addEntry(store);
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
