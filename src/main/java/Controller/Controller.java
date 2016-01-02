package Controller;

import Controller.DatabaseManager.ClarificationManager;
import Controller.DatabaseManager.ProblemManager;
import Controller.DatabaseManager.QAManager;
import Controller.DatabaseManager.SubmissionManager;
import Controller.EventHandler.EventHandler;
import SharedGuiElement.OpenCode;
import SharedGuiElement.OpenCodeBuilder;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class Controller extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Controller.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();

        /* bind UI */
        Core core = Core.getInstance();
        // GeneralController
        GeneralController generalController = controller.getGeneralController();
        generalController.setIp(core.getIP().getHostAddress());
        generalController.setPort(core.getPort());
        generalController.setScoreBoardPort(core.getScoreboardPort());

        // RemainingTimeController
        core.getTimer().setListener(new ContestTimer.ContestTimerListener() {
            @Override
            public void onUpdate(int totalSecond, int secondCounted) {
                controller.getRemainingTimeController().setRemainingTime(totalSecond - secondCounted);
            }

            @Override
            public void onOver(int totalSecond) {
                // TODO: end the game
            }
        });

        // TimeUpdateController
        TimeUpdateController timeUpdateController = controller.getTimeUpdateController();
        timeUpdateController.setStartTime(System.currentTimeMillis() / 1000L);
        timeUpdateController.setDuration(Core.getInstance().getTimer().getDuration() / 60);
        timeUpdateController.setOnConfirmAction(event -> core.getTimer().setDuration(timeUpdateController.getDuration()*60));

        ArrayList<String> problems = core.getProblemIDList();
        // TestDataUpdateController
        TestDataUpdateController testDataUpdateController = controller.getTestDataUpdateController();
        testDataUpdateController.setProblemChoice(problems);
        testDataUpdateController.setOnConfirmAction(event -> {
            String problemID = (String)testDataUpdateController.getProblemChoice();
            File inputFile = testDataUpdateController.getInputData();
            File outputFile = testDataUpdateController.getOutputData();
            if (inputFile == null || outputFile == null) return;
            File specialFile = null;
            if (testDataUpdateController.specialJudgeIsSelected()) {
                specialFile = testDataUpdateController.getSpecialJudgeCode();
            }
            core.updateTestData(problemID, inputFile, outputFile, specialFile);
        });

        // viewSubmission example
        ViewSubmissionController viewSubmissionController = controller.getViewSubmissionController();
        viewSubmissionController.setRejudgeProblemChoice(problems);
        viewSubmissionController.setRejudgeButtonOnAction(event -> {
            // TODO: rejudge
            System.out.println("selected=" + viewSubmissionController.getRejudgeProblemChoiceNumber());
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
        viewSubmissionController.setSubmissions((new SubmissionManager()).queryAll());

        // QA Controller
        ViewQuestionAndAnswerController viewQuestionAndAnswerController = controller.getViewQuestionAndAnswerController();
        //viewQuestionAndAnswerController.setQuestionAndAnswer((new QAManager()).queryAll());

        // ClarificationController
        ViewClarificationController viewClarificationController = controller.getViewClarificationController();
        viewClarificationController.setClarification((new ClarificationManager()).queryAll());

        primaryStage.setTitle("Controller");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        core.start();
    }
    static public void main(String [] argv) {
        launch(argv);
    }
}
