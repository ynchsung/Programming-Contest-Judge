package Controller;

import org.json.JSONObject;

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
        if (this.connection != null) {
            try {
                this.connection.send(msg);
            }
            catch (InterruptedException e) {
            }
        }
    }

    public void logout() {
        this.connection = null;
    }
}
