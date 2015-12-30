package Controller.DatabaseManager;

import java.sql.*;
import java.util.*;
import java.lang.String;

public class ClarificationManager {
    final int sleepTime = 200;

    public void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:clarification.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Clarification " +
                "(ClarificationID INTEGER PRIMARY KEY AUTOINCREMENT," +
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

    public int addEntry(Map<String, String> entry) {
        Connection c = null;
        PreparedStatement stmt = null;
        int id = -1;

        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:clarification.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("INSERT INTO Clarification (ProblemID,Content,Timestamp) VALUES (?, ?, ?);", 
                                Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, entry.get("problem_id"));
                stmt.setString(2, entry.get("content"));
                stmt.setString(3, entry.get("time_stamp"));
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
