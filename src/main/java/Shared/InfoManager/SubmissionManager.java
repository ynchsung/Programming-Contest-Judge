package Shared.InfoManager;

import Shared.SubmissionInfo;

import java.util.*;

public class SubmissionManager {
    private static Map<Integer, SubmissionInfo> infos;
    private static List<Observer> observers;
    private static final Object lock;

    static {
        infos = new HashMap<Integer, SubmissionInfo>();
        observers = new ArrayList<Observer>();
        lock = new Object();
    }

    public static void register(Observer observer) {
        synchronized (lock) {
            observers.add(observer);
        }
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void addEntry(SubmissionInfo submission) {
        synchronized (lock) {
            infos.put(submission.getID(), submission);
            notifyObservers();
        }
    }

    public void updateResult(int submission_id, String result, int resultTimeStamp) {
        synchronized (lock) {
            SubmissionInfo submissionInfo = infos.get(submission_id);
            if (submissionInfo != null) {
                submissionInfo.setResult(result);
                submissionInfo.setResultTimeStamp(resultTimeStamp);
                notifyObservers();
            }
        }
    }

    public String queryCode(int submission_id) {
        String ret = "Submission not found";
        synchronized (lock) {
            final SubmissionInfo submissionInfo = infos.get(submission_id);
            if (submissionInfo != null)
                ret = submissionInfo.getSourceCode();
        }
        return ret;
    }

    public Map<Integer, SubmissionInfo> queryAll() {
        Map<Integer, SubmissionInfo> ret = new HashMap<Integer, SubmissionInfo>();
        synchronized (lock) {
            for (Map.Entry<Integer, SubmissionInfo> entry : infos.entrySet()) {
                ret.put(entry.getKey(), entry.getValue().copy());
            }
        }
        return ret;
    }
}
