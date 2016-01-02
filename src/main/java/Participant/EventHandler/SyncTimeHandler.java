package Participant.EventHandler;

import org.json.JSONObject;
import org.json.JSONException;

public class SyncTimeHandler extends EventHandler {
    public SyncTimeHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("sync_time")) {
                // refresh time
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
