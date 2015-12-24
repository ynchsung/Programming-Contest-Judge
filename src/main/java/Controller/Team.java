package Controller;

import Controller.EventHandler.EventHandler;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class Team extends Client {
    EventHandler<Team> eventHandler;

    public Team(Connection connection) {
        super(connection);
    }

    @Override
    public void handle(JSONObject msg) {

    }
}
