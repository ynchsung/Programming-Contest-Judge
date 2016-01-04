package Participant.InfoManager;

import Participant.AnswerInfo;
import Participant.QuestionInfo;

import java.util.*;

public class QAManager {
    private static Map<Integer, QuestionInfo> infos;
    private static List<Observer> observers = new ArrayList<Observer>();

    static {
        infos = Collections.synchronizedMap(new HashMap<Integer, QuestionInfo>());
    }

    public void addQuestion(QuestionInfo question) {
        infos.put(question.getID(), question);
        notifyObservers();
    }

    public void addAnswer(int question_id, AnswerInfo answerInfo) {
        QuestionInfo retr = infos.get(question_id);
        if (retr != null) {
            retr.addAnswer(answerInfo);
            notifyObservers();
        }
    }

    public final Map<Integer, QuestionInfo> getAll() {
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
