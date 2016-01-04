package SharedGuiElement;

import Shared.AnswerInfo;
import Shared.QuestionInfo;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by aalexx on 12/31/15.
 */
public class QuestionAndAnswerTable extends HBox implements Initializable {
    @FXML private TableView questionAndAnswerTable;
    @FXML private TableColumn type;
    @FXML private TableColumn id;
    @FXML private TableColumn answerButton;
    @FXML private TableColumn problemId;
    @FXML private TableColumn content;
    @FXML private TableColumn timeStamp;
    private boolean typeDisable;
    private boolean idDisable;
    private boolean answerButtonDisable;
    private boolean problemIdDisable;
    private boolean contentDisable;
    private boolean timeStampDisable;
    private QuestionAndAnswerTable self;
    private ObservableList questionAndAnswer = FXCollections.observableArrayList();
    private Callback<Map<String, String>, Void> answerQuestionCallBack = event -> null;

    public QuestionAndAnswerTable(@NamedArg("typeDisable") boolean typeDisable,
                                  @NamedArg("idDisable") boolean idDisable,
                                  @NamedArg("answerButtonDisable") boolean answerButtonDisable,
                                  @NamedArg("problemIdDisable") boolean problemIdDisable,
                                  @NamedArg("contentDisable") boolean contentDisable,
                                  @NamedArg("timeStampDisable") boolean timeStampDisable) {
        this.typeDisable = typeDisable;
        this.idDisable = idDisable;
        this.answerButtonDisable = answerButtonDisable;
        this.problemIdDisable = problemIdDisable;
        this.contentDisable = contentDisable;
        this.timeStampDisable = timeStampDisable;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("QuestionAndAnswerTable.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void answerCall (String id, String question) {
        PopupAnswerDialog dialog = new PopupAnswerDialog(question);
        String answer = dialog.showAndWait();
        if (answer != null) {
            Map<String, String> remap = new HashMap<>();
            remap.put("questionId", id);
            remap.put("content", answer);
            answerQuestionCallBack.call(remap);
        }
    }

    public void setAnswerQuestionCallBack (Callback callBack) {
        this.answerQuestionCallBack = callBack;
    }

    public void setQuestionAndAnswer(List<Map<String, String>> questionAndAnswer) {
        this.questionAndAnswer = FXCollections.observableArrayList();
        for (Map<String, String> i : questionAndAnswer) {
            QuestionAndAnswerItem item = new QuestionAndAnswerItem(i.get("id"), i.get("type"),
                    i.get("problem_id"), i.get("content"), i.get("time_stamp"));
            this.questionAndAnswer.add(item);
        }
        questionAndAnswerTable.setItems(this.questionAndAnswer);
    }

    public void setQuestionAndAnswer(Map<Integer, QuestionInfo> infos) {
        this.questionAndAnswer = FXCollections.observableArrayList();
        for (Map.Entry<Integer, QuestionInfo> entry : infos.entrySet()) {
            QuestionAndAnswerItem questionItem = new QuestionAndAnswerItem(String.valueOf(entry.getValue().getID()),
                    "question", entry.getValue().getProblemID(), entry.getValue().getContent(),
                    String.valueOf(entry.getValue().getTimeStamp()));
            this.questionAndAnswer.add(questionItem);
            for (AnswerInfo answerInfo : entry.getValue().getAnswers()) {
                QuestionAndAnswerItem answerItem = new QuestionAndAnswerItem(String.valueOf(entry.getValue().getID()),
                        "answer", entry.getValue().getProblemID(), answerInfo.getContent(),
                        String.valueOf(answerInfo.getTimeStamp()));
                this.questionAndAnswer.add(answerItem);
            }
        }
        questionAndAnswerTable.setItems(this.questionAndAnswer);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.self = this;
        // init table
        questionAndAnswerTable.setTableMenuButtonVisible(false);
        //init column
        type.setCellValueFactory(new PropertyValueFactory("type"));
        answerButton.setCellValueFactory(new PropertyValueFactory("type"));
        problemId.setCellValueFactory(new PropertyValueFactory("problemId"));
        content.setCellValueFactory(new PropertyValueFactory("content"));
        timeStamp.setCellValueFactory(new PropertyValueFactory("timeStamp"));
        // format type
        type.setCellFactory(param -> new typeCell());
        answerButton.setCellFactory(param -> new answerCell(questionAndAnswerTable, self));
        // visibility
        type.setVisible(!typeDisable);
        id.setVisible(!idDisable);
        answerButton.setVisible(!answerButtonDisable);
        problemId.setVisible(!problemIdDisable);
        content.setVisible(!contentDisable);
        timeStamp.setVisible(!timeStampDisable);
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

    protected class answerCell extends TableCell<QuestionAndAnswerItem, String> {
        private Button button = new Button("Answer");
        public answerCell (TableView table, QuestionAndAnswerTable controller) {
            button.setOnAction(event ->{
                int selected = getTableRow().getIndex();
                QuestionAndAnswerItem item = (QuestionAndAnswerItem) table.getItems().get(selected);
                controller.answerCall(item.getId(), item.getContent());
            });
        }
        @Override
        protected  void updateItem (String type, boolean empty) {
            if (!empty && type.equals("question")){
                setGraphic(button);
            }
        }
    }
}
