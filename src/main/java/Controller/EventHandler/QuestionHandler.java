package Controller.EventHandler;

import Controller.Core;
import Controller.Judge;
import Controller.Team;
import Controller.DatabaseManager.QAManager;

import java.util.Map;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class QuestionHandler extends EventHandler<Team> {
    public QuestionHandler(EventHandler<? super Team> nextHandler) {
        super(nextHandler);
    }

    private void sendAck(Team team, String questionID, String timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("msg_type", "question");
            msg.put("status", "success");
            msg.put("question_id", questionID);
            msg.put("time_stamp", timeStamp);
            team.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void forward(Judge judge, String teamID, String questionID, String problemID, String content, String timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("msg_type", "question");
            msg.put("team_id", teamID);
            msg.put("question_id", questionID);
            msg.put("problem_id", problemID);
            msg.put("content", content);
            msg.put("time_stamp", timeStamp);
            judge.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Team team, JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("question")) {
                QAManager qaManager = new QAManager();
                String problemID = msg.getString("problem_id");
                String teamID = team.getID();
                String content = msg.getString("content");
                String timeStamp = Integer.toString(Core.getInstance().getTimer().getCountedTime() / 60);
                Map<String, String> store = new HashMap<String, String>();

                store.put("type", "question");
                store.put("problem_id", problemID);
                store.put("team_id", teamID);
                store.put("content", content);
                store.put("time_stamp", timeStamp);
                String qid = Integer.toString(qaManager.addEntry(store));

                sendAck(team, qid, timeStamp);
                for (Judge judge: Core.getInstance().getAllJudge()) {
                    forward(judge, teamID, qid, problemID, content, timeStamp);
                }
            }
            else doNext(team, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
