package Controller.EventHandler;

import Controller.Client;
import Controller.Core;
import Controller.Team;
import Controller.DatabaseManager.SubmissionManager;
import Controller.DatabaseManager.QAManager;
import Controller.DatabaseManager.ClarificationManager;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class SyncHandler extends EventHandler<Client> {
    public SyncHandler(EventHandler<? super Client> nextHandler) {
        super(nextHandler);
    }

    void forward(Client client, String timeStamp, JSONObject content) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("msg_type", "sync");
            msg.put("time_stamp", timeStamp);
            msg.put("content", content);
            client.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Client client, JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("sync")) {
                String nowTime = Integer.toString(Core.getInstance().getTimer().getCountedTime() / 60);
                int timeStamp = Integer.valueOf(msg.getString("time_stamp"));
                JSONObject fwd = new JSONObject();
                JSONObject content = new JSONObject();
                JSONArray jsonSubmissions = new JSONArray();
                JSONArray jsonResults = new JSONArray();
                JSONArray jsonQuestions = new JSONArray();
                JSONArray jsonAnswers = new JSONArray();
                JSONArray jsonClarifications = new JSONArray();

                if (client.getClass() == Team.class) {
                    SubmissionManager submissionManager = new SubmissionManager();
                    for (Map<String, String> submit: submissionManager.syncSubmission(client.getID(), timeStamp)) {
                        JSONObject jsonSub = new JSONObject();
                        jsonSub.put("msg_type", "submit");
                        jsonSub.put("submission_id", submit.get("submission_id"));
                        jsonSub.put("problem_id", submit.get("problem_id"));
                        jsonSub.put("language", submit.get("language"));
                        jsonSub.put("source_code", "NULL");
                        jsonSub.put("time_stamp", submit.get("submission_time_stamp"));
                        jsonSubmissions.put(jsonSub);
                    }
                    for (Map<String, String> result: submissionManager.syncResult(client.getID(), timeStamp)) {
                        JSONObject jsonRes = new JSONObject();
                        jsonRes.put("msg_type", "result");
                        jsonRes.put("submission_id", result.get("submission_id"));
                        jsonRes.put("result", result.get("result"));
                        jsonRes.put("submit_time_stamp", result.get("submission_time_stamp"));
                        jsonRes.put("time_stamp", result.get("result_time_stamp"));
                        jsonResults.put(jsonRes);
                    }
                }

                String qid;
                QAManager qaManager = new QAManager();
                if (client.getClass() == Team.class)
                    qid = client.getID();
                else
                    qid = "";
                for (Map<String, String> question: qaManager.syncQuestion(qid, timeStamp)) {
                    JSONObject jsonQ = new JSONObject();
                    jsonQ.put("msg_type", "question");
                    jsonQ.put("team_id", question.get("team_id"));
                    jsonQ.put("question_id", question.get("question_id"));
                    jsonQ.put("problem_id", question.get("problem_id"));
                    jsonQ.put("content", question.get("content"));
                    jsonQ.put("time_stamp", question.get("time_stamp"));
                    jsonQuestions.put(jsonQ);
                }
                for (Map<String, String> answer: qaManager.syncAnswer(qid, timeStamp)) {
                    JSONObject jsonA = new JSONObject();
                    jsonA.put("msg_type", "answer");
                    jsonA.put("team_id", answer.get("team_id"));
                    jsonA.put("question_id", answer.get("question_id"));
                    jsonA.put("answer_id", answer.get("answer_id"));
                    jsonA.put("answer", answer.get("answer"));
                    jsonA.put("time_stamp", answer.get("time_stamp"));
                    jsonAnswers.put(jsonA);
                }

                ClarificationManager clarificationManager = new ClarificationManager();
                for (Map<String, String> clar: clarificationManager.sync(timeStamp)) {
                    JSONObject jsonC = new JSONObject();
                    jsonC.put("msg_type", "clarification");
                    jsonC.put("clarification_id", clar.get("clarification_id"));
                    jsonC.put("problem_id", clar.get("problem_id"));
                    jsonC.put("content", clar.get("content"));
                    jsonC.put("time_stamp", clar.get("time_stamp"));
                    jsonClarifications.put(jsonC);
                }

                content.put("submission", jsonSubmissions);
                content.put("result", jsonResults);
                content.put("question", jsonQuestions);
                content.put("answer", jsonAnswers);
                content.put("clarification", jsonClarifications);

                forward(client, nowTime, content);
            }
            else doNext(client, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
