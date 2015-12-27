package Controller;

import Controller.EventHandler.*;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class Team extends Client {
    EventHandler<Team> eventHandler;

    public Team(Connection connection) {
        super(connection);
        this.eventHandler = new SubmissionHandler(new QuestionHandler(new ClarificationHandler(new SyncHandler(null))));
    }

    @Override
    public void handle(JSONObject msg) {

    }
}
