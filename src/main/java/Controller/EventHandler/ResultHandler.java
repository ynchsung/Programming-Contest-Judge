package Controller.EventHandler;

import Controller.Judge;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class ResultHandler extends EventHandler<Judge> {
    public ResultHandler(EventHandler<? super Judge> nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(Judge judge, JSONObject msg) {

    }
}
