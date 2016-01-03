package Participant.DatabaseManager;

import java.sql.*;
import java.util.*;
import java.lang.String;

public class ConfigureManager {
    final int sleepTime = 200;

    public void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:configure.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Configure " +
                "(IP STRING PRIMARY KEY NOT NULL," +
                " Port  INT NOT NULL," +
                " ScoreboardPort INT    NOT NULL," +
                " JudgePassword  STRING NOT NULL," +
                " StartTime INT NOT NULL," +
                " Duration   INT NOT NULL," +
                " Timestamp INT NOT NULL" + ")";
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
                c = DriverManager.getConnection("jdbc:sqlite:configure.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("INSERT INTO Configure (IP,Port,ScoreboardPort,JudgePassword,StartTime,Duration,Timestamp) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?);");
                stmt.setString(1, entry.get("ip"));
                stmt.setString(2, entry.get("port"));
                stmt.setString(3, entry.get("scoreboard_port"));
                stmt.setString(4, entry.get("judge_password"));
                stmt.setString(5, entry.get("start_time"));
                stmt.setString(6, entry.get("duration"));
                stmt.setString(7, entry.get("time_stamp"));

                stmt.executeUpdate();
                stmt.close();
                c.commit();
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
    }

    public void updateEntry(Map<String, String> entry) {
        Connection c = null;
        Statement stmt = null;
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:configure.db");
                c.setAutoCommit(false);

                int nTS = Integer.parseInt(entry.get("time_stamp"));

                stmt = c.createStatement();
                String sql = "SELECT Timestamp FROM Configure;";
                ResultSet rs = stmt.executeQuery(sql);

                if (rs.getInt("Timestamp") > nTS) {
                    rs.close();
                    stmt.close();
                    c.close();
                    break;
                }
                rs.close();
                stmt.close();

                sql = "UPDATE Configure SET Duration = " + entry.get("duration") +
                    ",Timestamp = " + entry.get("time_stamp") + ";";

                stmt = c.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
                c.commit();
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
    }

    public List<Map<String, String>> queryAll() {
        Connection c = null;
        Statement stmt = null;
        List<Map<String, String>> response = new ArrayList<Map<String, String>>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:configure.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM Configure;" );
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    String ip = rs.getString("IP");
                    int port = rs.getInt("Port");
                    int sbport = rs.getInt("ScoreboardPort");
                    String jpw = rs.getString("JudgePassword");
                    int stime = rs.getInt("StartTime");
                    int dur = rs.getInt("Duration");
                    int ts = rs.getInt("Timestamp");
                    entry.put("ip", ip);
                    entry.put("port", Integer.toString(port));
                    entry.put("scoreboard_port", Integer.toString(sbport));
                    entry.put("judge_password", jpw);
                    entry.put("start_time", Integer.toString(stime));
                    entry.put("duration", Integer.toString(dur));
                    entry.put("time_stamp", Integer.toString(ts));
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
    
    public void flushTable() {
        Connection c = null;
        Statement stmt = null;
        String response = new String();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:configure.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "DROP TABLE IF EXISTS Configure;";
                stmt.executeUpdate(sql);
                stmt.close();
                c.commit();
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
    }

    private boolean checkLock(String message, Connection c) {
        try {
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
