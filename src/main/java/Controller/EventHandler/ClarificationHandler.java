package Controller.EventHandler;

import Controller.Client;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class ClarificationHandler extends EventHandler<Client> {
    public ClarificationHandler(EventHandler<? super Client> nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(Client client, JSONObject msg) {

    }
}
