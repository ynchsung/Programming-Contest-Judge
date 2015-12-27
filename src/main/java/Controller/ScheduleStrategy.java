package Controller;

import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public interface ScheduleStrategy {
    public Judge schedule(JSONObject submit);
}
