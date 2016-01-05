package Shared;

import org.json.JSONObject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by tenyoku on 2016/1/4.
 */
public class AckQueue {
    class ProcessSendQueueThread extends Thread {
        private final Object lock;

        public ProcessSendQueueThread(Object lock) {
            this.lock = lock;
        }
        public void run() {
            while (true) {
                try {
                    synchronized (lock) {
                        nowMsg = queue.take();
                        server.send(nowMsg);
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                }
            }
        }
    }
    private ControllerServer server;
    private BlockingQueue<JSONObject> queue;
    private Thread thread;
    private JSONObject nowMsg;

    public AckQueue(ControllerServer server) {
        this.server = server;
        this.queue = new LinkedBlockingQueue<>();
        this.thread = new ProcessSendQueueThread(this);
    }

    public void start() {
        thread.start();
    }

    public void add(JSONObject msg) {
        queue.add(msg);
    }

    synchronized public JSONObject ackAndGetNowMsg() {
        JSONObject msg = nowMsg;
        nowMsg = null;
        notify();
        return msg;
    }
}
