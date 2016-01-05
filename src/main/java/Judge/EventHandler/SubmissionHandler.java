package Judge.EventHandler;

import Shared.EventHandler.EventHandler;
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
                // judge_submission(testdata_time_stamp);
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
