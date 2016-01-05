package Participant.EventHandler;

import Shared.EventHandler.EventHandler;
import Shared.InfoManager.SubmissionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultHandler extends EventHandler {
    public ResultHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("result")) {
                SubmissionManager submissionManager = new SubmissionManager();
                int submissionID = Integer.valueOf(msg.getString("submission_id"));
                String result = msg.getString("result");
                int result_timeStamp = Integer.valueOf(msg.getString("time_stamp"));

                submissionManager.updateResult(submissionID, result, result_timeStamp);
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
