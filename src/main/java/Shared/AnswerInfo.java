package Shared;

public class AnswerInfo {
    private final int id;
    private final String content;
    private final int timeStamp;

    public AnswerInfo(int id, String content, int timeStamp) {
        this.id = id;
        this.content = content;
        this.timeStamp = timeStamp;
    }

    public int getID() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }

    public int getTimeStamp() {
        return this.timeStamp;
    }

    public AnswerInfo copy() {
        return new AnswerInfo(this.id, this.content, this.timeStamp);
    }
}
