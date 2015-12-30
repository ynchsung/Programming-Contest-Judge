package CustomNode;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by aalexx on 12/27/15.
 *
 * Containing a disabled textfield and a button in a HBox, this is a simple javafx node "implementing" file chooser.
 * The textfield will display the selected file name, and the button will open a javafx filechooser to browser a file.
 *
 * One can disable the button by calling setDisable(boolean value).
 *
 * One can set/add filter by calling setfilter(...)/addfilter(...).
 *
 * One can get the chosen file by calling getChosenFile().
 */
public class MyFileChooser extends HBox implements Initializable {
    @FXML private TextField filePath;
    @FXML private Button choose;
    private String text;
    private File chosenFile;
    private List<String> filterDescription;
    private List<List<String> > filterExtension;

    public MyFileChooser (@NamedArg("text") String text) {
        this.text = text;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyFileChooser.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setButtonText (String text) {
        this.choose.setText(text);
    }

    public void setFilePath (String filePath) {
        this.filePath.setText(filePath);
    }

    public void setFilter (List<String> description, List<List<String> > extentions) {
        filterDescription = description;
        filterExtension = extentions;
    }

    public void addFilter (String description, List<String> extentions) {
        filterDescription.add(description);
        filterExtension.add(extentions);
    }

    public File getChosenFile() {
        return chosenFile;
    }

    @FXML protected void onChooseClicked () {
        FileChooser fileChooser = new FileChooser();
        if (filterDescription != null && filterExtension != null) {
            for (int i = 0 ; i < filterDescription.size() ; ++i) {
                FileChooser.ExtensionFilter exfilter = new FileChooser.ExtensionFilter(filterDescription.get(i), filterExtension.get(i));
                fileChooser.getExtensionFilters().add(exfilter);
            }
        }
        chosenFile = fileChooser.showOpenDialog(choose.getScene().getWindow());
        if (chosenFile != null)
            setFilePath(chosenFile.getName());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setButtonText(this.text);
    }
}
