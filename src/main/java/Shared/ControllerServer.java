package Shared;

import Participant.EventHandler.*;
import Shared.EventHandler.*;
import org.json.JSONException;
import org.json.JSONObject;

abstract public class ControllerServer {
    private Connection connection;
    private EventHandler eventHandler;

    public ControllerServer(Connection connection) {
        this.connection = connection;
    }

    protected void setEventHandler(EventHandler handler) {
        this.eventHandler = handler;
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
            msg.put("user_type", getUserType());
            msg.put("username", username);
            msg.put("password", password);
            send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    abstract protected String getUserType();

    public void logout() {
        if (this.connection != null) {
            this.connection.interrupt();
        }
        this.connection = null;
    }
}
