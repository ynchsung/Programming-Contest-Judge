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
    private JudgeQueue judgeQueue;
    private ContestTimer timer;

    private String sandboxPath;

    private JudgeCore() {
        timer = new ContestTimer(300*60);
    }

    public void setControllerServer(JudgeControllerServer controllerServer) {
        this.controllerServer = controllerServer;
        this.sendResultQueue = new AckQueue(this.controllerServer);
        this.sendClarificationQueue = new AckQueue(this.controllerServer);
        this.sendAnswerQueue = new AckQueue(this.controllerServer);
        this.judgeQueue = new JudgeQueue();
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
        this.judgeQueue.start();
        this.sandboxPath = "judgebox/box/0/box";
        // TODO: init sandbox

        this.syncTime();
        this.syncProblemInfo();
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
        }
        this.syncEvent(0);
    }

    public JudgeControllerServer getControllerServer() {
        return this.controllerServer;
    }

    public String getSandboxPath() {
        return this.sandboxPath;
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

    public JudgeQueue getJudgeQueue() {
        return this.judgeQueue;
    }

    public ContestTimer getTimer() {
        return timer;
    }

    public void ackAnswer() {
        sendAnswerQueue.ackAndGetNowMsg();
    }

    public void ackResult() {
        sendResultQueue.ackAndGetNowMsg();
    }

    public void ackClarification() {
        sendClarificationQueue.ackAndGetNowMsg();
    }

    public void syncTime() {
        try {
            JSONObject msg = new JSONObject();
            msg.put("msg_type", "sync_time");
            this.controllerServer.send(msg);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void syncProblemInfo() {
        try {
            JSONObject msg = new JSONObject();
            msg.put("msg_type", "sync_problem_info");
            this.controllerServer.send(msg);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void syncEvent(int timeStamp) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("msg_type", "sync");
            msg.put("time_stamp", String.valueOf(timeStamp));
            this.controllerServer.send(msg);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
