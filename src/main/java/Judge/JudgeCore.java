package Judge;

import org.json.JSONObject;

public class JudgeCore {
    static private JudgeCore sharedInstance = null;

    private ControllerServer controllerServer;
    private AckQueue sendResultQueue;
    private AckQueue sendClarificationQueue;
    private AckQueue sendAnswerQueue;

    private JudgeCore(ControllerServer controllerServer) {
        this.controllerServer = controllerServer;
        this.sendResultQueue = new AckQueue(this.controllerServer);
        this.sendClarificationQueue = new AckQueue(this.controllerServer);
        this.sendAnswerQueue = new AckQueue(this.controllerServer);
    }

    static public JudgeCore getInstance() {
        return sharedInstance;
    }

    static public void run(ControllerServer controllerServer) {
        if (sharedInstance != null)
            return;
        sharedInstance = new JudgeCore(controllerServer);
        sharedInstance.start();
    }

    private void start() {
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
}
