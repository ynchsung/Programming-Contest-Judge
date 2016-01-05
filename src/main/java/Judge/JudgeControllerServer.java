package Judge;

import Judge.EventHandler.*;
import Shared.Connection;
import Shared.ControllerServer;
import Shared.EventHandler.EventHandler;
import Shared.EventHandler.LoginResultHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class JudgeControllerServer extends ControllerServer {
    public JudgeControllerServer(Connection connection, LoginResultHandler.LoginResultListener loginResultListener) {
        super(connection);
        EventHandler handler = new LoginResultHandler(loginResultListener,
                new SubmissionHandler(new QuestionHandler(new AnswerHandler(new ClarificationHandler(
                new SyncHandler(new SyncJudgeDataHandler(new SyncTimeHandler(null))))))));
        setEventHandler(handler);
    }

    @Override
    protected String getUserType() {
        return "judge";
    }
}
