package Participant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by aalexx on 1/3/16.
 */
public class Participant extends Application {
    private Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ParticipantLoginPage.fxml"));
        Parent root = loader.load();
        // use loader.getController() to get login page's controller
        /*
            do login stuff here
         */
        primaryStage.setTitle("Participant");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    /*
     * Change scene to judge's main page.
     */
    public void loginSuccess () {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Participant.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // use loader.getController to get main controller
        /*
            real judge core
         */
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
