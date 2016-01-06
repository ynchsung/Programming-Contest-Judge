package Participant.EventHandler;

import Participant.ParticipantCore;
import Shared.EventHandler.EventHandler;
import Shared.InfoManager.QAManager;
import Shared.InfoManager.SubmissionManager;
import Shared.QuestionInfo;
import Shared.SubmissionInfo;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2016/1/5.
 */
public class SubmissionAckHandler extends EventHandler{
    public SubmissionAckHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            if (msg.get("msg_type").equals("submit") && msg.has("status")) {
                JSONObject sentMsg = ParticipantCore.getInstance().ackSubmission();
                int submissionId = Integer.valueOf(msg.getString("submission_id"));
                String problemId = sentMsg.getString("problem_id");
                String sourceCode = sentMsg.getString("source_code");
                String language = sentMsg.getString("language");
                int timeStamp = Integer.valueOf(msg.getString("time_stamp"));
                SubmissionInfo submissionInfo = new SubmissionInfo(submissionId, problemId, language, sourceCode, timeStamp);
                (new SubmissionManager()).addEntry(submissionInfo);
            } else {
                doNext(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
