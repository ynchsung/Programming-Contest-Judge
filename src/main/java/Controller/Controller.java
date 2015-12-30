package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class Controller extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Controller.fxml"));
        Parent root = loader.load();
        // use loader.getController() to get controller.
        primaryStage.setTitle("Controller");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
    static public void main(String [] argv) {
        launch(argv);
        Core.run();
    }
}
