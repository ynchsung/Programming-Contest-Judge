package Participant;

import SharedGuiElement.SubmissionTable;
import javafx.fxml.FXML;
import javafx.util.Callback;

import java.util.List;
import java.util.Map;

/**
 * Created by aalexx on 1/3/16.
 */
public class ViewSubmissionController {
    @FXML private SubmissionTable submissionTable;

    public void setOpenCodeCallBack (Callback callBack) {
        submissionTable.setOpenCodeCallBack(callBack);
    }

    public void setSubmissions (List<Map<String, String>> submissions) {
        submissionTable.setSubmissions(submissions);
    }
}
