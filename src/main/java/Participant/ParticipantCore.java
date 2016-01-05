package Participant;

import Shared.ContestTimer;

public class ParticipantCore {
    static private ParticipantCore sharedInstance = null;

    private ControllerServer controllerServer;
    private ContestTimer timer;

    private ParticipantCore(ControllerServer controllerServer) {
        this.controllerServer = controllerServer;
        timer = new ContestTimer(300*60);
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

    public ContestTimer getTimer() {
        return timer;
    }
}
