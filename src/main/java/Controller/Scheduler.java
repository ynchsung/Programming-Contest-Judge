package Controller;

import org.json.JSONObject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ynchsung on 12/27/15.
 */
public class Scheduler extends Thread {
    private ScheduleStrategy strategy;
    private BlockingQueue<JSONObject> submissionQueue;

    public Scheduler() {
        this.strategy = null;
        this.submissionQueue = new LinkedBlockingQueue<JSONObject>();
    }

    public void setStrategy(ScheduleStrategy strategy) {
        this.strategy = strategy;
    }

    public void add(JSONObject submit) {
        while(true) {
            try {
                this.submissionQueue.put(submit);
                break;
            } catch (InterruptedException e) {
                continue;
            }
        }
    }

    public void run() {
        if (this.strategy == null)
            return;
        this.submissionQueue.clear();
        try {
            while (true) {
                this.strategy.schedule(this.submissionQueue.take());
            }
        }
        catch (InterruptedException e) {
        }
    }
}
