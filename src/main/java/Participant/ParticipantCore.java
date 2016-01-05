package Participant;

import Shared.AckQueue;
import Shared.ContestTimer;
import org.json.JSONException;
import org.json.JSONObject;

public class ParticipantCore {
    static private ParticipantCore sharedInstance = null;

    private ParticipantControllerServer controllerServer;
    private ContestTimer timer;
    private AckQueue submitQueue;
    private AckQueue questionQueue;

    private ParticipantCore() {
        timer = new ContestTimer(300*60);
    }

    public void setControllerServer(ParticipantControllerServer controllerServer) {
        this.controllerServer = controllerServer;
        submitQueue = new AckQueue(controllerServer);
        questionQueue = new AckQueue(controllerServer);
    }

    static public ParticipantCore getInstance() {
        if (sharedInstance == null) {
            synchronized (ParticipantCore.class) {
                if (sharedInstance == null) {
                    sharedInstance = new ParticipantCore();
                }
            }
        }
        return sharedInstance;
    }

    public void start() {
        questionQueue.start();
        submitQueue.start();
    }

    public ContestTimer getTimer() {
        return timer;
    }

    public void sendQuestion(String problemID, String content) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("msg_type", "question");
            msg.put("problem_id", problemID);
            msg.put("content", content);
            questionQueue.add(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject ackQuestion() {
        return questionQueue.ackAndGetNowMsg();
    }
}
