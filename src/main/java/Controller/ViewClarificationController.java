package Controller;

import javafx.fxml.FXML;

import java.util.List;
import java.util.Map;

/**
 * Created by aalexx on 12/30/15.
 */
public class ViewClarificationController {
    @FXML private ClarificationTableController clarificationTableController;

    public void setClarification (List<Map<String, String>> clarification) {
        clarificationTableController.setClarification(clarification);
    }
}
