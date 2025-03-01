package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.module1.Equipe;
import models.module4.PerformanceEquipe;
import models.module4.Tournois;
import services.module1.ServiceEquipe;
import services.module4.ServicePerformanceEquipe;
import services.module4.ServiceTournois;

import java.sql.SQLException;

public class Performance2Controller {

    @FXML
    private TextField idField;
    @FXML
    private TextField equipeField;
    @FXML
    private TextField tournoisField;
    @FXML
    private TextField victoiresField;
    @FXML
    private TextField pertesField;
    @FXML
    private TextField rangField;

    private PerformanceEquipe performance;

    public void setPerformance(PerformanceEquipe performance) {
        this.performance = performance;
        equipeField.setText(String.valueOf(performance.getEquipe().getNom()));
        tournoisField.setText(String.valueOf(performance.getTournois().getNom()));
        victoiresField.setText(String.valueOf(performance.getVictoires()));
        pertesField.setText(String.valueOf(performance.getPertes()));
        rangField.setText(String.valueOf(performance.getRang()));
    }

    @FXML
    private void handleValiderButtonAction() {
        try {
            if (equipeField.getText().isEmpty() || tournoisField.getText().isEmpty() ||
                    victoiresField.getText().isEmpty() || pertesField.getText().isEmpty() || rangField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Tous les champs doivent être remplis.");
                return;
            }

            int equipeId, tournoisId, victoires, pertes, rang;
            try {
                equipeId = Integer.parseInt(equipeField.getText());
                tournoisId = Integer.parseInt(tournoisField.getText());
                victoires = Integer.parseInt(victoiresField.getText());
                pertes = Integer.parseInt(pertesField.getText());
                rang = Integer.parseInt(rangField.getText());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Tous les champs numériques doivent être des entiers valides.");
                return;
            }

            // Retrieve the Equipe and Tournois objects based on the IDs
            ServiceEquipe serviceEquipe = new ServiceEquipe();
            ServiceTournois serviceTournois = new ServiceTournois();

            Equipe equipe = serviceEquipe.get(equipeId);
            if (equipe == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "L'équipe sélectionnée n'existe pas.");
                return;
            }

            Tournois tournois = serviceTournois.get(tournoisId);
            if (tournois == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Le tournoi sélectionné n'existe pas.");
                return;
            }

            performance.setEquipe(equipe);
            performance.setTournois(tournois);
            performance.setVictoires(victoires);
            performance.setPertes(pertes);
            performance.setRang(rang);

            ServicePerformanceEquipe servicePerformanceEquipe = new ServicePerformanceEquipe();
            servicePerformanceEquipe.update(performance);

            showAlert(Alert.AlertType.INFORMATION, "Success", "La performance a été mise à jour avec succès!");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Erreur lors de la mise à jour de la performance : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "General Error", "Erreur générale : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
    }
}