package Shared.InfoManager;

import Shared.ClarificationInfo;

import java.util.*;

public class ClarificationManager {
    private static Map<Integer, ClarificationInfo> infos;
    private static List<Observer> observers;
    private static final Object lock;

    static {
        infos = new HashMap<Integer, ClarificationInfo>();
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

    public void addEntry(ClarificationInfo clarificationInfo) {
        synchronized (lock) {
            infos.put(clarificationInfo.getID(), clarificationInfo);
            notifyObservers();
        }
    }

    public Map<Integer, ClarificationInfo> queryAll() {
        Map<Integer, ClarificationInfo> ret = new HashMap<Integer, ClarificationInfo>();
        synchronized (lock) {
            for (Map.Entry<Integer, ClarificationInfo> entry : infos.entrySet()) {
                ret.put(entry.getKey(), entry.getValue().copy());
            }
        }
        return ret;
    }
}
