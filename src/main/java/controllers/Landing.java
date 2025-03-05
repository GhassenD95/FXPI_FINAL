package controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EventListener;

public class Landing {
    @FXML
    public VBox sidebarContainer;
    @FXML
    private Label newsLabel;

    @FXML
    private Button performanceListButton;

    @FXML
    private Button tournoiButton;

    public void initialize() throws IOException {
        VBox sidebar = FXMLLoader.load(getClass().getResource("/navigation.fxml"));
        sidebarContainer.getChildren().setAll(sidebar);
    }



    private void navigateTo(String fxmlFile, Button button) throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage.setScene(new Scene(root));
        stage.show();
    }


}
