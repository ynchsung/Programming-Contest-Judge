package Judge;

import java.util.*;

public class QuestionInfo {
    private final int id;
    private final String problem_id;
    private final String team_id;
    private final String content;
    private final int timeStamp;
    private List<AnswerInfo> answers;

    public QuestionInfo(int id, String problem_id, String team_id, String content, int timeStamp) {
        this.id = id;
        this.problem_id = problem_id;
        this.team_id = team_id;
        this.content = content;
        this.timeStamp = timeStamp;
        this.answers = Collections.synchronizedList(new ArrayList<AnswerInfo>());
    }

    public int getID() {
        return this.id;
    }

    public String getProblemID() {
        return this.problem_id;
    }

    public String getTeamID() {
        return this.team_id;
    }

    public String getContent() {
        return this.content;
    }

    public int getTimeStamp() {
        return this.timeStamp;
    }

    public final List<AnswerInfo> getAnswers() {
        return this.answers;
    }

    public void addAnswer(AnswerInfo answerInfo) {
        this.answers.add(answerInfo);
    }
}
