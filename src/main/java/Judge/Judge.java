package Judge;

import Shared.Connection;
import Shared.InfoManager.ClarificationManager;
import Shared.InfoManager.ProblemManager;
import Shared.InfoManager.QAManager;
import Shared.InfoManager.SubmissionManager;
import Shared.EventHandler.LoginResultHandler;
import Shared.ContestTimer;
import Shared.SubmissionInfo;
import SharedGuiElement.GuiCloseEventHandler;
import SharedGuiElement.OpenCode;
import SharedGuiElement.OpenCodeBuilder;
import SharedGuiElement.RemainingTime;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;

/**
 * Created by aalexx on 1/2/16.
 */
public class Judge extends Application {
    private Stage stage;
    JudgeControllerServer server = null;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("JudgeLoginPage.fxml"));
        Parent root = loader.load();
        JudgeLoginPageController controller = loader.getController();

        /* on close event */
        primaryStage.setOnCloseRequest(event -> {
            GuiCloseEventHandler handle = new GuiCloseEventHandler(primaryStage, event);
            handle.setOncloseAction(windowEvent -> {
                System.exit(0);
            });
            handle.onClose();
        });

        LoginResultHandler.LoginResultListener loginResultListener = success -> Platform.runLater(() -> {
            if (success) {
                loginSuccess();
            }
            else {
                if (server != null) {
                    server.logout();
                }
                System.err.println("login failed");
                controller.setMessage("Login failed");
            }
        });
        controller.setLoginButtonOnAction(event -> {
            String ip = controller.getIp();
            int port = Integer.valueOf(controller.getPort());
            String username = controller.getAccount();
            String password = controller.getPassword();
            Socket socket = new Socket();
            try {
                socket.connect(new InetSocketAddress(ip, port));
                Connection connection = new Connection(socket);
                server = new JudgeControllerServer(connection, loginResultListener);
                connection.setControllerServer(server);
                connection.start();
                server.login(username, password);
            } catch (IOException e) {
                loginResultListener.callback(false);
            }
        });
        primaryStage.setTitle("Judge");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    /*
     * Change scene to judge's main page.
     */
    public void loginSuccess () {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Judge.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        MainController controller = loader.getController();
        JudgeCore core = JudgeCore.getInstance();
        core.setControllerServer(server);

        RemainingTime remainingTime = controller.getRemainingTimeController();
        core.getTimer().setListener(new ContestTimer.ContestTimerListener() {
            @Override
            public void onUpdate(int totalSecond, int secondCounted) {
                remainingTime.setRemainingTime(totalSecond - secondCounted);
            }

            @Override
            public void onOver(int totalSecond) {
                //TODO: contest over
            }
        });

        ViewClarificationController viewClarificationController = controller.getViewClarificationController();
        viewClarificationController.setClarification((new ClarificationManager()).queryAll());
        ClarificationManager.register(() -> viewClarificationController.setClarification((new ClarificationManager()).queryAll()));
        viewClarificationController.setConfirmNewClarificationAction(event -> {
            String problemId = (String) viewClarificationController.getProblemChoice();
            if (problemId == null)
                problemId = "0";
            String content = viewClarificationController.getNewClarificationText();
            core.sendClarification(problemId, content);
        });
        viewClarificationController.setProblemChoice((new ProblemManager()).queryAllId());
        ProblemManager.register(() -> viewClarificationController.setProblemChoice((new ProblemManager()).queryAllId()));

        ViewQuestionAndAnswerController viewQuestionAndAnswerController = controller.getViewQuestionAndAnswerController();
        viewQuestionAndAnswerController.setQuestionAndAnswer((new QAManager()).queryAll());
        QAManager.register(() -> viewQuestionAndAnswerController.setQuestionAndAnswer((new QAManager()).queryAll()));
        viewQuestionAndAnswerController.setAnswerQuestionCallBack(new Callback<Map<String, String>, Void>() {
            @Override
            public Void call(Map<String, String> answer) {
                String questionId = answer.get("questionId");
                String content = answer.get("content");
                core.sendAnswer(questionId, content);
                return null;
            }
        });

        ViewSubmissionController viewSubmissionController = controller.getViewSubmissionController();
        viewSubmissionController.setSubmissions((new SubmissionManager()).queryAll());
        SubmissionManager.register(() -> viewSubmissionController.setSubmissions((new SubmissionManager()).queryAll()));
        viewSubmissionController.setOpenCodeCallBack(new Callback<String, Void>() {
            @Override
            public Void call(String submissionId) {
                SubmissionManager submissionManager = new SubmissionManager();
                String code = submissionManager.queryCode(Integer.valueOf(submissionId));
                OpenCode o = OpenCodeBuilder.create()
                        .setCode(code)
                        .build();
                o.show();
                return null;
            }
        });
        viewSubmissionController.setAutojudgeCallBack(new Callback<String, Void>() {
            @Override
            public Void call(String submissionID) {
                JudgeCore.getInstance().rejudgeSubmission(Integer.valueOf(submissionID));
                return null;
            }
        });
        viewSubmissionController.setManualJudgeCallBack(new Callback<String, Void>() {
            @Override
            public Void call(String submissionID) {
                SubmissionManager submissionManager = new SubmissionManager();
                SubmissionInfo submissionInfo = submissionManager.getSubmissionByID(Integer.valueOf(submissionID));
                ManualJudgePage manualJudgePage = new ManualJudgePage(submissionID, submissionInfo.getSourceCode());
                manualJudgePage.setOnConfirm(new Callback<Map<String, String>, Void>() {
                    @Override
                    public Void call(Map<String, String> param) {
                        try {
                            JSONObject msg = new JSONObject();
                            ProblemManager problemManager = new ProblemManager();
                            msg.put("msg_type", "result");
                            msg.put("submission_id", String.valueOf(submissionID));
                            msg.put("problem_id", submissionInfo.getProblemID());
                            msg.put("result", param.get("result"));
                            msg.put("submit_time_stamp", String.valueOf(submissionInfo.getSubmitTimeStamp()));
                            msg.put("testdata_time_stamp", String.valueOf(problemManager.getProblemById(submissionInfo.getProblemID()).getTestDataTimeStamp()));
                            JudgeCore.getInstance().sendResult(msg);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
                manualJudgePage.show();
                return null;
            }
        });

        core.start();
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(root, 800, 600);
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(root);
        }
        stage.sizeToScene();
    }

    public static void main (String[] args) {
        launch(args);
    }

}
