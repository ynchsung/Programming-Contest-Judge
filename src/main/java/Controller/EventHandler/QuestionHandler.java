package Controller.EventHandler;

import Controller.Core;
import Controller.Judge;
import Controller.Team;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class QuestionHandler extends EventHandler<Team> {
    public QuestionHandler(EventHandler<? super Team> nextHandler) {
        super(nextHandler);
    }

    private void sendAck(Team team, String questionID, long timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.append("msg_type", "question");
            msg.append("status", "success");
            msg.append("question_id", questionID);
            msg.append("time_stamp", timeStamp);
            team.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void forward(Judge judge, String teamID, String questionID, String problemID, String content, long timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.append("msg_type", "question");
            msg.append("team_id", teamID);
            msg.append("question_id", questionID);
            msg.append("problem_id", problemID);
            msg.append("content", content);
            msg.append("time_stamp", timeStamp);
            judge.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Team team, JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("question")) {
                String problemID = msg.getString("problem_id");
                String content = msg.getString("content");
                String teamID = team.getID();
                long timeStamp = System.currentTimeMillis() / 1000;
                //store to DB
                sendAck(team, /*ID*/"", timeStamp);
                for (Judge judge: Core.getInstance().getAllJudge()) {
                    forward(judge, teamID,  /*ID*/"", problemID, content, timeStamp);
                }
            }
            else doNext(team, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
