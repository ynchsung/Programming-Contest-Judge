package Controller.EventHandler;

import Controller.Client;
import Controller.Core;
import Controller.Judge;
import Controller.Team;
import Controller.DatabaseManager.QAManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class AnswerHandler extends EventHandler<Judge> {
    public AnswerHandler(EventHandler<? super Judge> nextHandler) {
        super(nextHandler);
    }

    private void sendAck(Judge judge, String questionID, long timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.append("msg_type", "answer");
            msg.append("status", "received");
            msg.append("question_id", questionID);
            msg.append("time_stamp", timeStamp);
            judge.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void forward(Client client, String questionID, String answer, long timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.append("msg_type", "answer");
            msg.append("question_id", questionID);
            msg.append("answer", answer);
            msg.append("time_stamp", timeStamp);
            client.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Judge judge, JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("answer")) {
                QAManager qaManager = new QAManager();
                String questionID = msg.getString("question_id");
                String teamID = msg.getString("team_id");
                String answer = msg.getString("answer");
                long timeStamp = System.currentTimeMillis() / 1000; /* TODO: get timer time */
                Map<String, String> store = new HashMap<String, String>();

                store.put("question_id", questionID);
                store.put("answer", answer);
                store.put("time_stamp", Long.toString(timeStamp));
                qaManager.addEntry(store);

                sendAck(judge, questionID, timeStamp);
                forward(Core.getInstance().getTeamByID(teamID), questionID, answer, timeStamp);
                for (Judge judge1: Core.getInstance().getAllJudge()) {
                    forward(judge1, questionID, answer, timeStamp);
                }
            }
            else doNext(judge, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
