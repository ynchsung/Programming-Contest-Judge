package Controller;

import CustomNode.MyChoiceBox;
import CustomNode.MyConfirmationButton;
import CustomNode.MyFileChooser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by aalexx on 12/27/15.
 *
 * The test data updating page.
 *
 * One need to pass the choice as a list of String to setProblemChoice().
 */
public class TestDataUpdateController implements Initializable {
    @FXML private CheckBox specialJudgeCheckbox;
    @FXML private MyConfirmationButton confirm;
    @FXML private MyFileChooser inputData;
    @FXML private MyFileChooser outputData;
    @FXML private MyFileChooser specialJudgeCode;
    @FXML private MyChoiceBox problemID;

    public void setProblemChoice(List<String> s) {
        problemID.setChoice(s);
    }

    public void setDefaultSelection (int index) {
        problemID.select(index);
    }

    public int getProlemChoiceNumber () {
        return problemID.getChoiceNumber();
    }

    public Object getProblemChoice () {
        return problemID.getChoice();
    }

    public File getInputData () {
        return inputData.getChosenFile();
    }

    public File getOutputData () {
        return outputData.getChosenFile();
    }

    public File getSpecialJudgeCode () {
        return specialJudgeCode.getChosenFile();
    }

    public boolean specialJudgeIsSelected () {
        return specialJudgeCheckbox.isSelected();
    }

    @FXML public void setOnConfirmAction(EventHandler<ActionEvent> action) {
        confirm.setOkAction(action);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        specialJudgeCode.setDisable(true);
        specialJudgeCheckbox.setSelected(false);
        specialJudgeCheckbox.setOnAction(event -> {
            if (specialJudgeCheckbox.isSelected())
                specialJudgeCode.setDisable(false);
            else
                specialJudgeCode.setDisable(true);
        });
    }
}
