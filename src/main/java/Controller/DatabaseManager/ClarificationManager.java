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
                "(ClarificationID INT PRIMARY KEY   NOT NULL," +
                " ProblemID INT NOT NULL, " +
                " Content   STRING  NOT NULL, " +
                " Timestamp INT NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void addEntry(Map<String, String> entry) {
        Connection c = null;
        Statement stmt = null;
        String common = "INSERT INTO Clarification (ClarificationID,ProblemID,Content,Timestamp) ";
        String sql = common + "VALUES (" + entry.get("clarification_id") + ", " +
            entry.get("problem_id") + ", '" +
            entry.get("content") + "', " +
            entry.get("time_stamp") + ");";

        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:clarification.db");
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
                System.exit(0);
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
                    int pid = rs.getInt("ProblemID");
                    int time = rs.getInt("Timestamp");
                    String content = rs.getString("Content");
                    entry.put("clarification_id", Integer.toString(cid));
                    entry.put("problem_id", Integer.toString(pid));
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
                System.exit(0);
            }
        }
        return response;
    }

    public List<Map<String, String>> sync(int time_stamp) {
        Connection c = null;
        Statement stmt = null;
        List<Map<String, String>> response = new ArrayList<Map<String, String>>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:clarification.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "SELECT * FROM Clarification WHERE Timestamp >= " + Integer.toString(time_stamp) + ";";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    int cid = rs.getInt("ClarificationID");
                    int pid = rs.getInt("ProblemID");
                    int time = rs.getInt("Timestamp");
                    String content = rs.getString("Content");
                    entry.put("clarification_id", Integer.toString(cid));
                    entry.put("problem_id", Integer.toString(pid));
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
                System.exit(0);
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
                //System.exit(0);
            }
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            //System.exit(0);
        }
    }
}
