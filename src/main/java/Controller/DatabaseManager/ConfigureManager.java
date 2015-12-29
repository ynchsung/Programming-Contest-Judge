package Controller.DatabaseManager;

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
                " EndTime   INT NOT NULL," +
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
        Statement stmt = null;
        String common = "INSERT INTO Configure (IP,Port,ScoreboardPort,JudgePassword,StartTime,EndTime,Timestamp) ";
        String sql = common + "VALUES ('" + entry.get("ip") + "', " +
            entry.get("port") + ", " + entry.get("scoreboard_port") + ", '" +
            entry.get("judge_password") + "', " +
            entry.get("start_time") + ", " + entry.get("end_time") + ", " +
            entry.get("time_stamp") + ");";

        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:configure.db");
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

                sql = "UPDATE Configure SET EndTime = " + entry.get("end_time") +
                    ",Timestamp = " + entry.get("time_stamp") + ";";

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
                    int etime = rs.getInt("EndTime");
                    int ts = rs.getInt("Timestamp");
                    entry.put("ip", ip);
                    entry.put("port", Integer.toString(port));
                    entry.put("scoreboard_port", Integer.toString(sbport));
                    entry.put("judge_password", jpw);
                    entry.put("start_time", Integer.toString(stime));
                    entry.put("end_time", Integer.toString(etime));
                    entry.put("time_stamp", Integer.toString(ts));
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
