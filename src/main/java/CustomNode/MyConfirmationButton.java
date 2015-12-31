package CustomNode;

import javafx.beans.NamedArg;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by aalexx on 12/28/15.
 *
 * This is a button that will popup a confirmation alert when been clicked.
 *
 * One can set the behavior of 'ok' and 'cancel' button by calling respect setter.
 * To make it more simple, one can still pass an eventhandler into the two button as if using noremal button.
 */
public class MyConfirmationButton extends HBox implements Initializable {
    @FXML private Button button;
    private String text;// = "confirm";
    private String title ;//= "Default Title";
    private String headerText;// = "Default header text.";
    private String contentText ;//= "Default content.";
    private boolean defaultButton = false;
    private EventHandler okAction = event -> {};
    private EventHandler cancelAction = event -> {};

    public MyConfirmationButton(@NamedArg("text") String text,
                                @NamedArg("title") String title,
                                @NamedArg("headerText") String headerText,
                                @NamedArg("contentText") String contentText,
                                @NamedArg("defaultButton") boolean isDefaultButton) {
        this.text = text;
        this.title = title;
        this.headerText = headerText;
        this.contentText = contentText;
        this.defaultButton = isDefaultButton;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyConfirmationButton.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setText (String text) {
        this.text = text;
        button.setText(this.text);
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public void setHeaderText (String headerText) {
        this.headerText = headerText;
    }

    public void setContentText (String contentText) {
        this.contentText = contentText;
    }

    public void setOkAction (EventHandler okAction) {
        this.okAction = okAction;
    }

    public void setCancelAction(EventHandler cancelAction) {
        this.cancelAction = cancelAction;
    }

    @FXML
    protected void onButtonClicked () {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(headerText);
        confirmationAlert.setContentText(contentText);
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        try {
            if (result.get() == ButtonType.OK)
                okAction.handle(new ActionEvent());
            else
                cancelAction.handle(new ActionEvent());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setText(text);
        setTitle(title);
        setHeaderText(headerText);
        setContentText(contentText);
        button.setDefaultButton(defaultButton);
    }
}
