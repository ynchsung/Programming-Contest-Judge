package SharedGuiElement;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by aalexx on 1/2/16.
 */
public class PopupAnswerDialog {
    private Stage stage;
    private Boolean clickYes = false;
    private PopupAnswerDialogController controller;

    public PopupAnswerDialog(String question, Stage owner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PopupAnswerDialog.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            controller.setQuestionTextArea(question);
            controller.registTarget(this);
            stage = new Stage();
            stage.setScene(new Scene(root, 400, 450));
            stage.initOwner(owner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PopupAnswerDialog(String question) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PopupAnswerDialog.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            controller.setQuestionTextArea(question);
            controller.registTarget(this);
            stage = new Stage();
            stage.setScene(new Scene(root, 400, 450));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String showAndWait () {
        stage.showAndWait();
        if (clickYes)
            return controller.getAnswer();
        return null;
    }

    public void closeStage () {
        if (stage != null)
            stage.close();
    }

    public void setClickYes () {
        clickYes = true;
    }
}
