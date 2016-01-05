package Judge.EventHandler;

import Shared.EventHandler.AnswerHandler;
import Shared.EventHandler.EventHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class SyncHandler extends EventHandler {
    public SyncHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("sync")) {
                QuestionHandler questionHandler = new QuestionHandler(null);
                AnswerHandler answerHandler = new AnswerHandler(null);
                ClarificationHandler clarificationHandler = new ClarificationHandler(null);

                JSONObject content = new JSONObject(msg.getString("content"));
                JSONArray questions = content.getJSONArray("question");
                JSONArray answers = content.getJSONArray("answer");
                JSONArray clarifications = content.getJSONArray("clarification");

                for (int i = 0; i < questions.length(); i++) {
                    if (!questions.isNull(i)) {
                        questionHandler.handle(questions.getJSONObject(i));
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
