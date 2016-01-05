package Participant.EventHandler;

import Shared.EventHandler.EventHandler;
import Shared.InfoManager.ProblemManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class SyncProblemInfoHandler extends EventHandler {
    public SyncProblemInfoHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("sync_problem_info")) {
                ProblemManager problemManager = new ProblemManager();

                JSONArray content = new JSONArray(msg.getString("content"));
                for (int i = 0; i < content.length(); i++) {
                    if (!content.isNull(i)) {
                        JSONObject p = content.getJSONObject(i);
                        problemManager.updateEntry(p.getString("problem_id"), Integer.valueOf(p.getString("time_limit")), Integer.valueOf(p.getString("memory_limit")));
                    }
                }
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
