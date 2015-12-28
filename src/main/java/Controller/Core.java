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

    private Map<String, Team> teams;
    private Map<String, Judge> judges;
    private Map<String, Problem> problems;
    private Scheduler scheduler;
    private final int port;

    private Core(int port) {
        this.teams = new HashMap<String, Team>();
        this.judges = new HashMap<String, Judge>();
        this.problems = new HashMap<String, Problem>();
        this.scheduler = new Scheduler();
        this.port = port;
    }

    static public Core getInstance() {
        return sharedInstance;
    }

    static public void start(int port) {
        if (sharedInstance != null)
            return;
        sharedInstance = new Core(port);
        sharedInstance.start();
    }

    private void start() {
        // TODO: read config, build teams, judges, problems(Map)

        this.scheduler.setStrategy(new RoundRobinStrategy(this.judges));
        this.scheduler.start();
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

    public Team getTeamByID(String id) {
        return this.teams.get(id);
    }

    public Judge getJudgeByID(String id) {
        return this.judges.get(id);
    }

    public Problem getProblemByID(String id) {
        return this.problems.get(id);
    }

    public Scheduler getScheduler() {
        return this.scheduler;
    }
}
