package Controller;

import SharedGuiElement.OpenCode;
import SharedGuiElement.OpenCodeBuilder;
import SharedGuiElement.QuestionAndAnswerTableController;
import SharedGuiElement.RemainingTimeController;
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

    public ViewQuestionAndAnswerController getViewQuestionAndAnswerController() {
        return viewQuestionAndAnswerController;
    }

    public ViewClarificationController getViewClarificationController() {
        return viewClarificationController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
