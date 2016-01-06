package Judge.EventHandler;

import Shared.EventHandler.EventHandler;
import Shared.InfoManager.ProblemManager;

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
                String status = msg.getString("status");
                if (status.equals("OK")) {
                    JSONObject content = msg.getJSONObject("content");
                    String input = content.getString("input");
                    String output = content.getString("output");
                    String judgeMethod = content.getString("judge_method");
                    int timeLimit = Integer.valueOf(content.getString("time_limit"));
                    int memoryLimit = Integer.valueOf(content.getString("memory_limit"));
                    int testDataTimeStamp = Integer.valueOf(content.getString("testdata_time_stamp"));

                    problemManager.updateTestData(problemID, testDataTimeStamp, input, output, judgeMethod);
                }
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
