package Controller.EventHandler;

import Controller.Client;
import Controller.Core;
import Controller.Judge;
import Controller.Team;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class ClarificationHandler extends EventHandler<Judge> {
    public ClarificationHandler(EventHandler<? super Client> nextHandler) {
        super(nextHandler);
    }

    private void sendAck(Judge judge, String clarificationID, long timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.append("msg_type", "clarification");
            msg.append("status", "received");
            msg.append("clarification_id", clarificationID);
            msg.append("time_stamp", timeStamp);
            judge.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void forward(Team team, String clarificationID, String problemID, String content, long timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.append("msg_type", "clarification");
            msg.append("clarification_id", clarificationID);
            msg.append("problem_id", problemID);
            msg.append("content", content);
            msg.append("time_stamp", timeStamp);
            team.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Judge judge, JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("clarification")) {
                String problemID = msg.getString("problem_id");
                String content = msg.getString("content");
                long timeStamp = System.currentTimeMillis() / 1000;
                //store to DB
                sendAck(judge, problemID, timeStamp);
                for(Team team: Core.getInstance().getAllTeam()) {
                    forward(team, /*ID*/"", problemID, content, timeStamp);
                }
            }
            else doNext(judge, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
