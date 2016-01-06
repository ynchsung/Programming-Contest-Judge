package Controller.Scoreboard;

import java.util.Map;
import java.util.HashMap;

public class TeamScore {
    private final String team_id;
    private int totalProblem;
    private int penalty;
    private Map<String, ProblemScore> map;

    public TeamScore(String team_id) {
        this.team_id = team_id;
        this.totalProblem = 0;
        this.penalty = 0;
        this.map = new HashMap<String, ProblemScore>();
    }

    public String getTeamID() {
        return this.team_id;
    }

    public int getTotalProblem() {
        return this.totalProblem;
    }

    public int getPenalty() {
        return this.penalty;
    }

    public Map<String, ProblemScore> getMap() {
        return this.map;
    }

    public void ac_count() {
        this.totalProblem = 0;
        for (Map.Entry<String, ProblemScore> entry : this.map.entrySet()) {
            ProblemScore temp_problemscore;
            temp_problemscore = entry.getValue();
            if (temp_problemscore.getAc())
                this.totalProblem++;
        }
    }

    public void countPenalty() {
        this.penalty = 0;
        for (Map.Entry<String, ProblemScore> entry : this.map.entrySet()) {
            ProblemScore temp_problemscore;
            temp_problemscore = entry.getValue();
            if (temp_problemscore.getAc()) {
                this.penalty += temp_problemscore.getSubmitNum() * 20 + temp_problemscore.getAcTime();
            }
        }
    }
}
