package Judge;

import CustomNode.MyChoiceBox;
import CustomNode.MyConfirmationButton;
import CustomNode.MyFileSaver;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.util.Callback;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by aalexx on 1/3/16.
 */
public class ManualJudgePageController implements Initializable {
    @FXML private MyConfirmationButton confirmButton;
    @FXML private MyChoiceBox resultChoiceBox;
    @FXML private MyFileSaver submissionCodeSaver;
    private ManualJudgePage target;
    private String submissionId = "";
    private Callback<Map<String, String>, Void> handler = param -> null;

    public void setSaveFile(byte[] target) {
        submissionCodeSaver.setSaveFile(target);
    }

    public void registManualJudgePage (ManualJudgePage target) {
        this.target = target;
    }

    public void setChoice (List choice) {
        resultChoiceBox.setChoice(choice);
    }

    public void setSubmissionId (String submissionId) {
        this.submissionId = submissionId;
    }

    public void setOnConfirm (Callback<Map<String, String>, Void> handler) {
        this.handler = handler;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        confirmButton.setOkAction(event -> {
            Map<String, String> re = new HashMap<>();
            re.put("submission_id", submissionId);
            re.put("result", (String)resultChoiceBox.getChoice());
            this.handler.call(re);
            target.closeStage();
        });

    }
}
