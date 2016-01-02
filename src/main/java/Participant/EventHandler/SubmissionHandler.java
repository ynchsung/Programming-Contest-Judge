package Participant.EventHandler;

import Participant.DatabaseManager.SubmissionManager;

import java.util.Map;
import java.util.HashMap;
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
                Map<String, String> store = new HashMap<String, String>();

                store.put("submission_id", submissionID);
                store.put("problem_id", problemID);
                store.put("language", language);
                store.put("source_code", sourceCode);
                store.put("time_stamp", timeStamp);

                submissionManager.addEntry(store);

                // judge_submission(testdata_time_stamp);
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
