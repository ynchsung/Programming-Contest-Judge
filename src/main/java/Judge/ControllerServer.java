package Judge;

import Judge.EventHandler.*;
import org.json.JSONException;
import org.json.JSONObject;

public class ControllerServer {
    private Connection connection;
    private EventHandler eventHandler;

    public ControllerServer(Connection connection, LoginResultHandler.LoginResultListener loginResultListener) {
        this.connection = connection;
        this.eventHandler = new LoginResultHandler(loginResultListener,
                new SubmissionHandler(
                new QuestionHandler(
                new AnswerHandler(
                new ClarificationHandler(
                new SyncHandler(
                new SyncProblemInfoHandler(
                new SyncJudgeDataHandler(
                new SyncTimeHandler(null)))))))));
    }

    public void handle(JSONObject msg) {
        this.eventHandler.handle(msg);
    }

    public boolean send(JSONObject msg) {
        if (this.connection != null) {
            try {
                this.connection.send(msg);
            }
            catch (InterruptedException e) {
                return false;
            }
            return true;
        }
        else
            return false;
    }

    public void login(String username, String password) {
        JSONObject msg = new JSONObject();
        try {
            msg.put("msg_type", "login");
            msg.put("user_type", "judge");
            msg.put("username", username);
            msg.put("password", password);
            send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        if (this.connection != null) {
            this.connection.interrupt();
        }
        this.connection = null;
    }
}
