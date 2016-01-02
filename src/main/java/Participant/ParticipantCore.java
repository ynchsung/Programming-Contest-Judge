package Participant;

import Participant.DatabaseManager.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParticipantCore {
    static private ParticipantCore sharedInstance = null;

    private ControllerServer controllerServer;

    private ParticipantCore(ControllerServer controllerServer) {
        this.controllerServer = controllerServer;
    }

    static public ParticipantCore getInstance() {
        return sharedInstance;
    }

    static public void run(ControllerServer controllerServer) {
        if (sharedInstance != null)
            return;
        sharedInstance = new ParticipantCore(controllerServer);
        sharedInstance.start();
    }

    private void start() {
    }
}
