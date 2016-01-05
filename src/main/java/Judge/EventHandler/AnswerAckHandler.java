package Judge.EventHandler;

import Judge.JudgeCore;
import Participant.ParticipantCore;
import Shared.EventHandler.EventHandler;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2016/1/5.
 */
public class AnswerAckHandler extends EventHandler{
    public AnswerAckHandler(EventHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            System.out.println(msg);
            if (msg.get("msg_type").equals("answer") && msg.has("status")) {
                JudgeCore.getInstance().ackAnswer();
            } else {
                doNext(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
