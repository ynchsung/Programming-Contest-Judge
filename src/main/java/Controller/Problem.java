package Controller;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class Problem {
    private final String id;
    private int time_limit;
    private int memory_limit;
    private long timestamp;

    public Problem(String id, int time_limit, int memory_limit, long timestamp) {
        this.id = id;
        this.time_limit = time_limit;
        this.memory_limit = memory_limit;
        this.timestamp = timestamp;
    }

    public int getTimeLimit() {
        return this.time_limit;
    }

    public int getMemoryLimit() {
        return this.memory_limit;
    }

    public long getTimestamp() {
        return this.timestamp;
    }
}
