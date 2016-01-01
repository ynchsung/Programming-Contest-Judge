package Controller.EventHandler;

import Controller.Client;

import org.json.JSONObject;
import org.json.JSONException;

/**
 * Created by ynchsung on 1/1/16.
 */
public class SyncTimeHandler extends EventHandler<Client> {
    public SyncTimeHandler(EventHandler<? super Client> nextHandler) {
        super(nextHandler);
    }

    void forward(Client client, int time) {
        try {
            JSONObject msg = new JSONObject();
            msg.append("msg_type", "sync_time");
            msg.append("remain_time", time);
            client.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Client client, JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("sync_time")) {
                forward(client, 0 /* TODO: get timer remain time */);
            }
            else doNext(client, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
