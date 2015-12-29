package Controller.DatabaseManager;

import java.sql.*;
import java.util.*;
import java.lang.String;

public class SubmissionManager {
    final int sleepTime = 200;

    public void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:submission.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Submission " +
                "(SubmissionID INT PRIMARY KEY  NOT NULL," +
                " ProblemID INT NOT NULL, " +
                " TeamID    INT NOT NULL, " +
                " SubmissionTimestamp   INT NOT NULL, " +
                " SourceCode    STRING, " +
                " Result    STRING, " +
                " ResultTimestamp   INT)";
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
        Statement stmt = null;

        String common = "INSERT INTO Submission (SubmissionID,ProblemID,TeamID,SubmissionTimestamp,SourceCode,Result,ResultTimestamp)";
        String sql = common + "VALUES (" + entry.get("submission_id") + ", " +
            entry.get("problem_id") + ", " +
            entry.get("team_id") + ", " +
            entry.get("time_stamp") + ", '" +
            entry.get("source_code") + "', " +
            "'Pending', -1);";

        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:submission.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
                c.commit();
                c.close();
                break;
            }
            catch (SQLException e) {
                checkLock(e.getMessage());
                continue;
            }
            catch (Exception e) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    public void updateEntry(Map<String, String> entry) {
        Connection c = null;
        Statement stmt = null;
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:submission.db");
                c.setAutoCommit(false);

                int nRTS = Integer.parseInt(entry.get("time_stamp"));

                stmt = c.createStatement();
                String sql = "SELECT ResultTimestamp FROM Submission WHERE SubmissionID = " + entry.get("submission_id") + ";";
                ResultSet rs = stmt.executeQuery(sql);

                if (rs.getInt("ResultTimestamp") > nRTS) {
                    rs.close();
                    stmt.close();
                    c.close();
                    break;
                }
                rs.close();
                stmt.close();

                sql = "UPDATE Submission SET Result = '" + entry.get("result") +
                    "',ResultTimestamp = " + entry.get("time_stamp") +
                    " WHERE SubmissionID = '" + entry.get("submission_id") + "';";

                stmt = c.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
                c.commit();
                c.close();
                break;
            }
            catch (SQLException e) {
                checkLock(e.getMessage());
                continue;
            }
            catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
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
                    int pid = rs.getInt("ProblemID");
                    int stime = rs.getInt("SubmissionTimestamp");
                    String result = rs.getString("Result");
                    int rtime = rs.getInt("ResultTimestamp");
                    entry.put("submission_id", Integer.toString(sid));
                    entry.put("problem_id", Integer.toString(pid));
                    entry.put("submission_time_stamp", Integer.toString(stime));
                    entry.put("result", result);
                    entry.put("result_time_stamp", Integer.toString(rtime));
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
            catch (SQLException e) {
                checkLock(e.getMessage());
                continue;
            }
            catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return response;
    }

    public String queryCode(int submission_id) {
        Connection c = null;
        Statement stmt = null;
        String response = new String();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:submission.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "SELECT SourceCode FROM Submission WHERE SubmissionID = " + Integer.toString(submission_id) + ";";
                ResultSet rs = stmt.executeQuery(sql);
                response = rs.getString("SourceCode");
                rs.close();
                stmt.close();
                c.close();
                break;
            }
            catch (SQLException e) {
                checkLock(e.getMessage());
                continue;
            }
            catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return response;
    }

    public List<Map<String, String>> sync(int team_id, int time_stamp) {
        Connection c = null;
        Statement stmt = null;
        List<Map<String, String>> response = new ArrayList<Map<String, String>>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:submission.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "SELECT SubmissionID, ProblemID, SubmissionTimestamp, SourceCode FROM Submission" +
                    " WHERE TeamID = " + Integer.toString(team_id) +
                    " AND SubmissionTimestamp >= " + Integer.toString(time_stamp) + ";";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    int sid = rs.getInt("SubmissionID");
                    int pid = rs.getInt("ProblemID");
                    int stime = rs.getInt("SubmissionTimestamp");
                    entry.put("submission_id", Integer.toString(sid));
                    entry.put("problem_id", Integer.toString(pid));
                    entry.put("submission_time_stamp", Integer.toString(stime));
                    if (rs.getString("SourceCode") != null) {
                        entry.put("source_code", "1");
                    }
                    response.add(entry);
                }
                rs.close();
                stmt.close();

                stmt = c.createStatement();
                sql = "SELECT SubmissionID, Result, ResultTimestamp FROM Submission" +
                    " WHERE TeamID = " + Integer.toString(team_id) +
                    " AND ResultTimestamp >= " + Integer.toString(time_stamp) + ";";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    String result = rs.getString("Result");
                    int sid = rs.getInt("SubmissionID");
                    int rtime = rs.getInt("ResultTimestamp");
                    entry.put("submission_id", Integer.toString(sid));
                    entry.put("result", result);
                    entry.put("result_time_stamp", Integer.toString(rtime));
                    response.add(entry);
                }
                rs.close();
                stmt.close();
                c.close();
                break;
            }
            catch (SQLException e) {
                checkLock(e.getMessage());
                continue;
            }
            catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        return response;
    }

    private void checkLock(String message) {
        try {
            if (message.equals("database is locked")) {
                Thread.sleep(sleepTime);
            }
            else {
                System.err.println("SQLException: " + message);
            }
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
