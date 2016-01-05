package Judge.EventHandler;

import Judge.JudgeCore;

import Shared.EventHandler.EventHandler;
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
                int time = msg.getInt("time");
                JudgeCore.getInstance().getTimer().setSecondCounted(time);
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
