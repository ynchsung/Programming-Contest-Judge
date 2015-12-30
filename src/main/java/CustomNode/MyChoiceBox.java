package CustomNode;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

/**
 * Created by aalexx on 12/27/15.
 *
 * A slightly "simpler" choice box.
 * In general, we want to automatically select the first item after setting selections, so there it is.
 *
 * Sometimes we want to know the sequence number of choice user selected instead of the choice itself.
 * So I added a getSelectedNumber() method.
 */
public class MyChoiceBox extends AnchorPane {
    @FXML private ChoiceBox choiceBox;

    public MyChoiceBox() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyChoiceBox.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setChoice(List<String> choice) {
        choiceBox.setItems(FXCollections.observableArrayList(choice));
        // select first on default
        choiceBox.getSelectionModel().selectFirst();
    }

    public void selectFirst () {
        choiceBox.getSelectionModel().selectFirst();
    }

    public void selectLast () {
        choiceBox.getSelectionModel().selectLast();
    }

    public void select(int index) {
        choiceBox.getSelectionModel().select(index);
    }

    public int getChoiceNumber () {
        return choiceBox.getItems().indexOf(getChoice());
    }

    public Object getChoice () {
        return choiceBox.getValue();
    }
}
