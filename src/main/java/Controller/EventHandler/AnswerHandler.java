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
import java.util.logging.ConsoleHandler;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class AnswerHandler extends EventHandler<Judge> {
    public AnswerHandler(EventHandler<? super Judge> nextHandler) {
        super(nextHandler);
    }

    private void sendAck(Judge judge, String questionID, String answerID, String timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("msg_type", "answer");
            msg.put("status", "received");
            msg.put("question_id", questionID);
            msg.put("answer_id", answerID);
            msg.put("time_stamp", timeStamp);
            judge.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void forward(Client client, String questionID, String answerID, String answer, String timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("msg_type", "answer");
            msg.put("question_id", questionID);
            msg.put("answer_id", answerID);
            msg.put("answer", answer);
            msg.put("time_stamp", timeStamp);
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
                String timeStamp = Integer.toString(Core.getInstance().getTimer().getCountedTime() / 60);
                Map<String, String> store = new HashMap<String, String>();

                store.put("type", "answer");
                store.put("question_id", questionID);
                store.put("answer", answer);
                store.put("time_stamp", timeStamp);
                String answerID = Integer.toString(qaManager.addEntry(store));

                sendAck(judge, questionID, answerID, timeStamp);
                forward(Core.getInstance().getTeamByID(teamID), questionID, answerID, answer, timeStamp);
                for (Judge judge1: Core.getInstance().getAllJudge()) {
                    forward(judge1, questionID, answerID, answer, timeStamp);
                }
            }
            else doNext(judge, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
