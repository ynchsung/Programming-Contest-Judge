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

    private void sendAck(Judge judge, String submissionID, String timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("msg_type", "result");
            msg.put("submission_id", submissionID);
            msg.put("status", "received");
            msg.put("time_stamp", timeStamp);
            judge.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendNak(Judge judge, String timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("msg_type", "result");
            msg.put("status", "redundant");
            msg.put("time_stamp", timeStamp);
            judge.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void forward(Team team, String submissionID, String result, String submit_timeStamp, String result_timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("msg_type", "result");
            msg.put("submission_id", submissionID);
            msg.put("result", result);
            msg.put("submit_time_stamp", submit_timeStamp);
            msg.put("time_stamp", result_timeStamp);
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
                String result_timeStamp = Integer.toString(Core.getInstance().getTimer().getCountedTime() / 60);
                Map<String, String> store = new HashMap<String, String>();
                store.put("submission_id", submissionID);
                store.put("result", result);
                store.put("time_stamp", result_timeStamp);
                store.put("testdata_time_stamp", testdata_timeStamp);
                boolean flag = submissionManager.updateEntry(store);

                if (flag) {
                    sendAck(judge, submissionID, result_timeStamp);

                    Map<String, String> getSub = submissionManager.getSubmissionById(Integer.valueOf(submissionID));
                    String teamID = getSub.get("team_id");
                    String submit_timeStamp = getSub.get("submission_time_stamp");
                    Team team = Core.getInstance().getTeamByID(teamID);
                    forward(team, submissionID, result, submit_timeStamp, result_timeStamp);
                }
                else
                    sendNak(judge, result_timeStamp);
            }
            else doNext(judge, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
