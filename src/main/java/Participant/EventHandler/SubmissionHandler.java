package Participant.EventHandler;

import Participant.InfoManager.SubmissionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class SubmissionHandler extends EventHandler {
    public SubmissionHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("submit")) {
                SubmissionManager submissionManager = new SubmissionManager();
                String submissionID = msg.getString("submission_id");
                String problemID = msg.getString("problem_id");
                String language = msg.getString("language");
                String sourceCode = msg.getString("source_code");
                String timeStamp = msg.getString("time_stamp");
                String testdata_time_stamp = msg.getString("testdata_time_stamp");

                // TODO
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
