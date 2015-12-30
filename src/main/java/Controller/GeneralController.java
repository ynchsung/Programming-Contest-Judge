package Controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 * Created by aalexx on 12/27/15.
 */
public class GeneralController {
    @FXML private Text ip;
    @FXML private Text port;
    @FXML private Text scoreBoardPort;

    public void setIp(String ip) {
        this.ip.setText(ip);
    }

    public void setPort(String port) {
        this.port.setText(port);
    }

    public void setScoreBoardPort(String scoreBoardPort) {
        this.scoreBoardPort.setText(scoreBoardPort);
    }
}
