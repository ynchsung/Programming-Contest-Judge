package Judge.DatabaseManager;

import java.sql.*;
import java.util.*;
import java.lang.String;

public class SubmissionManager {
    final int sleepTime = 200;
    private static List<Observer> observers = new ArrayList<Observer>();

    public void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:submission.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Submission " +
                "(SubmissionID INT PRIMARY KEY NOT NULL," +
                " ProblemID STRING NOT NULL," +
                " TeamID    STRING NOT NULL," +
                " SubmissionTimestamp   INT NOT NULL," +
                " Language      STRING," +
                " SourceCode    STRING," +
                " Result    STRING," +
                " ResultTimestamp   INT," +
                " DataTimestamp INT)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void addEntry(Map<String, String> entry) {
        Connection c = null;
        PreparedStatement stmt = null;

        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:submission.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("INSERT INTO Submission " +
                                "(SubmissionID,ProblemID,TeamID,SubmissionTimestamp,Language,SourceCode,Result,ResultTimestamp,DataTimestamp)" +
                                "VALUES (?, ?, ?, ?, ?, ?, 'Pending', -1, -1);");
                stmt.setString(1, entry.get("submission_id"));
                stmt.setString(2, entry.get("problem_id"));
                stmt.setString(3, entry.get("team_id"));
                stmt.setString(4, entry.get("time_stamp"));
                stmt.setString(5, entry.get("language"));
                stmt.setString(6, entry.get("source_code"));
                stmt.executeUpdate();
                stmt.close();
                c.commit();
                c.close();
                notifyObservers();
                break;
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
    }

    public void updateEntry(Map<String, String> entry) {
        Connection c = null;
        PreparedStatement stmt = null;
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:submission.db");
                c.setAutoCommit(false);

                int nRTS = Integer.parseInt(entry.get("time_stamp"));

                stmt = c.prepareStatement("SELECT ResultTimestamp FROM Submission WHERE SubmissionID = ?;");
                stmt.setString(1, entry.get("submission_id"));
                ResultSet rs = stmt.executeQuery();

                if (!rs.next() || rs.getInt("ResultTimestamp") > nRTS) {
                    rs.close();
                    stmt.close();
                    c.close();
                    break;
                }
                rs.close();
                stmt.close();

                stmt = c.prepareStatement("UPDATE Submission SET Result = ?, ResultTimestamp = ?, DataTimestamp = ? WHERE SubmissionID = ?;");

                stmt.setString(1, entry.get("result"));
                stmt.setString(2, entry.get("time_stamp"));
                stmt.setString(3, entry.get("testdata_time_stamp"));
                stmt.setString(4, entry.get("submission_id"));
                stmt.executeUpdate();
                stmt.close();
                c.commit();
                c.close();
                notifyObservers();
                break;
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
    }

    public List<Map<String, String>> queryAll() {
        Connection c = null;
        Statement stmt = null;
        List<Map<String, String>> response = new ArrayList<Map<String, String>>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:submission.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM Submission;" );
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    int sid = rs.getInt("SubmissionID");
                    String pid = rs.getString("ProblemID");
                    String tid = rs.getString("TeamID");
                    int stime = rs.getInt("SubmissionTimestamp");
                    String result = rs.getString("Result");
                    int rtime = rs.getInt("ResultTimestamp");
                    String lang = rs.getString("Language");
                    int dtime = rs.getInt("DataTimestamp");
                    entry.put("submission_id", Integer.toString(sid));
                    entry.put("problem_id", pid);
                    entry.put("team_id", tid);
                    entry.put("submission_time_stamp", Integer.toString(stime));
                    entry.put("result", result);
                    entry.put("result_time_stamp", Integer.toString(rtime));
                    entry.put("language", lang);
                    entry.put("testdata_time_stamp", Integer.toString(dtime));
                    if (rs.getString("SourceCode") != null) {
                        entry.put("source_code", "1");
                    }
                    response.add(entry);
                }
                rs.close();
                stmt.close();
                c.close();
                break;
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
        return response;
    }

    public String queryCode(int submission_id) {
        Connection c = null;
        PreparedStatement stmt = null;
        String response = new String();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:submission.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("SELECT SourceCode FROM Submission WHERE SubmissionID = ?;");
                stmt.setString(1, Integer.toString(submission_id));
                ResultSet rs = stmt.executeQuery();
                response = rs.getString("SourceCode");
                rs.close();
                stmt.close();
                c.close();
                break;
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
        return response;
    }

    public List<Map<String, String>> syncSubmission(String team_id, int time_stamp) {
        Connection c = null;
        PreparedStatement stmt = null;
        List<Map<String, String>> response = new ArrayList<Map<String, String>>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:submission.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("SELECT SubmissionID, ProblemID, SubmissionTimestamp, Language, SourceCode FROM Submission" +
                    " WHERE TeamID = ? AND SubmissionTimestamp >= ?;");
                stmt.setString(1, team_id);
                stmt.setString(2, Integer.toString(time_stamp));
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    int sid = rs.getInt("SubmissionID");
                    String pid = rs.getString("ProblemID");
                    int stime = rs.getInt("SubmissionTimestamp");
                    String lang = rs.getString("Language");
                    entry.put("submission_id", Integer.toString(sid));
                    entry.put("problem_id", pid);
                    entry.put("submission_time_stamp", Integer.toString(stime));
                    entry.put("language", lang);
                    if (rs.getString("SourceCode") != null) {
                        entry.put("source_code", "1");
                    }
                    response.add(entry);
                }
                rs.close();
                stmt.close();
                c.close();
                break;
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
        return response;
    }

    public List<Map<String, String>> syncResult(String team_id, int time_stamp) {
        Connection c = null;
        PreparedStatement stmt = null;
        List<Map<String, String>> response = new ArrayList<Map<String, String>>();

        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:submission.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("SELECT SubmissionID, SubmissionTimestamp, Result, ResultTimestamp, DataTimestamp FROM Submission" +
                    " WHERE TeamID = ? AND ResultTimestamp >= ?;");
                stmt.setString(1, team_id);
                stmt.setString(2, Integer.toString(time_stamp));
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    String result = rs.getString("Result");
                    int sid = rs.getInt("SubmissionID");
                    int stime = rs.getInt("SubmissionTimestamp");
                    int rtime = rs.getInt("ResultTimestamp");
                    int dtime = rs.getInt("DataTimestamp");
                    entry.put("submission_id", Integer.toString(sid));
                    entry.put("submission_time_stamp", Integer.toString(stime));
                    entry.put("result", result);
                    entry.put("result_time_stamp", Integer.toString(rtime));
                    entry.put("testdata_time_stamp", Integer.toString(dtime));
                    response.add(entry);
                }
                rs.close();
                stmt.close();
                c.close();
                break;
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
        return response;
    }

    public Map<String, String> getSubmissionById(int submission_id) {
        Connection c = null;
        PreparedStatement stmt = null;
        Map<String, String> response = new HashMap<String, String>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:submission.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("SELECT * FROM Submission WHERE SubmissionID = ?;");
                stmt.setString(1, Integer.toString(submission_id));
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int sid = rs.getInt("SubmissionID");
                    String pid = rs.getString("ProblemID");
                    int tid = rs.getInt("TeamID");
                    int stime = rs.getInt("SubmissionTimestamp");
                    String lang = rs.getString("Language");
                    String result = rs.getString("Result");
                    int rtime = rs.getInt("ResultTimestamp");
                    int dtime = rs.getInt("DataTimestamp");
                    response.put("submission_id", Integer.toString(sid));
                    response.put("problem_id", pid);
                    response.put("team_id", Integer.toString(tid));
                    response.put("submission_time_stamp", Integer.toString(stime));
                    response.put("language", lang);
                    response.put("result", result);
                    response.put("result_time_stamp", Integer.toString(rtime));
                    response.put("testdata_time_stamp", Integer.toString(dtime));
                    if (rs.getString("SourceCode") != null) {
                        response.put("source_code", "1");
                    }
                }
                rs.close();
                stmt.close();
                c.close();
                break;
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
        return response;
    }

    public void rejudge(String problem_id) {
        Connection c = null;
        PreparedStatement stmt = null;
        List<Map<String, String>> response = new ArrayList<Map<String, String>>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:submission.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("SELECT * FROM Submission WHERE ProblemID = ?;");
                stmt.setString(1, problem_id);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    int sid = rs.getInt("SubmissionID");
                    String pid = rs.getString("ProblemID");
                    String tid = rs.getString("TeamID");
                    int stime = rs.getInt("SubmissionTimestamp");
                    String result = rs.getString("Result");
                    int rtime = rs.getInt("ResultTimestamp");
                    String lang = rs.getString("Language");
                    int dtime = rs.getInt("DataTimestamp");
                    String code = rs.getString("SourceCode");
                    entry.put("submission_id", Integer.toString(sid));
                    entry.put("problem_id", pid);
                    entry.put("team_id", tid);
                    entry.put("submission_time_stamp", Integer.toString(stime));
                    entry.put("result", result);
                    entry.put("result_time_stamp", Integer.toString(rtime));
                    entry.put("language", lang);
                    entry.put("testdata_time_stamp", Integer.toString(dtime));
                    entry.put("source_code", code);
                    response.add(entry);
                }
                rs.close();
                stmt.close();

                stmt = c.prepareStatement("UPDATE Submission SET Result = 'Pending', ResultTimestamp = -1 WHERE ProblemID = ?;");
                stmt.setString(1, problem_id);
                stmt.executeUpdate();
                stmt.close();
                c.commit();
                c.close();
                notifyObservers();
                break;
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
    }

    public static void register(Observer observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void flushTable() {
        Connection c = null;
        Statement stmt = null;
        String response = new String();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:submission.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "DROP TABLE IF EXISTS Submission;";
                stmt.executeUpdate(sql);
                stmt.close();
                c.commit();
                c.close();
                notifyObservers();
                break;
            }
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
    }

    private boolean checkLock(String message, Connection c) {
        try {
            c.close();
            if (message.equals("database is locked") || message.startsWith("[SQLITE_BUSY]")) {
                Thread.sleep(sleepTime);
                return true;
            }
            else {
                System.err.println("Exception: " + message);
            }
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return false;
    }
}
