package Participant;

import SharedGuiElement.ClarificationTable;
import javafx.fxml.FXML;

import java.util.List;
import java.util.Map;

/**
 * Created by aalexx on 1/3/16.
 */
public class ViewClarificationController {
    @FXML private ClarificationTable clarificationTable;

    public void setClarification (List<Map<String, String>> clarification) {
        clarificationTable.setClarification(clarification);
    }
}
