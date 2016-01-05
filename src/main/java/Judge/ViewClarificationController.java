package Judge;

import CustomNode.MyChoiceBox;
import CustomNode.MyConfirmationButton;
import Shared.ClarificationInfo;
import SharedGuiElement.ClarificationTable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.*;

/**
 * Created by aalexx on 1/2/16.
 */
public class ViewClarificationController implements Initializable {
    @FXML private ClarificationTable clarificationTable;
    @FXML private TextArea newClarificationTextArea;
    @FXML private MyChoiceBox clarificationProblemChoice;
    @FXML private MyConfirmationButton confirmNewClarificationButton;

    public void setClarification (List<Map<String, String>> clarification) {
        clarificationTable.setClarification(clarification);
    }

    public void setClarification(Map<Integer, ClarificationInfo> integerClarificationInfoMap) {
        List<Map<String, String>> res = new ArrayList<>();
        for (ClarificationInfo info: integerClarificationInfoMap.values()) {
            Map<String, String> entry = new HashMap<>();
            entry.put("clarification_id", Integer.toString(info.getID()));
            entry.put("problem_id", info.getProblemID());
            entry.put("time_stamp", Integer.toString(info.getTimeStamp()));
            entry.put("content", info.getContent());
            res.add(entry);
        }
        setClarification(res);
    }

    public String getNewClarificationText () {
        return newClarificationTextArea.getText();
    }

    public void setConfirmNewClarificationAction (EventHandler<ActionEvent> handler) {
        confirmNewClarificationButton.setOkAction(handler);
    }

    public Object getProblemChoice () {
        return clarificationProblemChoice.getChoice();
    }

    public int getProblemChoiceNumber () {
        return clarificationProblemChoice.getChoiceNumber();
    }

    public void setProblemChoice (List<String> choice) {
        clarificationProblemChoice.setChoice(choice);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newClarificationTextArea.clear();
        confirmNewClarificationButton.setDisable(true);
        newClarificationTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals(""))
                    confirmNewClarificationButton.setDisable(true);
                else
                    confirmNewClarificationButton.setDisable(false);
            }
        });
    }
}
