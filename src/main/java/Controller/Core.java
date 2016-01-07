package Controller;

import Controller.DatabaseManager.*;
import Controller.Scoreboard.ScoreBoardHttpServer;
import Shared.ContestTimer;
import Shared.MyUtil;
import Shared.ProblemInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.*;
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
    private Thread listenThread;

    private Core() {
        boolean f = false;
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()&&inetAddress instanceof Inet4Address) {
                        String ipAddress = inetAddress.getHostAddress().toString();
                        this.ip = inetAddress;
                        f = true;
                        break;
                    }
                }
                if (f)
                    break;
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        this.teams = new HashMap<String, Team>();
        this.judges = new HashMap<String, Judge>();
        this.problems = new HashMap<String, ProblemInfo>();
        this.scheduler = new Scheduler();
        this.timer = new ContestTimer(300*60);

        try {
            readConfigure();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.scoreBoardHttpServer = new ScoreBoardHttpServer(this.scoreBoardPort);
    }

    private void readTeamAccount(String pathName) {
        AccountManager accountManager = new AccountManager();
        File file = new File(pathName);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String [] sp = line.split(",");
                Map<String, String> team_account = new HashMap<String, String>();
                this.teams.put(sp[0], new Team(sp[0], null));
                team_account.put("account", sp[0]);
                team_account.put("password", sp[1]);
                team_account.put("type", "team");
                accountManager.addEntry(team_account);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void readJudgeAccount(String pathName) {
        AccountManager accountManager = new AccountManager();
        File file = new File(pathName);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String [] sp = line.split(",");
                this.judges.put(sp[0], new Judge(sp[0], null));
                Map<String, String> judge_account = new HashMap<String, String>();
                judge_account.put("account", sp[0]);
                judge_account.put("password", sp[1]);
                judge_account.put("type", "judge");
                accountManager.addEntry(judge_account);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void readProblemInfo(String pathName) {
        ProblemManager problemManager = new ProblemManager();
        File file = new File(pathName);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String [] sp = line.split(",");
                String input = MyUtil.readFromFile(new File(sp[1]));
                String output = MyUtil.readFromFile(new File(sp[2]));
                int timeLimit = Integer.valueOf(sp[3]);
                int memoryLimit = Integer.valueOf(sp[4]);
                String judgeMethod = "";
                if (sp.length == 6)
                    judgeMethod = MyUtil.readFromFile(new File(sp[5]));

                Map<String, String> pinfo = new HashMap<String, String>();
                this.problems.put(sp[0], new ProblemInfo(sp[0], timeLimit, memoryLimit, 0));
                pinfo.put("problem_id", sp[0]);
                pinfo.put("time_limit", String.valueOf(timeLimit));
                pinfo.put("memory_limit", String.valueOf(memoryLimit));
                pinfo.put("special_judge", judgeMethod);
                pinfo.put("input", input);
                pinfo.put("output", output);
                pinfo.put("time_stamp", "0");
                problemManager.addEntry(pinfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void readConfigure() throws FileNotFoundException {
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
        this.port = 8888;
        this.scoreBoardPort = 8889;
        // default port

        File file = new File("config.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String [] sp = line.split(":");
                if (sp[0].equals("port"))
                    this.port = Integer.valueOf(sp[1]);
                else if (sp[0].equals("scoreboardport"))
                    this.scoreBoardPort = Integer.valueOf(sp[1]);
                else if (sp[0].equals("team")) {
                    readTeamAccount(sp[1]);
                }
                else if (sp[0].equals("judge")) {
                    readJudgeAccount(sp[1]);
                }
                else if (sp[0].equals("problem")) {
                    readProblemInfo(sp[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        config.put("ip", this.ip.getHostAddress());
        config.put("port", Integer.toString(this.port));
        config.put("scoreboard_port", Integer.toString(this.scoreBoardPort));
        config.put("start_time", "0");
        config.put("duration", "300");
        config.put("time_stamp", "0");
        configureManager.addEntry(config);
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
        listenThread = new Thread(new Runnable() {
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

    public void halt() {
        scheduler.interrupt();
        listenThread.interrupt();
        Client.killAllClient();
        timer.pause();
    }

    public void haltScoreBoardServer() {
        scoreBoardHttpServer.halt();
    }
}
