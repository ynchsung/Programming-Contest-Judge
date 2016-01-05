package Participant.EventHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginResultHandler extends EventHandler {
    public interface LoginResultListener {
        void callback(boolean success);
    }
    private LoginResultListener loginResultListener;
    public LoginResultHandler(LoginResultListener loginResultListener, EventHandler nextHandler) {
        super(nextHandler);
        this.loginResultListener = loginResultListener;
    }

    @Override
    public void handle(JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("login")) {
                String status = msg.getString("status");
                if (status.equals("success")) {
                    loginResultListener.callback(true);
                }
                else {
                    loginResultListener.callback(false);
                }
            }
            else doNext(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
