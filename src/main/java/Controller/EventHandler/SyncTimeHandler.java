package Controller.EventHandler;

import Controller.Client;

import Controller.Core;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * Created by ynchsung on 1/1/16.
 */
public class SyncTimeHandler extends EventHandler<Client> {
    public SyncTimeHandler(EventHandler<? super Client> nextHandler) {
        super(nextHandler);
    }

    void forward(Client client, String time) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("msg_type", "sync_time");
            msg.put("remain_time", time);
            client.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Client client, JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("sync_time")) {
                forward(client, Integer.toString(Core.getInstance().getTimer().getCountedTime()));
            }
            else doNext(client, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
