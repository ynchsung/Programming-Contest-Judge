package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;

/**
 * Created by aalexx on 12/27/15.
 *
 * This is the main page of controller.
 *
 * Every controller of sub-tabs can be accessed here.
 */
public class MainController implements Initializable {
    @FXML private RemainingTimeController remainingTimeController;
    @FXML private GeneralController generalController;
    @FXML private TimeUpdateController timeUpdateController;
    @FXML private TestDataUpdateController testDataUpdateController;
    @FXML private ViewSubmissionController viewSubmissionController;
    @FXML private ViewClarificationController viewClarificationController;
    @FXML private ViewQuestionAndAnswerController viewQuestionAndAnswerController;

    // getter for each tab's controller
    public ViewSubmissionController getViewSubmissionController() {
        return viewSubmissionController;
    }

    public TimeUpdateController getTimeUpdateController() {
        return timeUpdateController;
    }

    public TestDataUpdateController getTestDataUpdateController() {
        return testDataUpdateController;
    }

    public RemainingTimeController getRemainingTimeController() {
        return remainingTimeController;
    }

    public GeneralController getGeneralController() {
        return generalController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // init remainingTime
        remainingTimeController.setRemainingTime("lalala");
        // init each tab
        // general example
        generalController.setIp("127.0.0.1");
        generalController.setPort("7122");
        generalController.setScoreBoardPort("2217");
        // timeUpdate example
        timeUpdateController.setStartTime(System.currentTimeMillis() / 1000L);
        timeUpdateController.setDuration(300);
        timeUpdateController.setOnConfirmAction(event -> {
            System.out.println("User input : " + timeUpdateController.getDuration());
        });
        // testDataUpdate example
        ArrayList<String> s = new ArrayList<>();
        s.add("PA");
        s.add("PB");
        s.add("JIZZ");
        testDataUpdateController.setProblemChoice(s);
        testDataUpdateController.setOnConfirmAction(event -> {
            int selected = testDataUpdateController.getProlemChoiceNumber();
            System.out.println("selected=" + selected);
            if (testDataUpdateController.getInputData() != null)
                System.out.println("input=" + testDataUpdateController.getInputData().getAbsolutePath());
            if (testDataUpdateController.getOutputData() != null)
                System.out.println("output=" + testDataUpdateController.getOutputData().getAbsolutePath());
            System.out.println("special judge select=" + testDataUpdateController.specialJudgeIsSelected());
            if (testDataUpdateController.getSpecialJudgeCode() != null)
                System.out.println("special=" + testDataUpdateController.getSpecialJudgeCode().getAbsolutePath());
        });
        // viewSubmission example
        viewSubmissionController.setRejudgeProblemChoice(s);
        viewSubmissionController.setRejudgeButtonOnAction(event -> {
            System.out.println("selected=" + viewSubmissionController.getRejudgeProblemChoiceNumber());
        });
        viewSubmissionController.setOpenCodeCallBack(new Callback<String, Void>() {
            @Override
            public Void call(String submissionId) {
                System.out.println("select submissionId=" + submissionId);
                OpenCode o = OpenCodeBuilder.create()
                        .setCode("int main () {\n\treturn 0;\n}")
                        .build();
                o.show();
                return null;
            }
        });
        Map<String, String> ma = new HashMap<>();
        ma.put("submission_id", "7122");
        ma.put("problem_id", "222");
        ma.put("team_id", "33");
        ma.put("source_code", "1");
        ma.put("submission_time_stamp", "9900");
        ma.put("result", "TLE");
        ma.put("result_time_stamp", "33333");
        Map<String, String> mb = new HashMap<>();
        mb.put("submission_id", "88888");
        mb.put("problem_id", "11");
        mb.put("team_id", "34");
        mb.put("source_code", null);
        mb.put("submission_time_stamp", "88");
        mb.put("result", "TLE");
        mb.put("result_time_stamp", "445");
        List<Map<String, String> > lll = new ArrayList<>();
        lll.add(ma);
        lll.add(mb);
        viewSubmissionController.setSubmissions(lll);
        // view Clarification
        List<Map<String, String>> cl = new ArrayList<>();
        Map<String, String> cma = new HashMap<>();
        cma.put("clarification_id", "1");
        cma.put("problem_id", "0");
        cma.put("content", "This is a test.");
        cma.put("time_stamp", "712222");
        cl.add(cma);
        if (viewClarificationController == null)
            System.out.println("WTF");
        else
            viewClarificationController.setClarification(cl);
        // question and answer table
        List<Map<String, String>> qal = new ArrayList<>();
        Map<String, String> qam = new HashMap<>();
        qam.put("type", "question");
        qam.put("problem_id", "1");
        qam.put("content", "Is iron equal to wisdom?");
        qam.put("time_stamp", "7122");
        qal.add(qam);
        Map<String, String> qamb = new HashMap<>();
        qamb.put("type", "answer");
        qamb.put("problem_id", "1");
        qamb.put("content", "Yes");
        qamb.put("time_stamp", "7123");
        qal.add(qamb);
        viewQuestionAndAnswerController.setQuestionAndAnswer(qal);
    }
}
