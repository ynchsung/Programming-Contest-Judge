package Shared;

import Judge.Problem;

public class ProblemInfo {
    private final String id;
    private int timeLimit;
    private int memoryLimit;
    private int testDataTimeStamp;
    private String inputPathName;
    private String outputPathName;
    private String specialJudgePathName;

    private final Object lock;

    public ProblemInfo(String id, int timeLimit, int memoryLimit, int testDataTimeStamp) {
        this(id, timeLimit, memoryLimit, testDataTimeStamp, "", "", "");
    }

    private ProblemInfo(String id, int timeLimit, int memoryLimit, int testDataTimeStamp, String inputPathName,
                        String outputPathName, String specialJudgePathName) {
        this.id = id;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.testDataTimeStamp = testDataTimeStamp;
        this.inputPathName = inputPathName;
        this.outputPathName = outputPathName;
        this.specialJudgePathName = specialJudgePathName;
        this.lock = new Object();
    }

    public String getID() {
        String ret;
        synchronized (lock) {
            ret = this.id;
        }
        return ret;
    }

    public void updateInfo(int timeLimit, int memoryLimit, int testDataTimeStamp, String inputPathName, String outputPathName, String specialJudgePathName) {
        synchronized (lock) {
            this.timeLimit = timeLimit;
            this.memoryLimit = memoryLimit;
            this.testDataTimeStamp = testDataTimeStamp;
            this.inputPathName = inputPathName;
            this.outputPathName = outputPathName;
            this.specialJudgePathName = specialJudgePathName;
        }
    }

    public int getTimeLimit() {
        synchronized (lock) {
            return this.timeLimit;
        }
    }

    public int getMemoryLimit() {
        synchronized (lock) {
            return this.memoryLimit;
        }
    }

    public int getTestDataTimeStamp() {
        synchronized (lock) {
            return this.testDataTimeStamp;
        }
    }

    public String getInputPathName() {
        synchronized (lock) {
            return this.inputPathName;
        }
    }

    public String getOutputPathName() {
        synchronized (lock) {
            return this.outputPathName;
        }
    }

    public String getSpecialJudgePathName() {
        synchronized (lock) {
            return this.specialJudgePathName;
        }
    }

    public ProblemInfo copy() {
        return new ProblemInfo(this.id, this.timeLimit, this.memoryLimit, this.testDataTimeStamp, this.inputPathName,
                            this.outputPathName, this.specialJudgePathName);
    }
}
