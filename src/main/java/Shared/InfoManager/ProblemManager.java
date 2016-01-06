package Shared.InfoManager;

import Shared.ProblemInfo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.io.File;
import java.io.PrintWriter;

public class ProblemManager {
    private static Map<String, ProblemInfo> infos;
    private static List<Observer> observers;
    private static final Object lock;

    static {
        infos = new HashMap<String, ProblemInfo>();
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

    public void updateEntry(String problemID, int timeLimit, int memoryLimit) {
        synchronized (lock) {
            if (!infos.containsKey(problemID)) {
                infos.put(problemID, new ProblemInfo(problemID, timeLimit, memoryLimit, -1));
            }
            else {
                ProblemInfo pb = infos.get(problemID);
                pb.updateInfo(timeLimit, memoryLimit, pb.getTestDataTimeStamp(), pb.getInputPathName(),
                            pb.getOutputPathName(), pb.getSpecialJudgePathName());
            }
            notifyObservers();
        }
    }

    private void writeDataFile(String destPathName, String content) {
        try {
            PrintWriter writer = new PrintWriter(destPathName, "ascii");
            writer.print(content);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTestData(String problemID, int testDataTimeStamp, String input, String output, String specialJudge) {
        synchronized (lock) {
            if (infos.containsKey(problemID)) {
                ProblemInfo pb = infos.get(problemID);
                String inputPathName = String.format("Testdata/%s.in", problemID);
                String outputPathName = String.format("Testdata/%s.out", problemID);
                String specialJudgePathName = String.format("Testdata/%s_special_judge.cpp", problemID);

                writeDataFile(inputPathName, input);
                writeDataFile(outputPathName, output);
                writeDataFile(specialJudgePathName, specialJudge);

                pb.updateInfo(pb.getTimeLimit(), pb.getMemoryLimit(), testDataTimeStamp, inputPathName, outputPathName, specialJudgePathName);
            }
        }
    }

    public ProblemInfo getProblemById(String problemID) {
        synchronized (lock) {
            return infos.getOrDefault(problemID, null);
        }
    }

    public Map<String, ProblemInfo> queryAll() {
        Map<String, ProblemInfo> ret = new HashMap<String, ProblemInfo>();
        synchronized (lock) {
            for (Map.Entry<String, ProblemInfo> entry : infos.entrySet()) {
                ret.put(entry.getKey(), entry.getValue().copy());
            }
        }
        return ret;
    }

    public List<String> queryAllId() {
        List<String> ret;
        synchronized (lock) {
            ret = new ArrayList<String>(infos.keySet());
        }
        return ret;
    }
}
