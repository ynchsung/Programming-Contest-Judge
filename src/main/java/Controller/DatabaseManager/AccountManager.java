package Controller.DatabaseManager;

import java.sql.*;
import java.util.*;
import java.lang.String;

public class AccountManager {
    final int sleepTime = 200;

    public void createTable() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:account.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Account " +
                "(Account STRING PRIMARY KEY    NOT NULL," +
                " Password  STRING  NOT NULL," +
                " Type      STRING  NOT NULL)";
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
        String common = "INSERT INTO Account (Account,Password,Type) ";
        String sql = common + "VALUES ('" + entry.get("account") + "', '" +
            entry.get("password") + "', '" +
            entry.get("type") + "');";

        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:account.db");
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
                c = DriverManager.getConnection("jdbc:sqlite:account.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM Account;" );
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    String id = rs.getString("Account");
                    String pw = rs.getString("Password");
                    String type = rs.getString("Type");
                    entry.put("account", id);
                    entry.put("password", pw);
                    entry.put("type", type);
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

    public boolean authenticateJudge(Map<String, String> login) {
        Connection c = null;
        Statement stmt = null;
        boolean response = false;
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:account.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "SELECT Account FROM Account WHERE Account = '" + login.get("account") +
                    "' AND Password = '" + login.get("password") +
                    "' AND Type = 'judge';";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    response = true;
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

    public boolean authenticateTeam(Map<String, String> login) {
        Connection c = null;
        Statement stmt = null;
        boolean response = false;
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:account.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                String sql = "SELECT Account FROM Account WHERE Account = '" + login.get("account") +
                    "' AND Password = '" + login.get("password") +
                    "' AND Type != 'judge';";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    response = true;
                }
                rs.close();
                stmt.close();
                c.close();
                break;
            } catch (SQLException e) {
                checkLock(e.getMessage());
                continue;
            } catch (Exception e) {
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
