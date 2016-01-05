package Participant;

import Participant.EventHandler.*;
import Shared.Connection;
import Shared.ControllerServer;
import Shared.EventHandler.EventHandler;
import Shared.EventHandler.LoginResultHandler;

public class PriticipantControllerServer extends ControllerServer {
    public PriticipantControllerServer(Connection connection, LoginResultHandler.LoginResultListener loginResultListener) {
        super(connection);
        EventHandler handler = new LoginResultHandler(loginResultListener,
                new ResultHandler(new AnswerHandler(new ClarificationHandler(new SyncHandler(new SyncTimeHandler(null))))));
        setEventHandler(handler);
    }

    @Override
    protected String getUserType() {
        return "participant";
    }
}
