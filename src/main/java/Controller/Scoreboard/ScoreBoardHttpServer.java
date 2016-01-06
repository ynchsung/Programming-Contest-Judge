package Controller.Scoreboard;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tenyoku on 2016/1/6.
 */
public class ScoreBoardHttpServer extends Thread{
    static private final int UPDATE_PERIOD = 30*1000;

    private class CountDownTask extends TimerTask {
        @Override
        public void run() {
            scoreboardGenerator.update();
            String html = scoreboardGenerator.getHTML();
            result = "";
            result += "HTTP/1.1 200 OK\r\n";
            result += "Connection close\r\n";
            result += "Content-Length: "+ Integer.toString(html.getBytes().length)+"\r\n";
            result += "Content-Type: text/html\r\n";
            result += "\r\n";
            result += html;
        }
    }
    private ServerSocket serverSocket;
    private Scoreboard scoreboardGenerator;
    private String result;

    public ScoreBoardHttpServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        scoreboardGenerator = new Scoreboard();
    }

    @Override
    public void start() {
        Timer updateTimer = new Timer();
        updateTimer.schedule(new CountDownTask(), 0, UPDATE_PERIOD);
        super.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                socket.getOutputStream().write(result.getBytes());
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
