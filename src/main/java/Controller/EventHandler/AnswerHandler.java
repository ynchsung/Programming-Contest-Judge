package Controller.EventHandler;

import Controller.Core;
import Controller.Judge;
import Controller.Team;
import org.json.JSONException;
import org.json.JSONObject;

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

    private void forward(Team team, String questionID, String answer, long timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.append("msg_type", "answer");
            msg.append("question_id", questionID);
            msg.append("answer", answer);
            msg.append("time_stamp", timeStamp);
            team.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Judge judge, JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("answer")) {
                String questionID = msg.getString("question_id");
                String teamID = msg.getString("team_id");
                String answer = msg.getString("answer");
                long timeStamp = System.currentTimeMillis() / 1000;
                //store to DB
                sendAck(judge, questionID, timeStamp);
                forward(Core.getInstance().getTeamByID(teamID), questionID, answer, timeStamp);
            }
            else doNext(judge, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
