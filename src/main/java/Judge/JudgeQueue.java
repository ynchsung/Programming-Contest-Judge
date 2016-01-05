package Judge;

import Shared.InfoManager.ProblemManager;
import Shared.ProblemInfo;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class JudgeQueue {
    private Thread judgeQueueThread;
    private BlockingQueue<JudgeSubmissionTask> judgeQueue;

    class ProcessJudgeQueueThread extends Thread {
        public ProcessJudgeQueueThread() {
        }

        private void handleResult(int submissionID, String problemID, String result,
                                  int submitTimeStamp, int testDataTimeStamp) {
            try {
                JSONObject msg = new JSONObject();
                msg.put("msg_type", "result");
                msg.put("submission_id", String.valueOf(submissionID));
                msg.put("problem_id", problemID);
                msg.put("result", result);
                msg.put("submit_time_stamp", String.valueOf(submitTimeStamp));
                msg.put("testdata_time_stamp", String.valueOf(testDataTimeStamp));
                JudgeCore.getInstance().sendResult(msg);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            judgeQueue.clear();
            List<String> cppArg = new ArrayList<String>();
            cppArg.add("g++");
            cppArg.add("-O2");
            cppArg.add("-std=c++11");
            cppArg.add("--static");

            JudgeUnit cppTypeJudger = new JudgeUnit("cpp", JudgeCore.getInstance().getSandboxPath(), cppArg);
            try {
                while (true) {
                    JudgeSubmissionTask task = judgeQueue.take();
                    ProblemManager problemManager = new ProblemManager();
                    ProblemInfo pb = problemManager.getProblemById(task.getProblemID());
                    if (pb != null && pb.getTestDataTimeStamp() == task.getTestDataTimeStamp()) {
                        String result = cppTypeJudger.judge(task.getSourceCode(), pb.getTimeLimit(), pb.getMemoryLimit(),
                                pb.getInputPathName(), pb.getOutputPathName(), pb.getSpecialJudgePathName());

                        System.err.println(result);
                        handleResult(task.getSubmissionID(), result, task.getProblemID(),
                                    task.getSubmissionTimeStamp(), pb.getTestDataTimeStamp());
                    } else if (pb != null) {
                       try {
                           JSONObject syncDataMsg = new JSONObject();
                           syncDataMsg.put("msg_type", "syncjudgedata");
                           syncDataMsg.put("problem_id", task.getProblemID());
                           syncDataMsg.put("old_time_stamp", String.valueOf(pb.getTestDataTimeStamp()));
                           JudgeCore.getInstance().getControllerServer().send(syncDataMsg);
                           judgeQueue.put(task);
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                    }
                }
            }
            catch (InterruptedException e) {
            }
        }
    }

    public JudgeQueue() {
        this.judgeQueue = new LinkedBlockingQueue<JudgeSubmissionTask>();
    }

    public void judge(JudgeSubmissionTask task) {
        while (true) {
            try {
                this.judgeQueue.put(task);
                break;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        this.judgeQueueThread = new ProcessJudgeQueueThread();
        this.judgeQueueThread.start();
    }
}
