package Controller.EventHandler;

import Controller.Client;
import Controller.DatabaseManager.ClarificationManager;
import Controller.Team;
import Controller.Judge;
import Controller.DatabaseManager.QAManager;
import Controller.DatabaseManager.SubmissionManager;

import java.util.Map;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class SyncHandler extends EventHandler<Client> {
    public SyncHandler(EventHandler<? super Client> nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(Client client, JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("sync")) {
            }
            else doNext(client, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
