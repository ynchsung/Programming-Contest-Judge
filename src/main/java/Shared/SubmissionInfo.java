package Shared;

public class SubmissionInfo {
    private final int id;
    private final String problem_id;
    private final String language;
    private final String sourceCode;
    private final int submitTimeStamp;

    private String result;
    private int resultTimeStamp;

    public SubmissionInfo(int id, String problem_id, String language, String sourceCode, int submitTimeStamp, String result, int resultTimeStamp) {
        this.id = id;
        this.problem_id = problem_id;
        this.language = language;
        this.sourceCode = sourceCode;
        this.submitTimeStamp = submitTimeStamp;
        this.result = result;
        this.resultTimeStamp = resultTimeStamp;
    }

    public SubmissionInfo(int id, String problem_id, String language, String sourceCode, int submitTimeStamp) {
        this(id, problem_id, language, sourceCode, submitTimeStamp, "", -1);
    }

    public int getID() {
        return this.id;
    }

    public String getProblemID() {
        return this.problem_id;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getSourceCode() {
        return this.sourceCode;
    }

    public int getSubmitTimeStamp() {
        return this.submitTimeStamp;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return this.result;
    }

    public void setResultTimeStamp(int resultTimeStamp) {
        this.resultTimeStamp = resultTimeStamp;
    }

    public int getResultTimeStamp() {
        return this.resultTimeStamp;
    }

    public SubmissionInfo copy() {
        return new SubmissionInfo(this.id, this.problem_id, this.language, this.sourceCode, this.submitTimeStamp, this.result, this.resultTimeStamp);
    }
}
