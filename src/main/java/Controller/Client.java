package Controller;

import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public abstract class Client {
    protected Connection connection;

    public Client(Connection connection) {
        this.connection = connection;
    }

    public abstract void handle(JSONObject msg);

    public boolean send(JSONObject msg) {
        if (this.connection != null) {
            try {
                this.connection.send(msg);
            }
            catch (InterruptedException e) {
                return false;
            }
            return true;
        }
        else
            return false;
    }

    public void logout() throws InterruptedException {
        if (this.connection != null)
            this.connection.flushSendQueue();
        this.connection = null;
    }
}
