package Controller.EventHandler;

import Controller.Team;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class QuestionHandler extends EventHandler<Team> {
    public QuestionHandler(EventHandler<? super Team> nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(Team team, JSONObject msg) {

    }
}