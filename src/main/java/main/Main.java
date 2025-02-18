package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Performance.fxml"));
        Pane root = loader.load();  // Load the FXML into a Pane (or other layout)

        // Create a Scene and set it on the stage

        Scene scene = new Scene(root);
        primaryStage.setTitle("Ajouter un Tournoi");
        primaryStage.setScene(scene);

        // Show the Stage (window)
        primaryStage.show();
    }
}