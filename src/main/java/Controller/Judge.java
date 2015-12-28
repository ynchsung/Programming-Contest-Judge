package Controller;

import Controller.EventHandler.*;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class Judge extends Client {
    EventHandler<Judge> eventHandler;

    public Judge(String id, Connection connection) {
        super(id, connection);
        this.eventHandler = new ResultHandler(new AnswerHandler(new SyncJudgeDataHandler(new ClarificationHandler(new SyncHandler(null)))));
    }

    @Override
    public void handle(JSONObject msg) {

    }
}
