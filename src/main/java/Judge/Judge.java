package Judge;

import Controller.DatabaseManager.ClarificationManager;
import Judge.InfoManager.SubmissionManager;
import Judge.EventHandler.LoginResultHandler;
import Shared.ContestTimer;
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

/**
 * Created by aalexx on 1/2/16.
 */
public class Judge extends Application {
    private Stage stage;
    ControllerServer server = null;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("JudgeLoginPage.fxml"));
        Parent root = loader.load();
        JudgeLoginPageController controller = loader.getController();
        LoginResultHandler.LoginResultListener loginResultListener = success -> Platform.runLater(() -> {
            if (success) {
                loginSuccess();
            }
            else {
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
                server = new ControllerServer(connection, loginResultListener);
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
            //TODO: send Clarification
        });

        ViewQuestionAndAnswerController viewQuestionAndAnswerController = controller.getViewQuestionAndAnswerController();

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
            public Void call(String param) {
                //judge_submission
                return null;
            }
        });
        viewSubmissionController.setManualJudgeCallBack(new Callback<String, Void>() {
            @Override
            public Void call(String param) {
                //judge_submission
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
