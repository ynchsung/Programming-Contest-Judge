package Controller.DatabaseManager;

import java.sql.*;
import java.util.*;
import java.lang.String;

public class QAManager {
    final int sleepTime = 200;
    private static List<Observer> observers = new ArrayList<Observer>();

    public void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:qa.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Question " +
                "(QuestionID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ProblemID STRING NOT NULL, " +
                " TeamID    STRING NOT NULL, " +
                " Content   STRING  NOT NULL, " +
                " Timestamp INT)";
            stmt.executeUpdate(sql);
            stmt.close();

            stmt = c.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS Answer " +
                "(AnswerID INTEGER PRIMARY KEY AUTOINCREMENT," +
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

    public int addEntry(Map<String, String> entry) {
        Connection c = null;
        PreparedStatement stmt = null;
        int id = -1;

        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:qa.db");
                c.setAutoCommit(false);

                if (entry.get("type").equals("question")) {
                    stmt = c.prepareStatement("INSERT INTO Question (ProblemID,TeamID,Content,Timestamp) VALUES (?, ?, ?, ?);", 
                            Statement.RETURN_GENERATED_KEYS);
                    stmt.setString(1, entry.get("problem_id"));
                    stmt.setString(2, entry.get("team_id"));
                    stmt.setString(3, entry.get("content"));
                    stmt.setString(4, entry.get("time_stamp"));
                }
                else {
                    String common = "INSERT INTO Answer (QuestionID,Content,Timestamp) ";
                    stmt = c.prepareStatement("INSERT INTO Answer (QuestionID,Content,Timestamp) VALUES (?, ?, ?);", 
                            Statement.RETURN_GENERATED_KEYS);
                    stmt.setString(1, entry.get("question_id"));
                    stmt.setString(2, entry.get("answer"));
                    stmt.setString(3, entry.get("time_stamp"));
                }
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
        return id;
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
                    String pid = rs.getString("ProblemID");
                    String content = rs.getString("Content");
                    int time = rs.getInt("Timestamp");
                    entry.put("type", "question");
                    entry.put("question_id", Integer.toString(qid));
                    entry.put("problem_id", pid);
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
                    entry.put("type", "answer");
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
            catch (Exception e) {
                if (checkLock(e.getMessage(), c))
                    continue;
                else
                    break;
            }
        }
        return response;
    }

    public List<Map<String, String>> syncQuestion(String team_id, int time_stamp) {
        Connection c = null;
        PreparedStatement stmt = null;
        List<Map<String, String>> response = new ArrayList<Map<String, String>>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:qa.db");
                c.setAutoCommit(false);

                if (team_id.equals("")) {
                    stmt = c.prepareStatement("SELECT * FROM Question WHERE Timestamp >= ?;");
                    stmt.setString(1, Integer.toString(time_stamp));
                }
                else {
                    stmt = c.prepareStatement("SELECT * FROM Question WHERE TeamID = ? AND Timestamp >= ?;");
                    stmt.setString(1, team_id);
                    stmt.setString(2, Integer.toString(time_stamp));
                }
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    int qid = rs.getInt("QuestionID");
                    String tid = rs.getString("TeamID");
                    String pid = rs.getString("ProblemID");
                    String content = rs.getString("Content");
                    int time = rs.getInt("Timestamp");
                    entry.put("team_id", tid);
                    entry.put("question_id", Integer.toString(qid));
                    entry.put("problem_id", pid);
                    entry.put("content", content);
                    entry.put("time_stamp", Integer.toString(time));
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

    public List<Map<String, String>> syncAnswer(String team_id, int time_stamp) {
        Connection c = null;
        PreparedStatement stmt = null;
        List<Map<String, String>> response = new ArrayList<Map<String, String>>();
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:qa.db");
                c.setAutoCommit(false);

                if (team_id.equals("")) {
                    stmt = c.prepareStatement("SELECT AnswerID, Answer.QuestionID, Answer.Content, Answer.Timestamp, TeamID FROM Question," +
                            " Answer WHERE Question.QuestionID = Answer.QuestionID AND Answer.Timestamp >= ?;");
                    stmt.setString(1, Integer.toString(time_stamp));
                }
                else {
                    stmt = c.prepareStatement("SELECT AnswerID, Answer.QuestionID, Answer.Content, Answer.Timestamp, TeamID FROM Question," +
                            " Answer WHERE Question.QuestionID = Answer.QuestionID AND TeamID = ? AND Answer.Timestamp >= ?;");
                    stmt.setString(1, team_id);
                    stmt.setString(2, Integer.toString(time_stamp));
                }
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    int aid = rs.getInt("AnswerID");
                    int qid = rs.getInt("QuestionID");
                    String tid = rs.getString("TeamID");
                    String answer = rs.getString("Content");
                    int time = rs.getInt("Timestamp");
                    entry.put("answer_id", Integer.toString(aid));
                    entry.put("question_id", Integer.toString(qid));
                    entry.put("team_id", tid);
                    entry.put("answer", answer);
                    entry.put("time_stamp", Integer.toString(time));
                    response.add(entry);
                }
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

    public void register(Observer observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
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
                c = DriverManager.getConnection("jdbc:sqlite:qa.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "DROP TABLE IF EXISTS Question;";
                stmt.executeUpdate(sql);
                stmt.close();
                c.commit();
                
                stmt = c.createStatement();
                sql = "DROP TABLE IF EXISTS Answer;";
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
                System.err.println("SQLException: " + message);
            }
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return false;
    }
}
