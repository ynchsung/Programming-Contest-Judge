package Participant;

import Shared.Connection;
import Shared.ContestTimer;
import Shared.EventHandler.LoginResultHandler;
import Shared.InfoManager.ClarificationManager;
import Shared.InfoManager.QAManager;
import Shared.InfoManager.SubmissionManager;
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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;

/**
 * Created by aalexx on 1/3/16.
 */
public class Participant extends Application {
    private Stage stage;
    ParticipantControllerServer server = null;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ParticipantLoginPage.fxml"));
        Parent root = loader.load();
        ParticipantLoginPageController controller = loader.getController();
        LoginResultHandler.LoginResultListener loginResultListener = success -> Platform.runLater(() -> {
            if (success) {
                loginSuccess();
            } else {
                if (server != null) {
                    server.logout();
                }
                System.err.println("login failed");
                //TODO Login fail msg
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
                server = new ParticipantControllerServer(connection, loginResultListener);
                connection.setControllerServer(server);
                connection.start();
                server.login(username, password);
            } catch (IOException e) {
                loginResultListener.callback(false);
            }
        });
        primaryStage.setTitle("Participant");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    /*
     * Change scene to judge's main page.
     */
    public void loginSuccess () {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Participant.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        MainController controller = loader.getController();
        ParticipantCore core = ParticipantCore.getInstance();
        RemainingTime remainingTime = controller.getRemainingTimeController();
        core.setControllerServer(server);
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
        ClarificationManager.register(() -> {
            viewClarificationController.setClarification((new ClarificationManager()).queryAll());
            Platform.runLater(() -> {
                NewClarificationAlert alert = new NewClarificationAlert(stage);
                alert.show();
            });
        });

        ViewQuestionAndAnswerController viewQuestionAndAnswerController = controller.getViewQuestionAndAnswerController();
        viewQuestionAndAnswerController.setQuestionAndAnswer((new QAManager()).queryAll());
        QAManager.register(() -> {
            viewQuestionAndAnswerController.setQuestionAndAnswer((new QAManager()).queryAll());
            Platform.runLater(() -> {
                NewAnswerAlert alert = new NewAnswerAlert(stage);
                alert.show();
            });
        });
        viewQuestionAndAnswerController.setConfirmNewQuestionAction(event -> {
            String problemID = (String) viewQuestionAndAnswerController.getSelectedProblem();
            if (problemID == null) problemID = "0";
            String content = viewQuestionAndAnswerController.getAskQuestionTextArea();
            core.sendQuestion(problemID, content);
        });

        ViewSubmissionController viewSubmissionController = controller.getViewSubmissionController();
        viewSubmissionController.setSubmissions((new SubmissionManager()).queryAll());
        SubmissionManager.register(() -> {
            viewSubmissionController.setSubmissions((new SubmissionManager()).queryAll());
            Platform.runLater(() -> {
                NewResultAlert alert = new NewResultAlert(stage);
                alert.show();
            });
        });
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

        SubmitController submitController = controller.getSubmitController();
        submitController.setOnConfirmAction(param -> {
            String problemId = param.get("problem_id");
            if (problemId == null)
                problemId = "pA";
            String sourceCode = param.get("source_code");
            core.sendSubmission(problemId, sourceCode);
            return null;
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
