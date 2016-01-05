package Controller;

import Controller.EventHandler.*;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class Team extends Client {
    private EventHandler<Team> eventHandler;

    public Team(String id, Connection connection) {
        super(id, connection);
        this.eventHandler = new SubmissionHandler(new QuestionHandler(new SyncHandler(new SyncProblemInfoHandler(new SyncTimeHandler(null)))));
    }

    @Override
    public void handle(JSONObject msg) {
        this.eventHandler.handle(this, msg);
    }
}
