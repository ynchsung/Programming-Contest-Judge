package Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by aalexx on 12/27/15.
 *
 * Popup a new screen which shows the source code.
 */
public class OpenCode {
    private String code;
    private OpenCodeController controller;

    public void setCode (String code) {
        this.code = code;
    }

    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OpenCode.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Open Code");
            stage.setScene(new Scene(root, 400, 450));
            controller = loader.getController();
            controller.setCode(code);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
