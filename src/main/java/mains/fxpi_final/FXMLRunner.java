package mains.fxpi_final;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMLRunner extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("/Adduser.fxml"));

        // Set the scene
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("FXML Runner");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}