package Judge.DatabaseManager;

import java.sql.*;
import java.util.*;
import java.lang.String;

public class ClarificationManager {
    final int sleepTime = 200;
    private static List<Observer> observers = new ArrayList<Observer>();

    public void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:clarification.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Clarification " +
                "(ClarificationID INT PRIMARY KEY NOT NULL," +
                " ProblemID STRING  NOT NULL, " +
                " Content   STRING  NOT NULL, " +
                " Timestamp INT NOT NULL)";
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
                c = DriverManager.getConnection("jdbc:sqlite:clarification.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("INSERT INTO Clarification (ClarificationID,ProblemID,Content,Timestamp) VALUES (?, ?, ?, ?);");
                stmt.setString(1, entry.get("clarification_id"));
                stmt.setString(2, entry.get("problem_id"));
                stmt.setString(3, entry.get("content"));
                stmt.setString(4, entry.get("time_stamp"));
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
                c = DriverManager.getConnection("jdbc:sqlite:clarification.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM Clarification;" );
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    int cid = rs.getInt("ClarificationID");
                    String pid = rs.getString("ProblemID");
                    int time = rs.getInt("Timestamp");
                    String content = rs.getString("Content");
                    entry.put("clarification_id", Integer.toString(cid));
                    entry.put("problem_id", pid);
                    entry.put("time_stamp", Integer.toString(time));
                    entry.put("content", content);
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

    public List<Map<String, String>> sync(int time_stamp) {
        Connection c = null;
        PreparedStatement stmt = null;
        List<Map<String, String>> response = new ArrayList<Map<String, String>>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:clarification.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("SELECT * FROM Clarification WHERE Timestamp >= ?;");
                stmt.setString(1, Integer.toString(time_stamp));
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    int cid = rs.getInt("ClarificationID");
                    String pid = rs.getString("ProblemID");
                    int time = rs.getInt("Timestamp");
                    String content = rs.getString("Content");
                    entry.put("clarification_id", Integer.toString(cid));
                    entry.put("problem_id", pid);
                    entry.put("time_stamp", Integer.toString(time));
                    entry.put("content", content);
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
                c = DriverManager.getConnection("jdbc:sqlite:clarification.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "DROP TABLE IF EXISTS Clarification;";
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
