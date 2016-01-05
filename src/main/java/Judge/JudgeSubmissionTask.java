package Judge;

public class JudgeSubmissionTask {
    private final int submission_id;
    private final String problem_id;
    private final String language;
    private final String sourceCode;
    private final int submission_time_stamp;
    private final int testdata_time_stamp;

    public JudgeSubmissionTask(int submission_id, String problem_id, String language, String sourceCode,
                               int submission_time_stamp, int testdata_time_stamp) {
        this.submission_id = submission_id;
        this.problem_id = problem_id;
        this.language = language;
        this.sourceCode = sourceCode;
        this.submission_time_stamp = submission_time_stamp;
        this.testdata_time_stamp = testdata_time_stamp;
    }

    public int getSubmissionID() {
        return this.submission_id;
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

    public int getSubmissionTimeStamp() {
        return this.submission_time_stamp;
    }

    public int getTestDataTimeStamp() {
        return this.testdata_time_stamp;
    }
}
