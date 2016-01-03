package Judge;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by aalexx on 1/3/16.
 *
 * Call setOnConfirm(Callback<Map<String, String>, Void>) to set onConfirm behavior.
 *
 * Call show() to show;
 *
 * meaning:
 * "submission_id", [id]
 * "result", ["WA", "AC", "CE", "RE", "TLE", "MLE"]
 */
public class ManualJudgePage {
    private ManualJudgePageController controller;
    private List<String> resultChoiceList = Arrays.asList("WA", "AC", "CE", "RE", "TLE", "MLE");
    private Stage stage;

    public ManualJudgePage (String submissionId, String sourceCode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManualJudgePage.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            controller.setSaveFile(sourceCode.getBytes());
            controller.setSubmissionId(submissionId);
            controller.setChoice(resultChoiceList);
            controller.registManualJudgePage(this);
            stage = new Stage();
            stage.setTitle("Manual Judge");
            stage.setScene(new Scene(root, 400, 450));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show () {
        stage.show();
    }

    public void setOnConfirm (Callback<Map<String, String>, Void> callback) {
        controller.setOnConfirm(callback);
    }

    public void closeStage () {
        stage.close();
    }
}
