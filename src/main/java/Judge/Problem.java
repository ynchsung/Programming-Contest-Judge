package Judge;

public class Problem {
    private final String id;
    private int timeLimit;
    private int memoryLimit;
    private long testDataTimeStamp;

    public Problem(String id, int timeLimit, int memoryLimit, long testDataTimeStamp) {
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

    public long getTestDataTimeStamp() {
        return this.testDataTimeStamp;
    }
}
