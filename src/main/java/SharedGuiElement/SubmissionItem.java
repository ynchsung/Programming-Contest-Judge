package SharedGuiElement;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by aalexx on 12/27/15.
 */
public class SubmissionItem {
    // Properties
    private SimpleStringProperty submissionId;
    private SimpleStringProperty problemId;
    private SimpleStringProperty teamId;
    private SimpleBooleanProperty sourceCode;
    private SimpleStringProperty submissionTimeStamp;
    private SimpleStringProperty result;
    private SimpleStringProperty resultTimeStamp;

    public SubmissionItem(String submissionId, String problemId, String teamId, String sourceCode,
                          String submissionTimeStamp, String result, String resultTimeStamp) {
        this.submissionId = new SimpleStringProperty(submissionId);
        this.problemId = new SimpleStringProperty(problemId);
        this.teamId = new SimpleStringProperty(teamId);
        if (sourceCode == null || sourceCode.length() == 0)
            this.sourceCode = new SimpleBooleanProperty(false);
        else
            this.sourceCode = new SimpleBooleanProperty(true);
        this.submissionTimeStamp = new SimpleStringProperty(submissionTimeStamp);
        this.result = new SimpleStringProperty(result);
        this.resultTimeStamp = new SimpleStringProperty(resultTimeStamp);
    }

    // submissionId
    public void setSubmissionId(String submissionId) {
        this.submissionId.set(submissionId);
    }
    public String getSubmissionId() {
        return submissionId.get();
    }
    public SimpleStringProperty submissionProperty () {
        return submissionId;
    }
    // problemId
    public void setProblemId(String problemId) {
        this.problemId.set(problemId);
    }
    public String getProblemId () {
        return problemId.get();
    }
    public SimpleStringProperty problemIdProperty () {
        return problemId;
    }
    // teamId
    public void setTeamId (String teamId) {
        this.teamId.set(teamId);
    }
    public String getTeamId () {
        return teamId.get();
    }
    public SimpleStringProperty teamIdProperty() {
        return teamId;
    }
    // sourceCode
    public void setSourceCode (boolean sourceCode) {
        this.sourceCode.set(sourceCode);
    }
    public boolean getSourceCode () {
        return sourceCode.get();
    }
    public SimpleBooleanProperty sourceCodeProperty () {
        return sourceCode;
    }
    // submissionTimestamp
    public void setSubmissionTimeStamp(String submissionTimestamp) {
        this.submissionTimeStamp.set(submissionTimestamp);
    }
    public String getSubmissionTimeStamp() {
        return submissionTimeStamp.get();
    }
    public SimpleStringProperty submissionTimeStampProperty() {
        return submissionTimeStamp;
    }
    // result
    public void setResult (String result) {
        this.result.set(result);
    }
    public String getResult () {
        return result.get();
    }
    public SimpleStringProperty resultProperty () {
        return result;
    }
    //resultTimestamp
    public void setResultTimeStamp (String resultTimestamp) {
        this.resultTimeStamp.set(resultTimestamp);
    }
    public String getResultTimeStamp () {
        return this.resultTimeStamp.get();
    }
    public SimpleStringProperty resultTimeStampProperty () {
        return resultTimeStamp;
    }
}
