package Participant.EventHandler;

import Participant.InfoManager.SubmissionManager;

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
                String submissionID = msg.getString("submission_id");
                String result = msg.getString("result");
                String result_timeStamp = msg.getString("time_stamp");

                // TODO
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
