package Participant.EventHandler;

import Participant.DatabaseManager.ProblemManager;

import java.util.Map;
import java.util.HashMap;
import org.json.JSONObject;
import org.json.JSONException;

public class SyncJudgeDataHandler extends EventHandler {
    public SyncJudgeDataHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("syncjudgedata")) {
                ProblemManager problemManager = new ProblemManager();
                String problemID = msg.getString("problem_id");
                JSONObject content = new JSONObject(msg.get("content"));
                String input = content.getString("input");
                String output = content.getString("output");
                String judgeMethod = content.getString("judge_method");
                String timeLimit = content.getString("time_limit");
                String memoryLimit = content.getString("memory_limit");
                String testDataTimeStamp = content.getString("testdata_time_stamp");
                Map<String, String> store = new HashMap<String, String>();

                store.put("input", input);
                store.put("output", output);
                store.put("special_judge", judgeMethod);
                store.put("time_limit", timeLimit);
                store.put("memory_limit", memoryLimit);
                store.put("time_stamp", testDataTimeStamp);

                problemManager.updateEntry(store);
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
