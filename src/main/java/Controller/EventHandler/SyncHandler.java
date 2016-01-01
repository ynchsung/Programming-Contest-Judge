package Controller.EventHandler;

import Controller.Client;
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

    void forward(Client client, int timeStamp, JSONObject content) {
        try {
            JSONObject msg = new JSONObject();
            msg.append("msg_type", "sync");
            msg.append("time_stamp", timeStamp);
            msg.append("content", content);
            client.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Client client, JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("sync")) {
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
                        jsonSub.append("msg_type", "submit");
                        jsonSub.append("submission_id", submit.get("submission_id"));
                        jsonSub.append("problem_id", submit.get("problem_id"));
                        jsonSub.append("language", submit.get("language"));
                        jsonSub.append("time_stamp", submit.get("submission_time_stamp"));
                        jsonSubmissions.put(jsonSub);
                    }
                    for (Map<String, String> result: submissionManager.syncResult(client.getID(), timeStamp)) {
                        JSONObject jsonRes = new JSONObject();
                        jsonRes.append("msg_type", "result");
                        jsonRes.append("submission_id", result.get("submission_id"));
                        jsonRes.append("result", result.get("result"));
                        jsonRes.append("submit_time_stamp", result.get("submission_time_stamp"));
                        jsonRes.append("time_stamp", result.get("result_time_stamp"));
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
                    jsonQ.append("msg_type", "question");
                    jsonQ.append("team_id", question.get("team_id"));
                    jsonQ.append("question_id", question.get("question_id"));
                    jsonQ.append("problem_id", question.get("problem_id"));
                    jsonQ.append("content", question.get("content"));
                    jsonQ.append("time_stamp", question.get("time_stamp"));
                    jsonQuestions.put(jsonQ);
                }
                for (Map<String, String> answer: qaManager.syncAnswer(qid, timeStamp)) {
                    JSONObject jsonA = new JSONObject();
                    jsonA.append("msg_type", "answer");
                    jsonA.append("team_id", answer.get("team_id"));
                    jsonA.append("question_id", answer.get("question_id"));
                    jsonA.append("answer", answer.get("answer"));
                    jsonA.append("time_stamp", answer.get("time_stamp"));
                    jsonAnswers.put(jsonA);
                }

                ClarificationManager clarificationManager = new ClarificationManager();
                for (Map<String, String> clar: clarificationManager.sync(timeStamp)) {
                    JSONObject jsonC = new JSONObject();
                    jsonC.append("msg_type", "clarification");
                    jsonC.append("clarification_id", clar.get("clarification_id"));
                    jsonC.append("problem_id", clar.get("problem_id"));
                    jsonC.append("content", clar.get("content"));
                    jsonC.append("time_stamp", clar.get("time_stamp"));
                    jsonClarifications.put(jsonC);
                }

                content.append("submission", jsonSubmissions);
                content.append("result", jsonResults);
                content.append("question", jsonQuestions);
                content.append("answer", jsonAnswers);
                content.append("clarification", jsonClarifications);

                forward(client, 0 /* TODO: get timer time */, content);
            }
            else doNext(client, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
