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
            msg.put("status", "OK");
            msg.put("problem_id", problemID);
            msg.put("content", content);
            judge.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void forward(Judge judge, String problemID) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("msg_type", "syncjudgedata");
            msg.put("status", "up-to-date");
            msg.put("problem_id", problemID);
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
                int oldTimeStamp = Integer.valueOf(msg.getString("old_time_stamp"));
                Map<String, String> prob = problemManager.getProblemById(problemID);

                if (oldTimeStamp < Integer.valueOf(prob.get("time_stamp")))
                    forward(judge, problemID, prob.get("input"), prob.get("output"), prob.get("special_judge"), Integer.valueOf(Float.valueOf(prob.get("time_limit")).intValue()).toString(), prob.get("memory_limit"), prob.get("time_stamp"));
                else
                    forward(judge, problemID);
            }
            else doNext(judge, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
