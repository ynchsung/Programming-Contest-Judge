package Controller.DatabaseManager;

import java.sql.*;
import java.util.*;
import java.lang.String;

public class ProblemManager {
    final int sleepTime = 200;

    public void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:problem.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Problem " +
                "(ProblemID STRING PRIMARY KEY NOT NULL," +
                " TimeLimit DOUBLE  NOT NULL," +
                " MemoryLimit   INT NOT NULL," +
                " Input     STRING  NOT NULL," +
                " Output    STRING  NOT NULL," +
                " SpecialJudge  STRING," +
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
        Statement stmt = null;
        String common = "INSERT INTO Problem (ProblemID,TimeLimit,MemoryLimit,Input,Output,SpecialJudge,Timestamp) ";
        String sql = common + "VALUES ('" + entry.get("problem_id") + "', " +
            entry.get("time_limit") + ", " +
            entry.get("memory_limit") + ", '" +
            entry.get("input") + "', '" +
            entry.get("output") + "', '" +
            entry.get("special_judge") + "', " +
            entry.get("time_stamp") + ");";

        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:problem.db");
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
                c = DriverManager.getConnection("jdbc:sqlite:problem.db");
                c.setAutoCommit(false);

                int nTS = Integer.parseInt(entry.get("time_stamp"));

                stmt = c.createStatement();
                String sql = "SELECT Timestamp FROM Problem WHERE ProblemID = '" + entry.get("problem_id") + "';";
                ResultSet rs = stmt.executeQuery(sql);

                if (!rs.next() || rs.getInt("Timestamp") > nTS) {
                    rs.close();
                    stmt.close();
                    c.close();
                    break;
                }
                rs.close();
                stmt.close();

                List<String> setType = new ArrayList<String>();
                List<String> setValue = new ArrayList<String>();
                if (entry.get("time_limit") != null) {
                    setType.add("TimeLimit");
                    setValue.add(entry.get("time_limit"));
                }
                if (entry.get("memory_limit") != null) {
                    setType.add("MemoryLimit");
                    setValue.add(entry.get("memory_limit"));
                }
                if (entry.get("input") != null) {
                    setType.add("Input");
                    setValue.add("'" + entry.get("input") + "'");
                }
                if (entry.get("output") != null) {
                    setType.add("Output");
                    setValue.add("'" + entry.get("output") + "'");
                }
                if (entry.get("special_judge") != null) {
                    setType.add("SpecialJudge");
                    setValue.add("'" + entry.get("special_judge") + "'");
                }

                sql = "UPDATE Problem SET ";
                for (int i = 0; i < setType.size(); i++) {
                    sql += setType.get(i) + " = " + setValue.get(i) + ",";
                }
                sql +=  "Timestamp = " + entry.get("time_stamp") + " WHERE ProblemID = '" + entry.get("problem_id") + "';";

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
                c = DriverManager.getConnection("jdbc:sqlite:problem.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM Problem;" );
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    String pid = rs.getString("ProblemID");
                    int time = rs.getInt("Timestamp");
                    int me = rs.getInt("MemoryLimit");
                    double te = rs.getDouble("TimeLimit");
                    String input = rs.getString("Input");
                    String output = rs.getString("Output");
                    String sj = rs.getString("SpecialJudge");
                    entry.put("problem_id", pid);
                    entry.put("time_stamp", Integer.toString(time));
                    entry.put("memory_limit", Integer.toString(me));
                    entry.put("time_limit", Double.toString(te));
                    entry.put("input", input);
                    entry.put("output", output);
                    entry.put("special_judge", sj);
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
        Statement stmt = null;
        List<Map<String, String>> response = new ArrayList<Map<String, String>>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:problem.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "SELECT * FROM Problem WHERE Timestamp >= " + Integer.toString(time_stamp) + ";";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    String pid = rs.getString("ProblemID");
                    int time = rs.getInt("Timestamp");
                    int me = rs.getInt("MemoryLimit");
                    double te = rs.getDouble("TimeLimit");
                    String input = rs.getString("Input");
                    String output = rs.getString("Output");
                    String sj = rs.getString("SpecialJudge");
                    entry.put("problem_id", pid);
                    entry.put("time_stamp", Integer.toString(time));
                    entry.put("memory_limit", Integer.toString(me));
                    entry.put("time_limit", Double.toString(te));
                    entry.put("input", input);
                    entry.put("output", output);
                    entry.put("special_judge", sj);
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
    public Map<String, String> getProblemById(String problem_id) {
        Connection c = null;
        Statement s = null;
        Map<String, String> response = new HashMap<String, String>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:problem.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "SELECT * FROM Problem WHERE ProblemID = " + problem_id + ";";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    String pid = rs.getString("ProblemID");
                    int time = rs.getInt("Timestamp");
                    int me = rs.getInt("MemoryLimit");
                    double te = rs.getDouble("TimeLimit");
                    String input = rs.getString("Input");
                    String output = rs.getString("Output");
                    String sj = rs.getString("SpecialJudge");
                    response.put("problem_id", pid);
                    response.put("time_stamp", Integer.toString(time));
                    response.put("memory_limit", Integer.toString(me));
                    response.put("time_limit", Double.toString(te));
                    response.put("input", input);
                    response.put("output", output);
                    response.put("special_judge", sj);
                    
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
