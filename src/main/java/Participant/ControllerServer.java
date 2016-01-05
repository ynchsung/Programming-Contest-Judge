package Participant;

import Participant.EventHandler.*;
import org.json.JSONObject;

public class ControllerServer {
    private Connection connection;
    private EventHandler eventHandler;

    public ControllerServer(Connection connection) {
        this.connection = connection;
        this.eventHandler = new ResultHandler(
                new AnswerHandler(
                new ClarificationHandler(
                new SyncHandler(
                new SyncProblemInfoHandler(
                new SyncTimeHandler(null))))));
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

    public void logout() {
        if (this.connection != null) {
            this.connection.interrupt();
        }
        this.connection = null;
    }
}
