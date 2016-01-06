package Judge;

import Shared.AckQueue;
import Shared.ContestTimer;
import Shared.InfoManager.ProblemManager;
import Shared.InfoManager.QAManager;
import Shared.InfoManager.SubmissionManager;
import Shared.SubmissionInfo;
import org.json.JSONException;
import org.json.JSONObject;

public class JudgeCore {
    static private JudgeCore sharedInstance = null;

    private JudgeControllerServer controllerServer;
    private AckQueue sendResultQueue;
    private AckQueue sendClarificationQueue;
    private AckQueue sendAnswerQueue;
    private JudgeTaskProcessor judgeTaskProcessor;
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
        this.judgeTaskProcessor = new JudgeTaskProcessor();
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
        this.judgeTaskProcessor.start();
        this.sandboxPath = "judgebox/0/box";

        this.syncTime();
        this.syncProblemInfo();
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

    public void sendClarification(String problemId, String content) {
        JSONObject msg = new JSONObject();
        try {
            msg.put("msg_type", "clarification");
            msg.put("problem_id", problemId);
            msg.put("content", content);
            sendClarificationQueue.add(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void rejudgeSubmission(int submissionID) {
        SubmissionManager submissionManager = new SubmissionManager();
        SubmissionInfo submissionInfo = submissionManager.getSubmissionByID(submissionID);
        if (submissionInfo != null) {
            ProblemManager problemManager = new ProblemManager();
            this.judgeTaskProcessor.judge(new JudgeSubmissionTask(submissionInfo,
                        problemManager.getProblemById(submissionInfo.getProblemID()).getTestDataTimeStamp()));
        }
    }

    public JudgeTaskProcessor getJudgeTaskProcessor() {
        return this.judgeTaskProcessor;
    }

    public ContestTimer getTimer() {
        return timer;
    }

    public void rescheduleSubmission(String problemID) {
        this.judgeTaskProcessor.reschedule(problemID);
    }

    public JSONObject ackAnswer() {
        return sendAnswerQueue.ackAndGetNowMsg();
    }

    public JSONObject ackResult() {
        return sendResultQueue.ackAndGetNowMsg();
    }

    public JSONObject ackClarification() {
        return sendClarificationQueue.ackAndGetNowMsg();
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
