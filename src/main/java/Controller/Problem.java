package Controller;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class Problem {
    private final String id;
    private int timeLimit;
    private int memoryLimit;
    private int testDataTimeStamp;

    public Problem(String id, int timeLimit, int memoryLimit, int testDataTimeStamp) {
        this.id = id;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.testDataTimeStamp = testDataTimeStamp;
    }

    public int getTimeLimit() {
        return this.timeLimit;
    }

    public int getMemoryLimit() {
        return this.memoryLimit;
    }

    public int getTestDataTimeStamp() {
        return this.testDataTimeStamp;
    }
}
