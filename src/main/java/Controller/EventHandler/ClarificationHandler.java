package Controller.EventHandler;

import Controller.Client;
import Controller.Core;
import Controller.Judge;
import Controller.Team;
import Controller.DatabaseManager.ClarificationManager;

import java.util.Map;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class ClarificationHandler extends EventHandler<Judge> {
    public ClarificationHandler(EventHandler<? super Client> nextHandler) {
        super(nextHandler);
    }

    private void sendAck(Judge judge, String clarificationID, String timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("msg_type", "clarification");
            msg.put("status", "received");
            msg.put("clarification_id", clarificationID);
            msg.put("time_stamp", timeStamp);
            judge.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void forward(Client client, String clarificationID, String problemID, String content, String timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("msg_type", "clarification");
            msg.put("clarification_id", clarificationID);
            msg.put("problem_id", problemID);
            msg.put("content", content);
            msg.put("time_stamp", timeStamp);
            client.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Judge judge, JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("clarification")) {
                ClarificationManager clarificationManager = new ClarificationManager();
                String problemID = msg.getString("problem_id");
                String content = msg.getString("content");
                String timeStamp = Integer.toString(Core.getInstance().getTimer().getCountedTime() / 60);
                Map<String, String> store = new HashMap<String, String>();

                store.put("problem_id", problemID);
                store.put("content", content);
                store.put("time_stamp", timeStamp);

                String cid = Integer.toString(clarificationManager.addEntry(store));

                sendAck(judge, problemID, timeStamp);
                for (Team team: Core.getInstance().getAllTeam()) {
                    forward(team, cid, problemID, content, timeStamp);
                }
                for (Judge judge1: Core.getInstance().getAllJudge()) {
                    forward(judge1, cid, problemID, content, timeStamp);
                }
            }
            else doNext(judge, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
