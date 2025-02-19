package mains.fxpi_final;
import controllers.PerformanceController;
import controllers.TournoiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Create an instance of FXMLLoader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Tournoi1.fxml"));



        // Load the FXML file into a Pane (or other layout)
        Pane root = loader.load();

        // Create a Scene and set it on the stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // Show the Stage (window)
        primaryStage.show();
    }
}