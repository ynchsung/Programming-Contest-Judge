package Controller;

import Controller.Judge;
import org.json.JSONObject;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class RoundRobinStrategy implements ScheduleStrategy {
    int current;
    private List<String> judgeIDs;

    public RoundRobinStrategy(Map<String, Judge> judges) {
        this.current = 0;
        this.judgeIDs = new ArrayList<String>();
        for (String key : judges.keySet()) {
            this.judgeIDs.add(key);
        }
    }

    public Judge schedule(JSONObject submit) {
        while (true) {
            Judge judge = Core.getInstance().getJudgeByID(this.judgeIDs.get(this.current));
            this.current = (this.current + 1) % this.judgeIDs.size();
            if (judge.isAlive())
                return judge;
        }
    }
}
