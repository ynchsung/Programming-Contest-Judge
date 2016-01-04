package Shared;

public class ClarificationInfo {
    private final int id;
    private final String problem_id;
    private final String content;
    private final int timeStamp;

    public ClarificationInfo(int id, String problem_id, String content, int timeStamp) {
        this.id = id;
        this.problem_id = problem_id;
        this.content = content;
        this.timeStamp = timeStamp;
    }

    public int getID() {
        return this.id;
    }

    public String getProblemID() {
        return this.problem_id;
    }

    public String getContent() {
        return this.content;
    }

    public int getTimeStamp() {
        return this.timeStamp;
    }

    public ClarificationInfo copy() {
        return new ClarificationInfo(this.id, this.problem_id, this.content, this.timeStamp);
    }
}
