import java.sql.*;
import java.util.*;
import java.lang.String;

public class QAManager {
    final int sleepTime = 200;

    public void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:qa.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Question " +
                "(QuestionID INT PRIMARY KEY    NOT NULL," +
                " ProblemID INT NOT NULL, " +
                " TeamID    INT NOT NULL, " +
                " Content   STRING  NOT NULL, " +
                " Timestamp INT)";
            stmt.executeUpdate(sql);
            stmt.close();

            stmt = c.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS Answer " +
                "(AnswerID INT PRIMARY KEY  NOT NULL," +
                " QuestionID    INT NOT NULL, " +
                " Content   STRING  NOT NULL, " +
                " Timestamp INT)";
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
        String sql = new String();
        if (entry.get("answer_id") == null) {
            String common = "INSERT INTO Question (QuestionID,ProblemID,TeamID,Content,Timestamp) ";
            sql = common + "VALUES (" + entry.get("question_id") + ", " +
                entry.get("problem_id") + ", " +
                entry.get("team_id") + ", '" +
                entry.get("content") + "', " +
                entry.get("time_stamp") + ");";
        }
        else {
            String common = "INSERT INTO Answer (AnswerID,QuestionID,Content,Timestamp) ";
            sql = common + "VALUES (" + entry.get("answer_id") + ", " +
                entry.get("question_id") + ", '" +
                entry.get("answer") + "', " +
                entry.get("time_stamp") + ");";
        }

        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:qa.db");
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
                c = DriverManager.getConnection("jdbc:sqlite:qa.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM Question;" );
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    int qid = rs.getInt("QuestionID");
                    int pid = rs.getInt("ProblemID");
                    String content = rs.getString("Content");
                    int time = rs.getInt("Timestamp");
                    entry.put("question_id", Integer.toString(qid));
                    entry.put("problem_id", Integer.toString(pid));
                    entry.put("content", content);
                    entry.put("time_stamp", Integer.toString(time));
                    response.add(entry);
                }
                rs.close();
                stmt.close();

                stmt = c.createStatement();
                rs = stmt.executeQuery( "SELECT * FROM Answer;" );
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    int aid = rs.getInt("AnswerID");
                    int qid = rs.getInt("QuestionID");
                    String answer = rs.getString("Answer");
                    int time = rs.getInt("Timestamp");
                    entry.put("answer_id", Integer.toString(aid));
                    entry.put("question_id", Integer.toString(qid));
                    entry.put("answer", answer);
                    entry.put("time_stamp", Integer.toString(time));
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

    public List<Map<String, String>> syncParticipant(int team_id, int time_stamp) {
        Connection c = null;
        Statement stmt = null;
        List<Map<String, String>> response = new ArrayList<Map<String, String>>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:qa.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "SELECT * FROM Question" + " WHERE TeamID = " + Integer.toString(team_id) +
                    " AND Timestamp >= " + Integer.toString(time_stamp) + ";";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    int qid = rs.getInt("QuestionID");
                    int pid = rs.getInt("ProblemID");
                    String content = rs.getString("Content");
                    int time = rs.getInt("Timestamp");
                    entry.put("question_id", Integer.toString(qid));
                    entry.put("problem_id", Integer.toString(pid));
                    entry.put("content", content);
                    entry.put("time_stamp", Integer.toString(time));
                    response.add(entry);
                }
                rs.close();
                stmt.close();

                stmt = c.createStatement();
                sql = "SELECT AnswerID, Answer.QuestionID, Answer.Content, Answer.Timestamp FROM Question, Answer" +
                    " WHERE Question.QuestionID = Answer.QuestionID AND TeamID = " + Integer.toString(team_id) +
                    " AND Answer.Timestamp >= " + Integer.toString(time_stamp) + ";";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    int aid = rs.getInt("AnswerID");
                    int qid = rs.getInt("QuestionID");
                    String answer = rs.getString("Content");
                    int time = rs.getInt("Timestamp");
                    entry.put("answer_id", Integer.toString(aid));
                    entry.put("question_id", Integer.toString(qid));
                    entry.put("answer", answer);
                    entry.put("time_stamp", Integer.toString(time));
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

    public List<Map<String, String>> syncJudge(int time_stamp) {
        Connection c = null;
        Statement stmt = null;
        List<Map<String, String>> response = new ArrayList<Map<String, String>>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:qa.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "SELECT * FROM Question WHERE Timestamp >= " + Integer.toString(time_stamp) + ";";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    int qid = rs.getInt("QuestionID");
                    int pid = rs.getInt("ProblemID");
                    String content = rs.getString("Content");
                    int time = rs.getInt("Timestamp");
                    entry.put("question_id", Integer.toString(qid));
                    entry.put("problem_id", Integer.toString(pid));
                    entry.put("content", content);
                    entry.put("time_stamp", Integer.toString(time));
                    response.add(entry);
                }
                rs.close();
                stmt.close();

                stmt = c.createStatement();
                sql = "SELECT * FROM Answer WHERE Timestamp >= " + Integer.toString(time_stamp) + ";";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    int aid = rs.getInt("AnswerID");
                    int qid = rs.getInt("QuestionID");
                    String answer = rs.getString("Content");
                    int time = rs.getInt("Timestamp");
                    entry.put("answer_id", Integer.toString(aid));
                    entry.put("question_id", Integer.toString(qid));
                    entry.put("answer", answer);
                    entry.put("time_stamp", Integer.toString(time));
                    response.add(entry);
                }
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
