package Judge;

import Judge.DatabaseManager.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JudgeCore {
    static private JudgeCore sharedInstance = null;

    private ControllerServer controllerServer;

    private JudgeCore(ControllerServer controllerServer) {
        this.controllerServer = controllerServer;
    }

    static public JudgeCore getInstance() {
        return sharedInstance;
    }

    static public void run(ControllerServer controllerServer) {
        if (sharedInstance != null)
            return;
        sharedInstance = new JudgeCore(controllerServer);
        sharedInstance.start();
    }

    private void start() {
    }
}
