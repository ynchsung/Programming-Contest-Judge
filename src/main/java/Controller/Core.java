package Controller;

import Controller.DatabaseManager.*;
import Controller.Scoreboard.ScoreBoardHttpServer;
import Shared.ContestTimer;
import Shared.MyUtil;
import Shared.ProblemInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
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
    private Map<String, ProblemInfo> problems;
    private Scheduler scheduler;
    private InetAddress ip;
    private int port;
    private int scoreBoardPort;
    private ContestTimer timer;
    private ScoreBoardHttpServer scoreBoardHttpServer;

    private Core() {
        try {
            this.ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = 7122;
        this.scoreBoardPort = 7123;
        this.teams = new HashMap<String, Team>();
        this.judges = new HashMap<String, Judge>();
        this.problems = new HashMap<String, ProblemInfo>();
        this.scheduler = new Scheduler();
        this.scoreBoardHttpServer = new ScoreBoardHttpServer(this.scoreBoardPort);
        this.timer = new ContestTimer(300*60);

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

        config.put("ip", this.ip.getHostAddress());
        config.put("port", Integer.toString(this.port));
        config.put("scoreboard_port", Integer.toString(this.scoreBoardPort));
        config.put("judge_password", "judge7122");
        config.put("start_time", "0");
        config.put("duration", "300");
        config.put("time_stamp", "0");
        configureManager.addEntry(config);

        for (int i = 1; i <= 10; i++) {
            String id = String.format("team%02d", i);
            this.teams.put(id, new Team(id, null));
            Map<String, String> team_account = new HashMap<String, String>();
            team_account.put("account", id);
            team_account.put("password", id);
            team_account.put("type", "team");
            accountManager.addEntry(team_account);
        }
        for (int i = 1; i <= 3; i++) {
            String id = String.format("judge%02d", i);
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
            int timeStamp = 0;
            this.problems.put(id, new ProblemInfo(id, 6 - i, 65536, timeStamp));
            pinfo.put("problem_id", id);
            pinfo.put("time_limit", Integer.toString(6 - i));
            pinfo.put("memory_limit", "65536");
            pinfo.put("special_judge", "");
            pinfo.put("input", String.format("1\n%d %d\n", i * 2 + 7122, i + 7122));
            pinfo.put("output", String.format("%d\n", i * 2 + 7122 + i + 7122));
            pinfo.put("time_stamp", Long.toString(timeStamp));
            problemManager.addEntry(pinfo);
        }

        Map<String, String> submission = new HashMap<>();
        submission.put("problem_id", "pA");
        submission.put("team_id", "7122");
        submission.put("source_code", "#include \"jizz\"");
        submission.put("time_stamp", "9900");
        submission.put("result", "TLE");
        submission.put("result_time_stamp", "33333");
        submissionManager.addEntry(submission);

        Map<String, String> qa = new HashMap<>();
        qa.put("type", "question");
        qa.put("team_id", "1");
        qa.put("problem_id", "1");
        qa.put("content", "Is iron equal to wisdom?");
        qa.put("time_stamp", "7122");
        int qaID = qaManager.addEntry(qa);

        qa = new HashMap<>();
        qa.put("type", "answer");
        qa.put("question_id", Integer.toString(qaID));
        qa.put("answer", "Yes");
        qa.put("time_stamp", "7123");
        qaManager.addEntry(qa);

        Map<String, String> cr = new HashMap<>();
        cr.put("clarification_id", "1");
        cr.put("problem_id", "0");
        cr.put("content", "This is a test.");
        cr.put("time_stamp", "712222");
        clarificationManager.addEntry(cr);
        /* ==== DUMMY TEST CONFIG ==== */
    }

    static public Core getInstance() {
        if (sharedInstance == null) {
            synchronized (Core.class) {
                if (sharedInstance == null) {
                    sharedInstance = new Core();
                }
            }
        }
        return sharedInstance;
    }

    public void start() {
        this.timer.start();
        this.scheduler.setStrategy(new RoundRobinStrategy(this.judges));
        this.scheduler.start();
        this.scoreBoardHttpServer.start();
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

    public void updateTestData(String problemID, File input, File output, File specialJudge) {
        try {
            int timeStamp = this.getTimer().getCountedTime();
            Map<String, String> entry = new HashMap<>();
            entry.put("problem_id", problemID);
            entry.put("input", MyUtil.readFromFile(input));
            entry.put("output", MyUtil.readFromFile(output));
            if (specialJudge != null) {
                entry.put("special_judge", MyUtil.readFromFile(specialJudge));
            }
            entry.put("time_stamp", Long.toString(timeStamp));
            ProblemManager problemManager = new ProblemManager();
            problemManager.updateEntry(entry);

            ProblemInfo problemInfo = this.problems.get(problemID);
            if (problemInfo != null)
                problemInfo.updateInfo(problemInfo.getTimeLimit(), problemInfo.getMemoryLimit(), timeStamp, "", "", "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void rejudgeProblem(String problemID) {
        SubmissionManager submissionManager = new SubmissionManager();

        for (Map<String, String> ent : submissionManager.rejudge(problemID)) {
            try {
                ProblemManager problemManager = new ProblemManager();
                Map<String, String> pb = problemManager.getProblemById(problemID);
                JSONObject msg = new JSONObject();
                msg.put("msg_type", "submit");
                msg.put("submission_id", ent.get("submission_id"));
                msg.put("problem_id", ent.get("problem_id"));
                msg.put("language", ent.get("language"));
                msg.put("source_code", ent.get("source_code"));
                msg.put("time_stamp", ent.get("submission_time_stamp"));
                msg.put("testdata_time_stamp", pb.get("time_stamp"));
                Core.getInstance().getScheduler().add(msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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

    public Map<String, ProblemInfo> getProblems() {
        return this.problems;
    }

    public ProblemInfo getProblemByID(String id) {
        return this.problems.get(id);
    }

    public Scheduler getScheduler() {
        return this.scheduler;
    }

    public InetAddress getIP() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public int getScoreboardPort() {
        return this.scoreBoardPort;
    }

    public ContestTimer getTimer() {
        return timer;
    }

    public ArrayList<String> getProblemIDList() {
        return new ArrayList<>(problems.keySet());
    }
}
