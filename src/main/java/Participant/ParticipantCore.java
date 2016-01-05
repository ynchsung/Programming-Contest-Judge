package Participant;

import Shared.AckQueue;
import Shared.ContestTimer;

public class ParticipantCore {
    static private ParticipantCore sharedInstance = null;

    private PriticipantControllerServer controllerServer;
    private ContestTimer timer;
    private AckQueue submitQueue;
    private AckQueue questionQueue;

    private ParticipantCore() {
        timer = new ContestTimer(300*60);
    }

    public void setControllerServer(PriticipantControllerServer controllerServer) {
        this.controllerServer = controllerServer;
        submitQueue = new AckQueue(controllerServer);
    }

    static public ParticipantCore getInstance() {
        if (sharedInstance == null) {
            synchronized (ParticipantCore.class) {
                if (sharedInstance == null) {
                    sharedInstance = new ParticipantCore();
                }
            }
        }
        return sharedInstance;
    }

    public void start() {
    }

    public ContestTimer getTimer() {
        return timer;
    }
}
