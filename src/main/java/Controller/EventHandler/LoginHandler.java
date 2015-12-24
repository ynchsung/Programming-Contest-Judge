package Controller.EventHandler;

import Controller.Guest;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class LoginHandler extends EventHandler<Guest> {
    public LoginHandler(EventHandler<? super Guest> nextHandler) {
        super(nextHandler);
    }
    public void handle(Guest guest, JSONObject msg) {
        //TODO: Login
    }
}
