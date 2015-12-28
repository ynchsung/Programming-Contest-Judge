package Controller.EventHandler;

import Controller.Core;
import Controller.Judge;
import Controller.Team;
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
                String submissionID = msg.getString("submission_id");
                String result = msg.getString("result");
                String runTime = msg.getString("run_time");
                String memory = msg.getString("memory");
                long timeStamp = System.currentTimeMillis() / 1000;
                if (true /*not appeared*/) {
                    //store to db
                    sendAck(judge, submissionID, timeStamp);
                    //query this submission info
                    String teamID = ""/*teamID*/;
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
