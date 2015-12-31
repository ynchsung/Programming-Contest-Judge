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
                "(SubmissionID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ProblemID STRING NOT NULL," +
                " TeamID    STRING NOT NULL," +
                " SubmissionTimestamp   INT NOT NULL," +
                " Language      STRING  NOT NULL," +
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

    public int addEntry(Map<String, String> entry) {
        Connection c = null;
        PreparedStatement stmt = null;

        int id = -1;

        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:submission.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("INSERT INTO Submission " +
                                "(ProblemID,TeamID,SubmissionTimestamp,Language,SourceCode,Result,ResultTimestamp,DataTimestamp)" +
                                "VALUES (?, ?, ?, ?, ?, 'Pending', -1, -1);", Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, entry.get("problem_id"));
                stmt.setString(2, entry.get("team_id"));
                stmt.setString(3, entry.get("time_stamp"));
                stmt.setString(4, entry.get("language"));
                stmt.setString(5, entry.get("source_code"));
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
                else {
                    System.err.println("Fail to generate ID");
                }
                rs.close();
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
        return id;
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
                stmt.setString(3, entry.get("data_time_stamp"));
                stmt.setString(4, entry.get("submission_id"));
                stmt.executeUpdate();
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
                    entry.put("data_time_stamp", Integer.toString(dtime));
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

    public List<Map<String, String>> sync(String team_id, int time_stamp) {
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

                stmt = c.prepareStatement("SELECT SubmissionID, Result, ResultTimestamp FROM Submission" +
                    " WHERE TeamID = ? AND ResultTimestamp >= ?;");
                stmt.setString(1, team_id);
                stmt.setString(2, Integer.toString(time_stamp));
                rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    String result = rs.getString("Result");
                    int sid = rs.getInt("SubmissionID");
                    int rtime = rs.getInt("ResultTimestamp");
                    int dtime = rs.getInt("DataTimestamp");
                    entry.put("submission_id", Integer.toString(sid));
                    entry.put("result", result);
                    entry.put("result_time_stamp", Integer.toString(rtime));
                    entry.put("data_time_stamp", Integer.toString(dtime));
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

    public Map<String, String> getSubmissionById(int submission_id) {
        Connection c = null;
        Statement stmt = null;
        Map<String, String> response = new HashMap<String, String>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:submission.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "SELECT * FROM Submission WHERE SubmissionID = " + submission_id + ";";
                ResultSet rs = stmt.executeQuery(sql);
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
                    response.put("data_time_stamp", Integer.toString(dtime));
                    if (rs.getString("SourceCode") != null) {
                        response.put("source_code", "1");
                    }
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
                String sql = "DELETE FROM Submission;";
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
