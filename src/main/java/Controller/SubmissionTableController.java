package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by aalexx on 12/27/15.
 *
 * SubmissionTable controller.
 *
 * One can set submissions by passing a list of map to it.
 *
 * Note that for the value of "source_code", only the null-or-not matters.
 */
public class SubmissionTableController implements Initializable {
    @FXML private TableView<SubmissionItem> submissionTable;
    @FXML private TableColumn submissionId;
    @FXML private TableColumn problemId;
    @FXML private TableColumn teamId;
    @FXML private TableColumn sourceCode;
    @FXML private TableColumn submissionTimeStamp;
    @FXML private TableColumn result;
    @FXML private TableColumn resultTimeStamp;
    private SubmissionTableController self;
    private ObservableList submissions = FXCollections.observableArrayList();
    private Callback<String, Void> openCodeCallBack = param -> null;

    public void openSourceCode (String submissionId) {
        try {
            openCodeCallBack.call(submissionId);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void setOpenCodeCallBack (Callback callBack) {
        this.openCodeCallBack = callBack;
    }

    public void setSubmissions (List<Map<String, String>> submissions) {
        this.submissions = FXCollections.observableArrayList();
        for (Map<String, String> m : submissions) {
            SubmissionItem item = new SubmissionItem(m.get("submission_id"), m.get("problem_id"), m.get("team_id"),
                    m.get("source_code"), m.get("submission_time_stamp"), m.get("result"), m.get("result_time_stamp"));
            this.submissions.add(item);
            System.out.println(item.getSubmissionId() + " " + item.getProblemId() + " " + item.getTeamId() + " " +
                    item.getSourceCode() + " " + item.getSubmissionTimeStamp() + " " + item.getResult() + " " + item.getResultTimeStamp());
        }
        submissionTable.setItems(this.submissions);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.self = this;
        // init table
        submissionTable.setTableMenuButtonVisible(false);
        // set property value factory
        submissionId.setCellValueFactory(new PropertyValueFactory("submissionId"));
        problemId.setCellValueFactory(new PropertyValueFactory("problemId"));
        teamId.setCellValueFactory(new PropertyValueFactory("teamId"));
        sourceCode.setCellValueFactory(new PropertyValueFactory("sourceCode"));
        submissionTimeStamp.setCellValueFactory(new PropertyValueFactory("submissionTimeStamp"));
        result.setCellValueFactory(new PropertyValueFactory("result"));
        resultTimeStamp.setCellValueFactory(new PropertyValueFactory("resultTimeStamp"));
        // format the sourceCode column
        sourceCode.setCellFactory(param -> new SubmissionButtonCell(submissionTable, self));
    }

    protected class SubmissionButtonCell extends TableCell<SubmissionItem, Boolean> {
        private Button button = new Button("Open Code");
        public SubmissionButtonCell(TableView tableView, SubmissionTableController controller) {
            button.setOnAction(event -> {
                int selected = getTableRow().getIndex();
                SubmissionItem selectedSubmissionItem = (SubmissionItem) tableView.getItems().get(selected);
                String selectedSubmissionId = selectedSubmissionItem.getSubmissionId();
                controller.openSourceCode(selectedSubmissionId);
            });
        }

        @Override
        protected void updateItem(Boolean value, boolean empty) {
            super.updateItem(value, empty);
            if (!empty) {
                if (value)
                    button.setDisable(false);
                else
                    button.setDisable(true);
                setGraphic(button);
            }
        }
    }
}
