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
            c = DriverManager.getConnection("jdbc:sqlite:teamAccount.db");

            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Account " +
                "(Account STRING PRIMARY KEY    NOT NULL," +
                " Password  STRING  NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();

            c = DriverManager.getConnection("jdbc:sqlite:judgeAccount.db");

            stmt = c.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS Account " +
                "(Account STRING PRIMARY KEY    NOT NULL," +
                " Password  STRING  NOT NULL)";
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
                c = DriverManager.getConnection("jdbc:sqlite:" + entry.get("type") + "Account.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("INSERT INTO Account (Account, Password) VALUES(?, ?);");
                stmt.setString(1, entry.get("account"));
                stmt.setString(2, entry.get("password"));
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
                c = DriverManager.getConnection("jdbc:sqlite:teamAccount.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM Account;" );
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    String id = rs.getString("Account");
                    String pw = rs.getString("Password");
                    entry.put("account", id);
                    entry.put("password", pw);
                    entry.put("type", "team");
                    response.add(entry);
                }
                rs.close();
                stmt.close();
                c.close();
                
                c = DriverManager.getConnection("jdbc:sqlite:judgeAccount.db");
                c.setAutoCommit(false);

                stmt = c.createStatement();
                rs = stmt.executeQuery( "SELECT * FROM Account;" );
                while (rs.next()) {
                    Map<String, String> entry = new HashMap<String, String>();

                    String id = rs.getString("Account");
                    String pw = rs.getString("Password");
                    entry.put("account", id);
                    entry.put("password", pw);
                    entry.put("type", "judge");
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

    public boolean authenticateJudge(String account, String password) {
        Connection c = null;
        PreparedStatement stmt = null;
        boolean response = false;
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:judgeAccount.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("SELECT Account FROM Account WHERE Account = ? AND Password = ?;");
                stmt.setString(1, account);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
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

    public boolean authenticateTeam(String account, String password) {
        Connection c = null;
        PreparedStatement stmt = null;
        boolean response = false;
        while (true) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:teamAccount.db");
                c.setAutoCommit(false);

                stmt = c.prepareStatement("SELECT Account FROM Account WHERE Account = ? AND Password = ?;");
                stmt.setString(1, account);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
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
