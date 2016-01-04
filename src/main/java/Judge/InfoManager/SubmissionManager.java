package Judge.InfoManager;

import java.util.*;

public class SubmissionManager {
    private static Map<Integer, SubmissionInfo> infos;
    private static List<Observer> observers = new ArrayList<Observer>();

    static {
        infos = Collections.synchronizedMap(new HashMap<Integer, SubmissionInfo>());
    }

    public void addEntry(SubmissionInfo submission) {
        infos.put(submission.getID(), submission);
        notifyObservers();
    }

    public final Map<Integer, SubmissionInfo> getAll() {
        return infos;
    }

    public static void register(Observer observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
