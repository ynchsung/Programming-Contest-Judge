package Participant;

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
