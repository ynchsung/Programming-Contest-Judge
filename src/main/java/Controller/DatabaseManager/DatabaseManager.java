package Controller.DatabaseManager;
import java.util.*;
import java.sql.*;
import java.lang.String;

public abstract class DatabaseManager {
    final int sleep_time = 200;

    public abstract void createTable();
    public abstract List<Map<String, String>> queryAll();
    public abstract void flushTable();

    protected boolean checkLock(String message, Connection c) {
        try {
            if (c != null)
                c.close();
            if (message == null) 
                return false;
            if (message.equals("database is locked") || message.startsWith("[SQLITE_BUSY]")) {
                Thread.sleep(sleep_time);
                return true;
            }
            else {
                System.err.println("Exception: " + message);
            }
        }
        catch (Exception e) {
            System.err.println("Exception in checkLock: " +e.getClass().getName() + ": " + e.getMessage());
        }
        return false;
    }
}
