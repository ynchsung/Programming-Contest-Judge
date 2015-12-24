package Controller;

import Controller.EventHandler.EventHandler;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by tenyoku on 2015/12/24.
 */
public abstract class Client {
    private Connection connection;

    public Client(Connection connection) {
        this.connection = connection;
    }

    public abstract void handle(JSONObject msg);

    public void send(JSONObject msg) {
        try {
            this.connection.send(msg);
        }
        catch (IOException e) {
        }
    }

    public void logout() {
        this.connection = null;
    }
}
