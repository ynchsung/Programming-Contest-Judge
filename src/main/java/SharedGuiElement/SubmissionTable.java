package SharedGuiElement;

import Shared.SubmissionInfo;
import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
public class SubmissionTable extends HBox implements Initializable {
    @FXML private TableView<SubmissionItem> submissionTable;
    @FXML private TableColumn submissionId;
    @FXML private TableColumn problemId;
    @FXML private TableColumn teamId;
    @FXML private TableColumn sourceCode;
    @FXML private TableColumn rejudge;
    @FXML private TableColumn submissionTimeStamp;
    @FXML private TableColumn result;
    @FXML private TableColumn resultTimeStamp;
    private boolean submissionIdDisable;
    private boolean problemIdDisable;
    private boolean teamIdDisable;
    private boolean sourceCodeDisable;
    private boolean rejudgeDisable;
    private boolean submissionTimeStampDisable;
    private boolean resultDisable;
    private boolean resultTimeStampDisable;
    private SubmissionTable self;
    private ObservableList submissions = FXCollections.observableArrayList();
    private Callback<String, Void> openCodeCallBack = param -> null;
    private Callback<String, Void> autoRejudgeCallBack = param -> null;
    private Callback<String, Void> manualJudgeCallBack = param -> null;

    public SubmissionTable(@NamedArg("submissionIdDisable") boolean submissionIdDisable,
                           @NamedArg("problemIdDisable") boolean problemIdDisable,
                           @NamedArg("teamIdDisable") boolean teamIdDisable,
                           @NamedArg("sourceCodeDisable") boolean sourceCodeDisable,
                           @NamedArg("rejudgeDisable") boolean rejudgeDisable,
                           @NamedArg("submissionTimeStampDisable") boolean submissionTimeStampDisable,
                           @NamedArg("resultDisable") boolean resultDisable,
                           @NamedArg("resultTimeStampDisable") boolean resultTimeStampDisable) {
        this.submissionIdDisable = submissionIdDisable;
        this.problemIdDisable = problemIdDisable;
        this.teamIdDisable = teamIdDisable;
        this.sourceCodeDisable = sourceCodeDisable;
        this.rejudgeDisable = rejudgeDisable;
        this.submissionTimeStampDisable = submissionTimeStampDisable;
        this.resultDisable = resultDisable;
        this.resultTimeStampDisable = resultTimeStampDisable;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SubmissionTable.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void openSourceCode (String submissionId) {
        try {
            openCodeCallBack.call(submissionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rejudgeCall (String submissionId) {
        JudgeConfirmationAlert alert = new JudgeConfirmationAlert();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == JudgeConfirmationAlert.autoJudge) {
            autoRejudgeCallBack.call(submissionId);
        } else if (result.get() == JudgeConfirmationAlert.manualJudge) {
            manualJudgeCallBack.call(submissionId);
        }
    }

    public void setOpenCodeCallBack (Callback callBack) {
        this.openCodeCallBack = callBack;
    }

    public void setAutojudgeCallBack(Callback callBack) {
        this.autoRejudgeCallBack = callBack;
    }

    public void setManualJudgeCallBack(Callback callBack) {
        this.manualJudgeCallBack = callBack;
    }

    @FXML public void setSubmissions (List<Map<String, String>> submissions) {
        this.submissions = FXCollections.observableArrayList();
        for (Map<String, String> m : submissions) {
            SubmissionItem item = new SubmissionItem(m.get("submission_id"), m.get("problem_id"), m.get("team_id"),
                    m.get("source_code"), m.get("submission_time_stamp"), m.get("result"), m.get("result_time_stamp"));
            this.submissions.add(item);
        }
        submissionTable.setItems(this.submissions);
    }

    @FXML public void setSubmissions (Map<Integer, SubmissionInfo> infos) {
        this.submissions = FXCollections.observableArrayList();
        for (Map.Entry<Integer, SubmissionInfo> entry : infos.entrySet()) {
            SubmissionItem item = new SubmissionItem(String.valueOf(entry.getValue().getID()),
                    entry.getValue().getProblemID(), "", entry.getValue().getSourceCode(),
                    String.valueOf(entry.getValue().getSubmitTimeStamp()), entry.getValue().getResult(),
                    String.valueOf(entry.getValue().getResultTimeStamp()));
            this.submissions.add(item);
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
        rejudge.setCellValueFactory(new PropertyValueFactory("sourceCode"));
        submissionTimeStamp.setCellValueFactory(new PropertyValueFactory("submissionTimeStamp"));
        result.setCellValueFactory(new PropertyValueFactory("result"));
        resultTimeStamp.setCellValueFactory(new PropertyValueFactory("resultTimeStamp"));
        // format the sourceCode column
        sourceCode.setCellFactory(param -> new SubmissionButtonCell(submissionTable, self));
        rejudge.setCellFactory(param -> new RejudgeButtonCell(submissionTable, self));
        // visibility
        submissionId.setVisible(!submissionIdDisable);
        problemId.setVisible(!problemIdDisable);
        teamId.setVisible(!teamIdDisable);
        sourceCode.setVisible(!sourceCodeDisable);
        rejudge.setVisible(!rejudgeDisable);
        submissionTimeStamp.setVisible(!submissionTimeStampDisable);
        result.setVisible(!resultDisable);
        resultTimeStamp.setVisible(!resultTimeStampDisable);
    }

    protected class SubmissionButtonCell extends TableCell<SubmissionItem, Boolean> {
        private Button button = new Button("Open Code");
        public SubmissionButtonCell(TableView tableView, SubmissionTable controller) {
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
            } else
                setGraphic(null);
        }
    }

    protected class RejudgeButtonCell extends TableCell<SubmissionItem, Boolean> {
        private Button button = new Button("Rejudge");
        public RejudgeButtonCell(TableView tableView, SubmissionTable controller) {
            button.setOnAction(event -> {
                int selected = getTableRow().getIndex();
                SubmissionItem selectedSubmissionItem = (SubmissionItem) tableView.getItems().get(selected);
                String selectedSubmissionId = selectedSubmissionItem.getSubmissionId();
                controller.rejudgeCall(selectedSubmissionId);
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
            } else
                setGraphic(null);
        }
    }
}
