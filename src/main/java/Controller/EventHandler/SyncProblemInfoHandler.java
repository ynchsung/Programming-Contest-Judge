package Controller.EventHandler;

import Controller.Client;
import Controller.Core;
import Shared.ProblemInfo;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class SyncProblemInfoHandler extends EventHandler<Client> {
    public SyncProblemInfoHandler(EventHandler<? super Client> nextHandler) {
        super(nextHandler);
    }

    private void forward(Client client, Map<String, ProblemInfo> pInfo) {
        try {
            JSONObject msg = new JSONObject();
            JSONArray content = new JSONArray();
            for (Map.Entry<String, ProblemInfo> entry : pInfo.entrySet()) {
                JSONObject p = new JSONObject();
                p.put("problem_id", entry.getValue().getID());
                p.put("time_limit", String.valueOf(entry.getValue().getTimeLimit()));
                p.put("memory_limit", String.valueOf(entry.getValue().getMemoryLimit()));
                content.put(p);
            }
            msg.put("msg_type", "sync_problem_info");
            msg.put("content", content);
            client.send(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Client client, JSONObject msg) {
        try {
            if (msg.getString("msg_type").equals("sync_problem_info")) {
                Map<String, ProblemInfo> pInfo = Core.getInstance().getProblems();
                forward(client, pInfo);
            }
            else doNext(client, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
