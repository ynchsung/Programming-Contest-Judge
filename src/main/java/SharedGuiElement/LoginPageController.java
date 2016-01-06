package SharedGuiElement;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * Created by aalexx on 1/2/16.
 */
public class LoginPageController {
    @FXML private TextField account;
    @FXML private PasswordField password;
    @FXML private TextField ip;
    @FXML private TextField port;
    @FXML private Button confirm;
    @FXML private Text message;

    public void setAccount(String account) {
        this.account.setText(account);
    }

    public void setIp(String ip) {
        this.ip.setText(ip);
    }

    public void setPassword(String password) {
        this.password.setText(password);
    }

    public void setPort(String port) {
        this.port.setText(port);
    }

    public String getAccount() {
        return account.getText();
    }

    public String getIp() {
        return ip.getText();
    }

    public String getPassword() {
        return password.getText();
    }

    public String getPort() {
        return port.getText();
    }

    public void setButtonText (String text) {
        confirm.setText(text);
    }

    public void setMessage (String message) {
        this.message.setText(message);
    }

    public void setConfirmOnAction (EventHandler<ActionEvent> handler) {
        confirm.setOnAction(handler);
    }
}
