package Judge.InfoManager;

import java.util.*;

public class ClarificationManager {
    private static Map<Integer, ClarificationInfo> infos;
    private static List<Observer> observers = new ArrayList<Observer>();

    public void addEntry(ClarificationInfo clarificationInfo) {
        infos.put(clarificationInfo.getID(), clarificationInfo);
        notifyObservers();
    }

    public final Map<Integer, ClarificationInfo> getAll() {
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
