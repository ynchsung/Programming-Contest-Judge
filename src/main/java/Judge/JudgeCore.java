package Judge;

import Shared.AckQueue;
import Shared.ContestTimer;
import Shared.InfoManager.QAManager;
import org.json.JSONException;
import org.json.JSONObject;

public class JudgeCore {
    static private JudgeCore sharedInstance = null;

    private JudgeControllerServer controllerServer;
    private AckQueue sendResultQueue;
    private AckQueue sendClarificationQueue;
    private AckQueue sendAnswerQueue;
    private ContestTimer timer;

    private JudgeCore() {
        timer = new ContestTimer(300*60);
    }

    public void setControllerServer(JudgeControllerServer controllerServer) {
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
        this.timer.start();
    }

    public void sendResult(JSONObject msg) {
        sendResultQueue.add(msg);
    }

    public void sendClarification(JSONObject msg) {
        sendClarificationQueue.add(msg);
    }

    public void sendAnswer(String questionId, String content) {
        JSONObject msg = new JSONObject();
        try {
            String teamId = (new QAManager()).getQuestionById(Integer.valueOf(questionId)).getTeamID();
            msg.put("msg_type", "answer");
            msg.put("question_id", questionId);
            msg.put("team_id", teamId);
            msg.put("answer", content);
            sendAnswerQueue.add(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ContestTimer getTimer() {
        return timer;
    }

    public void ackAnswer() {
        sendAnswerQueue.ackAndGetNowMsg();
    }
}
