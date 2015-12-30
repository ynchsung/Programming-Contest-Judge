package Controller;

import Controller.DatabaseManager.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private Core() {
        this.teams = new HashMap<String, Team>();
        this.judges = new HashMap<String, Judge>();
        this.problems = new HashMap<String, Problem>();
        this.scheduler = new Scheduler();
        this.port = 7122;

        // TODO: read config, build teams, judges, problems(Map)
        /* ==== DUMMY TEST CONFIG ==== */
        ConfigureManager configureManager = new ConfigureManager();
        AccountManager accountManager = new AccountManager();
        ProblemManager problemManager = new ProblemManager();
        SubmissionManager submissionManager = new SubmissionManager();
        QAManager qaManager = new QAManager();
        ClarificationManager clarificationManager = new ClarificationManager();
        Map<String, String> config = new HashMap<String, String>();

        configureManager.createTable();
        accountManager.createTable();
        problemManager.createTable();
        submissionManager.createTable();
        qaManager.createTable();
        clarificationManager.createTable();

        config.put("ip", "8.8.8.8"/* TODO: get local ip */);
        config.put("port", "7122");
        config.put("scoreboard_port", "7123");
        config.put("judge_password", "judge7122");
        config.put("start_time", "2016-01-01T12:00:00");
        config.put("duration", "300");
        configureManager.addEntry(config);

        for (int i = 1; i <= 10; i++) {
            String id = String.format("team%0d", i);
            this.teams.put(id, new Team(id, null));
            Map<String, String> team_account = new HashMap<String, String>();
            team_account.put("account", id);
            team_account.put("password", id);
            team_account.put("type", "team");
            accountManager.addEntry(team_account);
        }
        for (int i = 1; i <= 3; i++) {
            String id = String.format("judge%0d", i);
            this.judges.put(id, new Judge(id, null));
            Map<String, String> judge_account = new HashMap<String, String>();
            judge_account.put("account", id);
            judge_account.put("password", "judge7122");
            judge_account.put("type", "judge");
            accountManager.addEntry(judge_account);
        }
        for (int i = 1; i <= 5; i++) {
            String id = String.format("p%c", 'A' + i - 1);
            Map<String, String> pinfo = new HashMap<String, String>();
            long timeStamp = System.currentTimeMillis() / 1000;
            this.problems.put(id, new Problem(id, 6 - i, 65536, timeStamp));
            pinfo.put("problem_id", id);
            pinfo.put("time_limit", Integer.toString(6 - i));
            pinfo.put("memory_limit", "65536");
            pinfo.put("special_judge", "");
            pinfo.put("input", String.format("1\n%d %d\n", i * 2 + 7122, i + 7122));
            pinfo.put("output", String.format("%d\n", i * 2 + 7122 + i + 7122));
            pinfo.put("time_stamp", Long.toString(timeStamp));
            problemManager.addEntry(pinfo);
        }
        /* ==== DUMMY TEST CONFIG ==== */
    }

    static public Core getInstance() {
        return sharedInstance;
    }

    static public void run() {
        if (sharedInstance != null)
            return;
        sharedInstance = new Core();
        sharedInstance.start();
    }

    private void start() {
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

    public List<Team> getAllTeam() {
        return new ArrayList<Team>(teams.values());
    }

    public List<Judge> getAllJudge() {
        return new ArrayList<Judge>(judges.values());
    }

    public Problem getProblemByID(String id) {
        return this.problems.get(id);
    }

    public Scheduler getScheduler() {
        return this.scheduler;
    }
}
