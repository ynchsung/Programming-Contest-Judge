package Participant;

import Controller.*;
import SharedGuiElement.RemainingTime;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by aalexx on 1/3/16.
 */
public class MainController implements Initializable {
    @FXML private RemainingTime remainingTime;
    @FXML private SubmitController submitController;
    @FXML private ViewSubmissionController viewSubmissionController;
    @FXML private ViewClarificationController viewClarificationController;
    @FXML private ViewQuestionAndAnswerController viewQuestionAndAnswerController;

    public RemainingTime getRemainingTimeController() {
        return remainingTime;
    }

    public SubmitController getSubmitController() {
        return submitController;
    }

    public ViewClarificationController getViewClarificationController() {
        return viewClarificationController;
    }

    public ViewQuestionAndAnswerController getViewQuestionAndAnswerController() {
        return viewQuestionAndAnswerController;
    }

    public ViewSubmissionController getViewSubmissionController() {
        return viewSubmissionController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
