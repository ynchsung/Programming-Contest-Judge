package Judge.EventHandler;

import Judge.JudgeCore;
import Shared.EventHandler.EventHandler;
import Shared.InfoManager.SubmissionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultAckHandler extends EventHandler{
    public ResultAckHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("result")) {
                JSONObject getMsg = JudgeCore.getInstance().ackResult();
                int submissionID = Integer.valueOf(msg.getString("submission_id"));
                int result_timeStamp = Integer.valueOf(msg.getString("time_stamp"));

                assert(submissionID == Integer.valueOf(getMsg.getString("submission_id")));

                SubmissionManager submissionManager = new SubmissionManager();
                submissionManager.updateResult(submissionID, getMsg.getString("result"), result_timeStamp);
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
