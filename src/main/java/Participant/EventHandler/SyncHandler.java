package Participant.EventHandler;

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
                // store sync event
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
