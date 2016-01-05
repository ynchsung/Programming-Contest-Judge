package Participant.EventHandler;

import Shared.EventHandler.EventHandler;
import Shared.InfoManager.SubmissionManager;
import Shared.InfoManager.QAManager;
import Shared.SubmissionInfo;
import Shared.QuestionInfo;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class SyncHandler extends EventHandler {
    public SyncHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    private void storeSyncSubmission(JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("submit")) {
                SubmissionManager submissionManager = new SubmissionManager();
                int submissionID = Integer.valueOf(msg.getString("submission_id"));
                String problemID = msg.getString("problem_id");
                String language = msg.getString("language");
                String sourceCode = msg.getString("source_code");
                int timeStamp = Integer.valueOf(msg.getString("time_stamp"));

                submissionManager.addEntry(new SubmissionInfo(submissionID, problemID, language, sourceCode, timeStamp));
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void storeSyncQuestion(JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("question")) {
                QAManager qaManager = new QAManager();
                int questionID = Integer.valueOf(msg.getString("question_id"));
                String problemID = msg.getString("problem_id");
                String teamID = msg.getString("team_id");
                String content = msg.getString("content");
                int timeStamp = Integer.valueOf(msg.getString("time_stamp"));

                qaManager.addQuestion(new QuestionInfo(questionID, problemID, teamID, content, timeStamp));
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("sync")) {
                ResultHandler resultHandler = new ResultHandler(null);
                AnswerHandler answerHandler = new AnswerHandler(null);
                ClarificationHandler clarificationHandler = new ClarificationHandler(null);

                JSONObject content = new JSONObject(msg.getString("content"));
                JSONArray submissions = content.getJSONArray("submission");
                JSONArray results = content.getJSONArray("result");
                JSONArray questions = content.getJSONArray("question");
                JSONArray answers = content.getJSONArray("answer");
                JSONArray clarifications = content.getJSONArray("clarification");

                for (int i = 0; i < submissions.length(); i++) {
                    if (!submissions.isNull(i)) {
                        storeSyncSubmission(submissions.getJSONObject(i));
                    }
                }
                for (int i = 0; i < results.length(); i++) {
                    if (!results.isNull(i)) {
                        resultHandler.handle(results.getJSONObject(i));
                    }
                }
                for (int i = 0; i < questions.length(); i++) {
                    if (!questions.isNull(i)) {
                        storeSyncQuestion(questions.getJSONObject(i));
                    }
                }
                for (int i = 0; i < answers.length(); i++) {
                    if (!answers.isNull(i)) {
                        answerHandler.handle(answers.getJSONObject(i));
                    }
                }
                for (int i = 0; i < clarifications.length(); i++) {
                    if (!clarifications.isNull(i)) {
                        clarificationHandler.handle(clarifications.getJSONObject(i));
                    }
                }
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
