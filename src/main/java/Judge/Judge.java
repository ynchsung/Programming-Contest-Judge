package Judge;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by aalexx on 1/2/16.
 */
public class Judge extends Application {
    private Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("JudgeLoginPage.fxml"));
        Parent root = loader.load();
        // use loader.getController() to get login page's controller
        primaryStage.setTitle("Judge");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    /*
     * Change scene to judge's main page.
     */
    public void changeStage () {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Judge.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // use loader.getController to get main controller
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(root, 800, 600);
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(root);
        }
        stage.sizeToScene();
    }

    public static void main (String[] args) {
        launch(args);
    }

}
