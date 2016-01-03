package Controller.EventHandler;

import Controller.Judge;
import Controller.DatabaseManager.ProblemManager;

import java.util.Map;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class SyncJudgeDataHandler extends EventHandler<Judge> {
    public SyncJudgeDataHandler(EventHandler<? super Judge> nextHandler) {
        super(nextHandler);
    }

    private void forward(Judge judge, String problemID, String inputData, String outputData, String judgeMethod,
                         String timeLimit, String memoryLimit, String testDataTimeStamp) {
        try {
            JSONObject msg = new JSONObject(), content = new JSONObject();
            content.put("input", inputData);
            content.put("output", outputData);
            content.put("judge_method", judgeMethod);
            content.put("time_limit", timeLimit);
            content.put("memory_limit", memoryLimit);
            content.put("testdata_time_stamp", testDataTimeStamp);
            msg.put("msg_type", "syncjudgedata");
            msg.put("problem_id", problemID);
            msg.put("content", content);
            judge.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Judge judge, JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("syncjudgedata")) {
                ProblemManager problemManager = new ProblemManager();
                String problemID = msg.getString("problem_id");
                Map<String, String> prob = problemManager.getProblemById(problemID);

                forward(judge, problemID, prob.get("input"), prob.get("output"), prob.get("special_judge"), prob.get("time_limit"), prob.get("memory_limit"), prob.get("time_stamp"));
            }
            else doNext(judge, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
