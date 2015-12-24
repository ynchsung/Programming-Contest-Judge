package Controller;

import org.json.JSONException;
import org.json.JSONObject;

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

    public Connection(Socket socket) {
        this.socket = socket;
        this.client = null;
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

    public void send(JSONObject msg) throws IOException {
        this.output.writeUTF(msg.toString());
    }

    public void run() {
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
        }
    }
}
