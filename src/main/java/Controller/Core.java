package Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class Core {
    static private Core sharedInstance = null;

    private Map<Integer, Team> teams;
    private Map<Integer, Judge> judges;
    private ScheduleStrategy scheduler;
    private final int port;

    private Core(ScheduleStrategy scheduler, int port) {
        this.teams = new HashMap<Integer, Team>();
        this.judges = new HashMap<Integer, Judge>();
        this.scheduler = scheduler;
        this.port = port;
    }

    static public Core getInstance() {
        return sharedInstance;
    }

    static public void start(ScheduleStrategy scheduler, int port) {
        if (sharedInstance != null)
            return;
        sharedInstance = new Core(scheduler, port);

        // TODO: read config, build teams, judges(Map)
        Thread listenThread = new Thread(new Runnable() {
            private int port;
            public Runnable setPort(int port) {
                this.port = port;
                return this;
            }
            @Override
            public void run() {
                ServerSocket serverSocket = null;
                ExecutorService threadExecutor = Executors.newCachedThreadPool();
                try {
                    serverSocket = new ServerSocket(this.port);
                    System.out.println("Server listening requests...");
                    while (true) {
                        Socket socket = serverSocket.accept();
                        Connection connection = new Connection(socket);
                        connection.setClient(new Guest(connection));
                        threadExecutor.execute(connection);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if (threadExecutor != null) {
                        threadExecutor.shutdown();
                    }
                    if (serverSocket != null) {
                        try {
                            serverSocket.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.setPort(port));
        listenThread.start();
    }

    public Team getTeamByID(Integer id) {
        return this.teams.get(id);
    }

    public Judge getJudgeByID(Integer id) {
        return this.judges.get(id);
    }
}
