package Controller;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by aalexx on 12/30/15.
 */
public class ClarificationItem {
    private SimpleStringProperty clarificationId;
    private SimpleStringProperty problemId;
    private SimpleStringProperty content;
    private SimpleStringProperty timeStamp;

    public ClarificationItem (String clarificationId, String problemId, String content, String timeStamp) {
        this.clarificationId = new SimpleStringProperty(clarificationId);
        this.problemId = new SimpleStringProperty(problemId);
        this.content = new SimpleStringProperty(content);
        this.timeStamp = new SimpleStringProperty(timeStamp);
    }

    // clarificationId
    public void setClarificationId (String clarificationId) {
        this.clarificationId.set(clarificationId);
    }
    public String getClarificationId () {
        return this.clarificationId.get();
    }
    public SimpleStringProperty clarificationProperty () {
        return clarificationId;
    }
    //problemId
    public void setProblemId (String problemId) {
        this.problemId.set(problemId);
    }
    public String getProblemId () {
        return problemId.get();
    }
    public SimpleStringProperty problemIdProperty () {
        return problemId;
    }
    //content
    public void setContent (String content) {
        this.content.set(content);
    }
    public String getContent () {
        return content.get();
    }
    public SimpleStringProperty contentProperty () {
        return content;
    }
    //timeStamp
    public void setTimeStamp (String timeStamp) {
        this.timeStamp.set(timeStamp);
    }
    public String getTimeStamp () {
        return timeStamp.get();
    }
    public SimpleStringProperty timeStampProperty () {
        return timeStamp;
    }
}
