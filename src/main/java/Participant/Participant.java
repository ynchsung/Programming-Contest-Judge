package Participant;

import Participant.EventHandler.LoginResultHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by aalexx on 1/3/16.
 */
public class Participant extends Application {
    private Stage stage;
    ControllerServer server = null;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ParticipantLoginPage.fxml"));
        Parent root = loader.load();
        ParticipantLoginPageController controller = loader.getController();
        LoginResultHandler.LoginResultListener loginResultListener = success -> Platform.runLater(() -> {
            if (success) {
                loginSuccess();
            } else {
                if (server != null) {
                    server.logout();
                }
                System.err.println("login failed");
                //TODO Login fail msg
            }
        });
        controller.setLoginButtonOnAction(event -> {
            String ip = controller.getIp();
            int port = Integer.valueOf(controller.getPort());
            String username = controller.getAccount();
            String password = controller.getPassword();
            Socket socket = new Socket();
            try {
                socket.connect(new InetSocketAddress(ip, port));
                Connection connection = new Connection(socket);
                server = new ControllerServer(connection, loginResultListener);
                connection.setControllerServer(server);
                connection.start();
                server.login(username, password);
            } catch (IOException e) {
                loginResultListener.callback(false);
            }
        });
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
