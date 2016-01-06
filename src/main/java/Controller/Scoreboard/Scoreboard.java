package Controller.Scoreboard;

import Controller.DatabaseManager.*;
import java.util.*;

public class Scoreboard {
    private String html;

    public Scoreboard() {
    }

    public String getHTML() {
        return this.html;
    }

    public void update(){
        SubmissionManager submissionManager = new SubmissionManager();
        ProblemManager problemManager = new ProblemManager();
        AccountManager accountManager = new AccountManager();

        List<Map<String, String>> submission_list = submissionManager.queryAll();
        List<Map<String, String>> problem_list = problemManager.queryAll();
        List<Map<String, String>> account_list = accountManager.queryAll();

        List<String> problem_id = new ArrayList<String>();
        for (Map<String, String> temp_problem : problem_list) {
            problem_id.add(temp_problem.get("problem_id"));
        }

        List<String> team_id = new ArrayList<String>();
        for (Map<String, String> temp_account : account_list) {
            String type = temp_account.get("type");
            if (type.equals("team")) {
                team_id.add(temp_account.get("account"));
            }
        }
        /*
        now we have all problemid & teamid
        */
        /*get all submission*/

        Map<String, Submission> submission_map = new HashMap<String, Submission>();
        for (Map<String, String> temp_submission : submission_list) {
            submission_map.put(temp_submission.get("submission_id"), new Submission(temp_submission));
        }

        Map<String, TeamScore> teamMap = new HashMap<String, TeamScore>();
        /*initialize all team in teamMap*/
        for (String id : team_id) {
            teamMap.put(id, new TeamScore(id));
        }

        ProblemScore temp_problem_score;
        for (Submission entry : submission_map.values()) {
            //traverse all submission and record each result into teamMap information
            if (!teamMap.containsKey(entry.getTeamId())) {
                continue;
            }
            TeamScore temp_teamScore = teamMap.get(entry.getTeamId());
            if (!temp_teamScore.getMap().containsKey(entry.getProblemId())) { //if problem first submit
                temp_teamScore.getMap().put(entry.getProblemId(), new ProblemScore());
            }
            temp_problem_score = temp_teamScore.getMap().get(entry.getProblemId());
            if (!temp_problem_score.getAc()) { //if problem not ac yet
                if (entry.getResult()) {
                    temp_problem_score.setAc(true);
                    temp_problem_score.setAcTime(entry.getSubtimStamp());
                } else
                    temp_problem_score.increaseSubmitNum();
            } else {
                if (entry.getResult() && (temp_problem_score.getAcTime() > entry.getSubtimStamp()))
                    temp_problem_score.setAcTime(entry.getSubtimStamp());
            }
        }

        List<TeamScore> counted_team_list = new ArrayList<TeamScore>();
        for (TeamScore entry : teamMap.values()) {
            entry.countPenalty();
            entry.ac_count();
            counted_team_list.add(entry);
        }
        Collections.sort(counted_team_list);

        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>");
        builder.append("<html lang=\"zh-Hant\">");
        builder.append("<head><style>table, th, td {border: 1px solid black;border-collapse: collapse;}th, td {padding: 5px;}th {text-align: left;}</style></head>");
        builder.append("<body><table class=\"table table-striped\" style=\"width:100%\">");
        builder.append("<tr><th>Team</th><th>Problem solved</th><th>penalty</th>");
        for (String i : problem_id) {
            builder.append("<th>").append(i).append("</th>");
        }
        builder.append("</tr>");
        for (TeamScore temp_team : counted_team_list) {
            builder.append("<tr>");
            builder.append("<td>").append(temp_team.getTeamID()).append("</td>");
            builder.append("<td>").append(temp_team.getTotalProblem()).append("</td>");
            builder.append("<td>").append(temp_team.getPenalty()).append("</td>");
            for (String j : problem_id) {
                if (temp_team.getMap().containsKey(j)) {
                    temp_problem_score = temp_team.getMap().get(j);
                    if (temp_problem_score.getAc())
                        builder.append("<td class = \"success\">").append(temp_problem_score.getSubmitNum())
                                .append("/").append(temp_problem_score.getAcTime()).append("</td>");
                    else
                        builder.append("<td class = \"danger\" >").append(temp_problem_score.getSubmitNum())
                                .append("/--</td>");
                }
                else
                    builder.append("<td class = \"active\">0/--</td>");
            }
            builder.append("</tr>");
        }
        builder.append("</table></body></html>");
        this.html = builder.toString();
    }
}
