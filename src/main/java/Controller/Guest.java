package Controller;

import Controller.EventHandler.*;
import Controller.EventHandler.LoginHandler;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class Guest extends Client {
    EventHandler<Guest> eventHandler;

    public Guest(Connection connection) {
        super("guest", connection);
        eventHandler = new LoginHandler(null);
    }

    @Override
    public void handle(JSONObject msg) {
        this.eventHandler.handle(this, msg);
    }

    @Override
    public void logout() {
        this.connection = null;
        return;
    }
}
