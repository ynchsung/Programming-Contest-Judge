package Controller;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Iterator;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class LeastTimeStrategy implements ScheduleStrategy {
    class JudgeInfo {
        public String id;
        public int tot_time;

        public JudgeInfo(String id) {
            this.id = id;
            this.tot_time = 0;
        }
    }
    private PriorityQueue<JudgeInfo> judgeinfos;

    public LeastTimeStrategy(Map<String, Judge> judges) {
        Comparator<JudgeInfo> cmp = new Comparator<JudgeInfo>() {
            public int compare(JudgeInfo a, JudgeInfo b) {
                return (a.tot_time - b.tot_time);
            }
        };
        this.judgeinfos = new PriorityQueue<JudgeInfo>(0, cmp);
        for (String key : judges.keySet()) {
            this.judgeinfos.offer(new JudgeInfo(key));
        }
    }

    public Judge schedule(JSONObject submit) {
        int t;
        List<JudgeInfo> tmp = new ArrayList<JudgeInfo>();
        try {
            t = Core.getInstance().getProblemByID((String)submit.get("problem_id")).getTimeLimit();
        }
        catch (JSONException e) {
            return null;
        }
        while (true) {
            JudgeInfo ret = this.judgeinfos.poll();
            Judge judge = Core.getInstance().getJudgeByID(ret.id);
            if (judge.isAlive()) {
                ret.tot_time += t;
                for (Iterator it = tmp.iterator(); it.hasNext(); )
                    this.judgeinfos.offer((JudgeInfo)it.next());
                this.judgeinfos.offer(ret);
                return judge;
            }
            else {
                tmp.add(ret);
                if (this.judgeinfos.size() == 0) {
                    for (Iterator it = tmp.iterator(); it.hasNext(); )
                        this.judgeinfos.offer((JudgeInfo)it.next());
                    tmp.clear();
                }
            }
        }
    }
}
