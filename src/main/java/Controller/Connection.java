package Controller;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class Connection extends Thread {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private Client client;
    private BlockingQueue<JSONObject> sendQueue;

    class ProcessSendQueueThread extends Thread {
        public ProcessSendQueueThread() {
        }

        public void run() {
            sendQueue.clear();
            try {
                while (true) {
                    JSONObject msg = sendQueue.take();
                    try {
                        output.writeUTF(msg.toString());
                    }
                    catch (IOException e) {
                    }
                }
            }
            catch (InterruptedException e) {
                sendQueue.clear();
            }
        }
    }

    public Connection(Socket socket) {
        this.socket = socket;
        this.client = null;
        this.sendQueue = new LinkedBlockingQueue<JSONObject>();
        try {
            this.input = new DataInputStream(this.socket.getInputStream());
            this.output = new DataOutputStream(this.socket.getOutputStream());
        }
        catch (IOException e) {
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void send(JSONObject msg) throws InterruptedException {
        this.sendQueue.put(msg);
    }

    public void run() {
        Thread sendThread = new ProcessSendQueueThread();
        sendThread.start();
        try {
            while (true) {
                String msg = this.input.readUTF();
                try {
                    this.client.handle(new JSONObject(msg));
                }
                catch (JSONException e) {
                }
            }
        }
        catch (IOException e) {
            this.client.logout();
            this.client = null;
            sendThread.interrupt();
        }
    }
}
