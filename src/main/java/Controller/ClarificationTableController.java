package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by aalexx on 12/30/15.
 */
public class ClarificationTableController implements Initializable {
    @FXML private TableView clarificationTable;
    @FXML private TableColumn clarificationId;
    @FXML private TableColumn problemId;
    @FXML private TableColumn content;
    @FXML private TableColumn timeStamp;
    private ObservableList clarification = FXCollections.observableArrayList();

    public void setClarification (List<Map<String, String>> clarification) {
        this.clarification = FXCollections.observableArrayList();
        for (Map<String, String> i : clarification) {
            ClarificationItem item = new ClarificationItem(i.get("clarification_id"),
                    i.get("problem_id"), i.get("content"), i.get("time_stamp"));
            this.clarification.add(item);
        }
        clarificationTable.setItems(this.clarification);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // init table
        clarificationTable.setTableMenuButtonVisible(false);
        //init column
        clarificationId.setCellValueFactory(new PropertyValueFactory("clarificationId"));
        problemId.setCellValueFactory(new PropertyValueFactory("problemId"));
        content.setCellValueFactory(new PropertyValueFactory("content"));
        timeStamp.setCellValueFactory(new PropertyValueFactory("timeStamp"));
    }
}
