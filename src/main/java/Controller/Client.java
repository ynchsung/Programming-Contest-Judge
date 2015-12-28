package Controller;

import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public abstract class Client {
    protected String id;
    protected Connection connection;

    public Client(String id, Connection connection) {
        this.id = id;
        this.connection = connection;
    }

    public abstract void handle(JSONObject msg);

    public String getID() {
        return this.id;
    }

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

    public void login(Guest guest) {
        this.logout();
        guest.connection.setClient(this);
        this.connection = guest.connection;
        guest.logout();
    }

    public void logout() {
        if (this.connection != null) {
            this.connection.flushSendQueue();
            this.connection.interrupt();
        }
        this.connection = null;
    }
}
