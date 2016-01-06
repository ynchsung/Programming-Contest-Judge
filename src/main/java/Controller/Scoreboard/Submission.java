package Controller.Scoreboard;

import java.util.Map;

public class Submission {
    private String team_id;
    private int subtimstamp;
    private String problem_id;
    private boolean result;

    public String getTeamId() {
        return team_id;
    }

    public int getSubtimStamp() {
        return subtimstamp;
    }

    public String getProblemId() {
        return problem_id;
    }

    public boolean getResult() {
        return result;
    }

    public Submission(Map<String, String> submissionfrommap) {
        this.team_id = submissionfrommap.get("team_id");
        this.subtimstamp = Integer.valueOf(submissionfrommap.get("submission_time_stamp"));
        this.problem_id = submissionfrommap.get("problem_id");
        this.result = (submissionfrommap.get("result").equals("AC"));
    }
}
