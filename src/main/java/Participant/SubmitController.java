package Participant;

import CustomNode.MyChoiceBox;
import CustomNode.MyConfirmationButton;
import CustomNode.MyFileChooser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by aalexx on 1/3/16.
 *
 * need to set callback for confirm action.
 *
 * Map<String, String>
 *     "problem_id", [id]
 *     "source_code", [code]
 */
public class SubmitController implements Initializable {
    @FXML private RadioButton chooseFileRadioButton;
    @FXML private RadioButton pastCodeRadioButton;
    @FXML private ToggleGroup uploadToggleGroup;
    @FXML private MyChoiceBox problemIdChoiceBox;
    @FXML protected MyFileChooser codeFileChooser;
    @FXML protected TextArea codeTextArea;
    @FXML private MyConfirmationButton confirmButton;
    private SubmitStategy strategy = new SubmitStategy(this);
    private Callback<Map<String, String>, Void> callback = param -> null;
    private SubmitController self;


    public void setOnConfirmAction (Callback<Map<String, String>, Void> callback) {
        this.callback = callback;
    }

    public void setProblemId (List<String> choice) {
        problemIdChoiceBox.setChoice(choice);
    }

    public File getChosenFile () {
        return codeFileChooser.getChosenFile();
    }

    public String getTextArea () {
        return codeTextArea.getText();
    }

    public void setConfirmButtonDisable (boolean value) {
        confirmButton.setDisable(value);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        self = this;
        setConfirmButtonDisable(true);
        codeFileChooser.addListener(((observable1, oldValue1, newValue1) -> {
            strategy.onChange();
        }));
        codeTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            strategy.onChange();
        });
        confirmButton.setOkAction(event -> {
            Map<String, String> re = new HashMap<>();
            re.put("problem_id", (String)problemIdChoiceBox.getChoice());
            re.put("source_code", strategy.getCode());
            callback.call(re);
        });
        uploadToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton selected = (RadioButton) newValue.getToggleGroup().getSelectedToggle();
            if (selected == chooseFileRadioButton) {
                strategy = new SubmitChooseFileStrategy(self);
                strategy.onChange();
            } else if (selected == pastCodeRadioButton) {
                strategy = new SubmitPasteCodeStrategy(self);
                strategy.onChange();
            }
        });
    }
}
