package Judge;

import Judge.EventHandler.*;
import Judge.EventHandler.ClarificationHandler;
import Judge.EventHandler.SyncHandler;
import Judge.EventHandler.SyncProblemInfoHandler;
import Judge.EventHandler.SyncTimeHandler;
import Participant.EventHandler.*;
import Shared.Connection;
import Shared.ControllerServer;
import Shared.EventHandler.AnswerHandler;
import Shared.EventHandler.EventHandler;
import Shared.EventHandler.LoginResultHandler;

public class JudgeControllerServer extends ControllerServer {
    public JudgeControllerServer(Connection connection, LoginResultHandler.LoginResultListener loginResultListener) {
        super(connection);
        EventHandler handler = new LoginResultHandler(loginResultListener,
                new AnswerAckHandler(
                new SubmissionHandler(new QuestionHandler(new AnswerHandler(new ClarificationHandler(
                new SyncHandler(new SyncProblemInfoHandler(new SyncJudgeDataHandler(new SyncTimeHandler(null))))))))));
        setEventHandler(handler);
    }

    @Override
    protected String getUserType() {
        return "judge";
    }
}
