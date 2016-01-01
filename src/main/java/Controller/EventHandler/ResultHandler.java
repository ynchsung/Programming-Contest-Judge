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

    private void forward(Team team, String submissionID, String result, String submit_timeStamp, String result_timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.append("msg_type", "result");
            msg.append("submission_id", submissionID);
            msg.append("result", result);
            msg.append("submit_time_stamp", submit_timeStamp);
            msg.append("time_stamp", result_timeStamp);
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
                String testdata_timeStamp = msg.getString("testdata_time_stamp");
                long result_timeStamp = System.currentTimeMillis() / 1000; /* TODO: get timer time */
                if (true /*not appeared*/) {
                    Map<String, String> store = new HashMap<String, String>();
                    store.put("submission_id", submissionID);
                    store.put("result", result);
                    store.put("time_stamp", Long.toString(result_timeStamp));
                    store.put("testdata_time_stamp", testdata_timeStamp);
                    submissionManager.updateEntry(store);

                    sendAck(judge, submissionID, result_timeStamp);

                    Map<String, String> getSub = submissionManager.getSubmissionById(Integer.valueOf(submissionID));
                    String teamID = getSub.get("team_id");
                    String submit_timeStamp = getSub.get("submission_time_stamp");
                    Team team = Core.getInstance().getTeamByID(teamID);
                    forward(team, submissionID, result, submit_timeStamp, String.valueOf(result_timeStamp));
                }
                else {
                    sendNak(judge, result_timeStamp);
                }
            }
            else doNext(judge, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
