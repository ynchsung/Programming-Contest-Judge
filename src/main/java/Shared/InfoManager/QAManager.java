package Shared.InfoManager;

import Shared.AnswerInfo;
import Shared.QuestionInfo;

import java.util.*;

public class QAManager {
    private static Map<Integer, QuestionInfo> infos;
    private static List<Observer> observers;
    private static final Object lock;

    static {
        infos = new HashMap<Integer, QuestionInfo>();
        observers = new ArrayList<Observer>();
        lock = new Object();
    }

    public static void register(Observer observer) {
        synchronized (lock) {
            System.out.println(observer);
            observers.add(observer);
        }
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void addQuestion(QuestionInfo question) {
        synchronized (lock) {
            if (!infos.containsKey(question.getID())) {
                infos.put(question.getID(), question);
                notifyObservers();
            }
        }
    }

    public void addAnswer(int question_id, AnswerInfo answerInfo) {
        synchronized (lock) {
            QuestionInfo g = infos.get(question_id);
            if (g != null && g.addAnswer(answerInfo)) {
                notifyObservers();
            }
        }
    }

    public QuestionInfo getQuestionById(Integer id) {
        return infos.get(id);
    }

    public Map<Integer, QuestionInfo> queryAll() {
        Map<Integer, QuestionInfo> ret = new HashMap<Integer, QuestionInfo>();
        synchronized (lock) {
            for (Map.Entry<Integer, QuestionInfo> entry : infos.entrySet()) {
                ret.put(entry.getKey(), entry.getValue().copy());
            }
        }
        return ret;
    }
}
