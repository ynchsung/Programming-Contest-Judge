package Participant;

import Participant.EventHandler.*;
import Shared.Connection;
import Shared.ControllerServer;
import Shared.EventHandler.AnswerHandler;
import Shared.EventHandler.EventHandler;
import Shared.EventHandler.LoginResultHandler;

public class ParticipantControllerServer extends ControllerServer {
    public ParticipantControllerServer(Connection connection, LoginResultHandler.LoginResultListener loginResultListener) {
        super(connection);
        EventHandler handler = new LoginResultHandler(loginResultListener,
                new QuestionAckHandler(new SubmissionAckHandler(
                new ResultHandler(new AnswerHandler(
                new ClarificationHandler(new SyncHandler(
                new SyncProblemInfoHandler(
                new SyncTimeHandler(null)))))))));
        setEventHandler(handler);
    }

    @Override
    protected String getUserType() {
        return "participant";
    }
}
