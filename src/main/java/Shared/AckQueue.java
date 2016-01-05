package Shared;

import org.json.JSONObject;
import sun.awt.Mutex;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by tenyoku on 2016/1/4.
 */
public class AckQueue {
    class ProcessSendQueueThread extends Thread {
        public void run() {
            while (true) {
                try {
                    JSONObject msg = queue.take();
                    mutex.lock();
                    server.send(msg);
                } catch (InterruptedException e) {
                }
            }
        }
    }
    private ControllerServer server;
    private BlockingQueue<JSONObject> queue;
    private Mutex mutex;
    private Thread thread;

    public AckQueue(ControllerServer server) {
        this.server = server;
        this.queue = new LinkedBlockingQueue<>();
        this.mutex = new Mutex();
        this.thread = new ProcessSendQueueThread();
    }

    public void start() {
        thread.start();
    }

    public void add(JSONObject msg) {
        queue.add(msg);
    }

    public void unlock() {
        mutex.unlock();
    }
}
