package Participant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ParticipantMain extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception{
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Controller.fxml"));
        Parent root = loader.load();
        // use loader.getController() to get controller.
        primaryStage.setTitle("Controller");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        */
    }

    static public void main(String [] argv) {
        // login get judge obj
        ControllerServer controllerServer = /* TODO: login and get a connection */new ControllerServer("controller", null);
        launch(argv);
        ParticipantCore.run(controllerServer);
    }
}
