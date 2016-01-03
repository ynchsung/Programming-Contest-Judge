package Participant;

import SharedGuiElement.LoginPageController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by aalexx on 1/3/16.
 */
public class ParticipantLoginPageController implements Initializable {
    @FXML private LoginPageController loginPageController;

    public void setLoginButtonOnAction (EventHandler<ActionEvent> handler) {
        loginPageController.setConfirmOnAction(handler);
    }

    public void setMessage (String message) {
        loginPageController.setMessage(message);
    }

    public String getAccount () {
        return loginPageController.getAccount();
    }

    public String getPassword () {
        return loginPageController.getPassword();
    }

    public String getIp () {
        return loginPageController.getIp();
    }

    public String getPort () {
        return loginPageController.getPort();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginPageController.setButtonText("Login");
    }
}
