package Controller.EventHandler;

import Controller.Judge;
import Controller.DatabaseManager.ProblemManager;

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
            content.append("input", inputData);
            content.append("output", outputData);
            content.append("judge_method", judgeMethod);
            content.append("time_limit", timeLimit);
            content.append("memory_limit", memoryLimit);
            content.append("testdata_time_stamp", testDataTimeStamp);
            msg.append("msg_type", "syncjudgedata");
            msg.append("problem_id", problemID);
            msg.append("content", content);
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

                forward(judge, problemID, ""/* input data */, ""/* output data */, ""/* judge_method */, ""/* time_limit */, ""/* memory_limit */, ""/*testdata_time_stamp*/);
            }
            else doNext(judge, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
