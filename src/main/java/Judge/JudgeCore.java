package Judge;

import Shared.AckQueue;
import Shared.ContestTimer;
import org.json.JSONObject;

public class JudgeCore {
    static private JudgeCore sharedInstance = null;

    private ControllerServer controllerServer;
    private AckQueue sendResultQueue;
    private AckQueue sendClarificationQueue;
    private AckQueue sendAnswerQueue;
    private ContestTimer timer;

    public JudgeCore() {
        timer = new ContestTimer(300*60);
    }

    public void setControllerServer(ControllerServer controllerServer) {
        this.controllerServer = controllerServer;
        this.sendResultQueue = new AckQueue(this.controllerServer);
        this.sendClarificationQueue = new AckQueue(this.controllerServer);
        this.sendAnswerQueue = new AckQueue(this.controllerServer);
    }

    static public JudgeCore getInstance() {
        if (sharedInstance == null) {
            synchronized (JudgeCore.class) {
                if (sharedInstance == null) {
                    sharedInstance = new JudgeCore();
                }
            }
        }
        return sharedInstance;
    }

    public void start() {
        this.sendResultQueue.start();
        this.sendClarificationQueue.start();
        this.sendAnswerQueue.start();
    }

    public void sendResult(JSONObject msg) {
        sendResultQueue.add(msg);
    }

    public void sendClrification(JSONObject msg) {
        sendClarificationQueue.add(msg);
    }

    public void sendAnswer(JSONObject msg) {
        sendAnswerQueue.add(msg);
    }

    public ContestTimer getTimer() {
        return timer;
    }
}
