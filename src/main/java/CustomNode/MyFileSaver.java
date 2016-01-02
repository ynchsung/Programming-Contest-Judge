package CustomNode;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by aalexx on 1/3/16.
 *
 * Need to setSaveFile (byte[]) on init.
 * 
 */
public class MyFileSaver extends HBox implements Initializable {
    @FXML private Button saveButton;
    private String text;
    private byte[] target;

    public MyFileSaver (@NamedArg("text") String text) {
        this.text = text;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyFileSaver.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setSaveFile (byte[] target) {
        this.target = target;
    }

    public void setSaveButtonText (String text) {
        saveButton.setText(text);
    }

    @FXML protected void onSaveClicked () {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As");
        File file = fileChooser.showSaveDialog(saveButton.getScene().getWindow());
        if (file != null) {
            try {
                FileOutputStream out = new FileOutputStream(file) ;
                out.write(target);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setSaveButtonText(text);
    }
}
