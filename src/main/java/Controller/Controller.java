package Controller;

import Controller.DatabaseManager.*;
import Shared.ContestTimer;
import SharedGuiElement.GuiCloseEventHandler;
import SharedGuiElement.OpenCode;
import SharedGuiElement.OpenCodeBuilder;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.io.File;
import java.util.ArrayList;

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

        /* on close event */
        primaryStage.setOnCloseRequest(event -> {
            GuiCloseEventHandler handle = new GuiCloseEventHandler(primaryStage, event);
            handle.setOncloseAction(windowEvent -> {
                Core.getInstance().halt();
                Core.getInstance().haltScoreBoardServer();
                System.exit(0);
            });
            handle.onClose();
        });


        // RemainingTimeController
        core.getTimer().setListener(new ContestTimer.ContestTimerListener() {
            @Override
            public void onUpdate(int totalSecond, int secondCounted) {
                controller.getRemainingTimeController().setRemainingTime(totalSecond - secondCounted);
            }

            @Override
            public void onOver(int totalSecond) {
                core.halt();
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
            Core.getInstance().rejudgeProblem(viewSubmissionController.getRejudgeProblemChoice());
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
        SubmissionManager.register(() -> viewSubmissionController.setSubmissions((new SubmissionManager()).queryAll()));

        // QA Controller
        ViewQuestionAndAnswerController viewQuestionAndAnswerController = controller.getViewQuestionAndAnswerController();
        viewQuestionAndAnswerController.setQuestionAndAnswer((new QAManager()).queryAll());
        QAManager.register(() -> viewQuestionAndAnswerController.setQuestionAndAnswer((new QAManager()).queryAll()));

        // ClarificationController
        ViewClarificationController viewClarificationController = controller.getViewClarificationController();
        viewClarificationController.setClarification((new ClarificationManager()).queryAll());
        ClarificationManager.register(() -> viewClarificationController.setClarification((new ClarificationManager()).queryAll()));

        primaryStage.setTitle("Controller");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        core.start();
    }
    static public void main(String [] argv) {
        launch(argv);
    }
}
