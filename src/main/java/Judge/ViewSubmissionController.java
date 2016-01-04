package Judge;

import Shared.SubmissionInfo;
import SharedGuiElement.SubmissionTable;
import javafx.fxml.FXML;
import javafx.util.Callback;

import java.util.List;
import java.util.Map;

/**
 * Created by aalexx on 1/2/16.
 */
public class ViewSubmissionController {
    @FXML private SubmissionTable submissionTable;

    public void setOpenCodeCallBack (Callback callBack) {
        submissionTable.setOpenCodeCallBack(callBack);
    }

    public void setAutojudgeCallBack(Callback callBack) {
        submissionTable.setAutojudgeCallBack(callBack);
    }

    public void setManualJudgeCallBack(Callback callBack) {
        submissionTable.setManualJudgeCallBack(callBack);
    }

    public void setSubmissions (List<Map<String, String>> submissions) {
        submissionTable.setSubmissions(submissions);
    }

    public void setSubmissions (Map<Integer, SubmissionInfo> infos) {
        submissionTable.setSubmissions(infos);
    }
}
