package Participant;

import Shared.ClarificationInfo;
import SharedGuiElement.ClarificationTable;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.HashMap;
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
}
