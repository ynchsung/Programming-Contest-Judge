package Controller.EventHandler;

import Controller.*;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class LoginHandler extends EventHandler<Guest> {
    public LoginHandler(EventHandler<? super Guest> nextHandler) {
        super(nextHandler);
    }

    private void sendResponse(Client client, String status) {
        try {
            JSONObject msg = new JSONObject();
            msg.append("msg_type", "login");
            msg.append("status", status);
            client.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void handle(Guest guest, JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("login")) {
                //AccountManager accountManager = new AccountManager();
                String userType = msg.getString("user_type");
                String username = msg.getString("username");
                String password = msg.getString("password");
                if (userType.equals("judge")) {
                    if (true/*accountManager.authenticateJudge(username, password)*/) {
                        Judge judge = Core.getInstance().getJudgeByID(username);
                        judge.login(guest);
                        sendResponse(judge, "success");
                    }
                    else {
                        sendResponse(guest, "fail");
                    }
                }
                else if (userType.equals("participant")) {
                    if (true/*accountManager.authenticateJudge(username, password)*/) {
                        Team team = Core.getInstance().getTeamByID(username);
                        team.login(guest);
                        sendResponse(team, "success");
                    }
                    else {
                        sendResponse(guest, "fail");
                    }
                }
            }
            else doNext(guest, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
