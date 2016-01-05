package Judge.EventHandler;

import Judge.JudgeCore;
import Shared.EventHandler.EventHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class ClarificationAckHandler extends EventHandler{
    public ClarificationAckHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            System.out.println(msg);
            if (msg.getString("msg_type").equals("clarification") && msg.has("status")) {
                JudgeCore.getInstance().ackClarification();
            } else {
                doNext(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
