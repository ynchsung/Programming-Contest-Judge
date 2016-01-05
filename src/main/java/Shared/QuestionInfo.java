package Shared;

import java.util.*;

public class QuestionInfo {
    private final int id;
    private final String problem_id;
    private final String team_id;
    private final String content;
    private final int timeStamp;
    private Map<Integer, AnswerInfo> answers;

    public QuestionInfo(int id, String problem_id, String team_id, String content, int timeStamp) {
        this.id = id;
        this.problem_id = problem_id;
        this.team_id = team_id;
        this.content = content;
        this.timeStamp = timeStamp;
        this.answers = new HashMap<Integer, AnswerInfo>();
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

    public final Map<Integer, AnswerInfo> getAnswers() {
        return this.answers;
    }

    public boolean addAnswer(AnswerInfo answerInfo) {
        if (!this.answers.containsKey(answerInfo.getID())) {
            this.answers.put(answerInfo.getID(), answerInfo);
            return true;
        }
        else
            return false;
    }

    public QuestionInfo copy() {
        QuestionInfo ret = new QuestionInfo(this.id, this.problem_id, this.team_id, this.content, this.timeStamp);
        for (Map.Entry<Integer, AnswerInfo> entry : this.answers.entrySet()) {
            ret.addAnswer(entry.getValue().copy());
        }
        return ret;
    }
}
