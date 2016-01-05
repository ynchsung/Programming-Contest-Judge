package Judge.EventHandler;

import Judge.JudgeCore;
import Shared.EventHandler.EventHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultAckHandler extends EventHandler{
    public ResultAckHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            System.out.println(msg);
            if (msg.getString("msg_type").equals("result") && msg.has("status")) {
                JudgeCore.getInstance().ackResult();
            } else {
                doNext(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
