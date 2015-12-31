package SharedGuiElement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by aalexx on 12/31/15.
 */
public class QuestionAndAnswerTableController implements Initializable {
    @FXML private TableView questionAndAnswerTable;
    @FXML private TableColumn type;
    @FXML private TableColumn problemId;
    @FXML private TableColumn content;
    @FXML private TableColumn timeStamp;
    private ObservableList questionAndAnswer = FXCollections.observableArrayList();

    public void setQuestionAndAnswer(List<Map<String, String>> questionAndAnswer) {
        this.questionAndAnswer = FXCollections.observableArrayList();
        for (Map<String, String> i : questionAndAnswer) {
            QuestionAndAnswerItem item = new QuestionAndAnswerItem(i.get("type"),
                    i.get("problem_id"), i.get("content"), i.get("time_stamp"));
            this.questionAndAnswer.add(item);
        }
        questionAndAnswerTable.setItems(this.questionAndAnswer);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // init table
        questionAndAnswerTable.setTableMenuButtonVisible(false);
        //init column
        type.setCellValueFactory(new PropertyValueFactory("type"));
        problemId.setCellValueFactory(new PropertyValueFactory("problemId"));
        content.setCellValueFactory(new PropertyValueFactory("content"));
        timeStamp.setCellValueFactory(new PropertyValueFactory("timeStamp"));
        // format type
        type.setCellFactory(parm -> new typeCell());
    }

    protected class typeCell extends TableCell<QuestionAndAnswerItem, String> {
        private Label labelQ = new Label("Q");
        private Label labelA = new Label("A");

        @Override
        protected void updateItem (String type, boolean empty) {
            if (!empty) {
                if (type.equals("question"))
                    setGraphic(labelQ);
                else if (type.equals("answer"))
                    setGraphic(labelA);
            }
        }
    }
}
