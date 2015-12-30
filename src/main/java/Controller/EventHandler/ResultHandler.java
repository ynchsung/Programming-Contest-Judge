package Controller.EventHandler;

import Controller.Core;
import Controller.Judge;
import Controller.Team;
import Controller.DatabaseManager.SubmissionManager;

import java.util.Map;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class ResultHandler extends EventHandler<Judge> {
    public ResultHandler(EventHandler<? super Judge> nextHandler) {
        super(nextHandler);
    }

    private void sendAck(Judge judge, String submissionID, long timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.append("msg_type", "result");
            msg.append("submission_id", submissionID);
            msg.append("status", "received");
            msg.append("time_stamp", timeStamp);
            judge.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendNak(Judge judge, long timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.append("msg_type", "result");
            msg.append("status", "redundant");
            msg.append("time_stamp", timeStamp);
            judge.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void forward(Team team, String submissionID, String result, String runTime, String memory, long timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.append("msg_type", "result");
            msg.append("submission_id", submissionID);
            msg.append("result", result);
            msg.append("run_time", runTime);
            msg.append("memory", memory);
            msg.append("time_stamp", timeStamp);
            team.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Judge judge, JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("result")) {
                SubmissionManager submissionManager = new SubmissionManager();
                String submissionID = msg.getString("submission_id");
                String result = msg.getString("result");
                String runTime = msg.getString("run_time");
                String memory = msg.getString("memory");
                String testdata_timestamp = msg.getString("testdata_timestamp");
                long timeStamp = System.currentTimeMillis() / 1000;
                if (true /*not appeared*/) {
                    Map<String, String> store = new HashMap<String, String>();
                    store.put("submission_id", submissionID);
                    store.put("result", result);
                    store.put("time_stamp", Long.toString(timeStamp));
                    store.put("testdata_timestamp", testdata_timestamp);
                    submissionManager.updateEntry(store);

                    sendAck(judge, submissionID, timeStamp);

                    Map<String, String> getSub = submissionManager.getSubmissionById(Integer.valueOf(submissionID));
                    String teamID = getSub.get("team_id");
                    Team team = Core.getInstance().getTeamByID(teamID);
                    forward(team, submissionID, result, runTime, memory, timeStamp);
                }
                else {
                    sendNak(judge, timeStamp);
                }
            }
            else doNext(judge, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
