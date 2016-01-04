package Judge;

public class AnswerInfo {
    private final String content;
    private final int timeStamp;

    public AnswerInfo(String content, int timeStamp) {
        this.content = content;
        this.timeStamp = timeStamp;
    }

    public String getContent() {
        return this.content;
    }

    public int getTimeStamp() {
        return this.timeStamp;
    }
}
