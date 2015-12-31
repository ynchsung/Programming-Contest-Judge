package Controller;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by aalexx on 12/31/15.
 */
public class QuestionAndAnswerItem {
    private SimpleStringProperty type;
    private SimpleStringProperty problemId;
    private SimpleStringProperty content;
    private SimpleStringProperty timeStamp;

    public QuestionAndAnswerItem(String type, String problemId, String content, String timeStamp) {
        this.type = new SimpleStringProperty(type);
        this.problemId = new SimpleStringProperty(problemId);
        this.content = new SimpleStringProperty(content);
        this.timeStamp = new SimpleStringProperty(timeStamp);
    }

    // type
    public void setType(String type) {
        this.type.set(type);
    }
    public String getType () {
        return this.type.get();
    }
    public SimpleStringProperty typeProperty () {
        return type;
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
