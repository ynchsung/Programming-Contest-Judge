package Controller;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tenyoku on 2015/12/31.
 */
public class ContestTimer {
    public interface ContestTimerListener {
        void onUpdate(int totalSecond, int secondCounted);
        void onOver(int totalSecond);
    }
    private int totalSecond;
    private int secondCounted;
    private Timer timer;
    private final Object lock;
    private ContestTimerListener listener;
    private class CountDownTask extends TimerTask {
        @Override
        public void run() {
            secondCounted += 1;
            listener.onUpdate(totalSecond, secondCounted);
            if (secondCounted >= totalSecond) {
                synchronized (lock) {
                    timer.cancel();
                    listener.onOver(totalSecond);
                }
            }
        }
    }

    public ContestTimer(int totalSecond) {
        this.totalSecond = totalSecond;
        this.secondCounted = 0;
        this.timer = new Timer();
        this.lock = new Object();
    }

    // must be called after got the lock
    private void startCount() {
        timer = new Timer();
        timer.schedule(new CountDownTask(), 1000, 1000);
    }

    synchronized public void start() {
        synchronized (lock) {
            timer.cancel();
            secondCounted = 0;
            startCount();
        }
    }

    public void pause() {
        synchronized (lock) {
            timer.cancel();
        }
    }

    public void resume() {
        synchronized (lock) {
            timer.cancel();
            startCount();
        }
    }

    public void reset() {
        synchronized (lock) {
            timer.cancel();
            secondCounted = 0;
        }
    }

    public void setListener(ContestTimerListener listener) {
        this.listener = listener;
    }

    public void setDuration(int duration) {
        synchronized (lock) {
            this.totalSecond = duration;
        }
    }

    public int getDuration() {
        return this.totalSecond;
    }

    public int getRemainTime() {
        return Math.max(this.totalSecond-this.secondCounted, 0);
    }

    public int getCountedTime() {
        return this.secondCounted;
    }
}
