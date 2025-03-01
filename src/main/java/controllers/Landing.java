package controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Landing {

    @FXML
    private Label newsLabel;

    @FXML
    private Button performanceListButton;

    @FXML
    private Button tournoiButton;



    @FXML
    private void handlePerformanceListButtonAction() throws IOException {
        navigateTo("/Performancelist.fxml", performanceListButton);
    }

    @FXML
    private void handleTournoiButtonAction() throws IOException {
        navigateTo("/Tournoi1.fxml", tournoiButton);
    }

    private void navigateTo(String fxmlFile, Button button) throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage.setScene(new Scene(root));
        stage.show();
    }

}