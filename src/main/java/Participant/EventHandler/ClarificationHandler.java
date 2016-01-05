package Participant.EventHandler;

import Shared.InfoManager.ClarificationManager;
import Shared.ClarificationInfo;

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
                int clarificationID = Integer.valueOf(msg.getString("clarification_id"));
                String problemID = msg.getString("problem_id");
                String content = msg.getString("content");
                int timeStamp = Integer.valueOf(msg.getString("time_stamp"));

                clarificationManager.addEntry(new ClarificationInfo(clarificationID, problemID, content, timeStamp));
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
