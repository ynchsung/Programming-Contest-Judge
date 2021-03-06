package SharedGuiElement;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by aalexx on 12/31/15.
 */
public class QuestionAndAnswerItem {
    private SimpleStringProperty id;
    private SimpleStringProperty type;
    private SimpleStringProperty problemId;
    private SimpleStringProperty content;
    private SimpleStringProperty timeStamp;

    public QuestionAndAnswerItem(String id, String type, String problemId, String content, String timeStamp) {
        this.id = new SimpleStringProperty(id);
        this.type = new SimpleStringProperty(type);
        this.problemId = new SimpleStringProperty(problemId);
        this.content = new SimpleStringProperty(content);
        this.timeStamp = new SimpleStringProperty(timeStamp);
    }

    //id
    public void setId (String id) {
        this.id.set(id);
    }
    public String getId () {
        return id.get();
    }
    public SimpleStringProperty idProperty () {
        return id;
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
