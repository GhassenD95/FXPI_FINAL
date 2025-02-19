package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class MainViewController {

    @FXML
    private void openEquipment() {
        openWindow("/views/EquipementView.fxml", "Gestion des Équipements");
    }

    @FXML
    private void openInstallationSportive() {
        openWindow("/views/InstallationSportiveView.fxml", "Gestion des Installations Sportives");
    }

    private void openWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
