package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.module1.Equipe;
import models.module4.PerformanceEquipe;
import models.module4.Tournois;
import services.module4.ServicePerformanceEquipe;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class PerformanceController {
    @FXML
    private ComboBox<String> equipeComboBox;

    @FXML
    private TextField victoiresField;

    @FXML
    private TextField pertesField;

    @FXML
    private TextField rangField;

    @FXML
    private ComboBox<String> tournoisComboBox;

    @FXML
    private Button validerButton;
    @FXML


    private final ServicePerformanceEquipe servicePerformanceEquipe = new ServicePerformanceEquipe();

    @FXML
    public void initialize() {
        initializeEquipes();
        initializeTournois();
    }


    private void initializeEquipes() {
        try {
            List<Equipe> equipeList = servicePerformanceEquipe.getAllEquipes();
            if (equipeList != null && !equipeList.isEmpty()) {
                List<String> EquipeNames = equipeList.stream()
                        .map(Equipe::getNom)
                        .collect(Collectors.toList());
                equipeComboBox.getItems().setAll(EquipeNames);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeTournois() {
        List<Tournois> tournoisList = servicePerformanceEquipe.getAllTournois();
        if (tournoisList != null && !tournoisList.isEmpty()) {
            List<String> tournoisNames = tournoisList.stream()
                    .map(Tournois::getNom)
                    .collect(Collectors.toList());
            tournoisComboBox.getItems().setAll(tournoisNames);
        }
    }

    @FXML
    private void handleValiderButtonAction() {
        try {
            if (equipeComboBox.getValue() == null || equipeComboBox.getValue().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Veuillez sélectionner une équipe.");
                return;
            }
            if (tournoisComboBox.getValue() == null || tournoisComboBox.getValue().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Veuillez sélectionner un tournoi.");
                return;
            }

            String victoiresText = victoiresField.getText().trim();
            String pertesText = pertesField.getText().trim();
            String rangText = rangField.getText().trim();

            if (victoiresText.isEmpty() || pertesText.isEmpty() || rangText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Tous les champs doivent être remplis.");
                return;
            }

            int victoires, pertes, rang;

            // Validate that victoires is a non-negative integer
            try {
                victoires = Integer.parseInt(victoiresText);
                if (victoires < 0) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", "Le nombre de victoires doit être un nombre entier positif.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Le nombre de victoires doit être un entier valide.");
                return;
            }

            // Validate that pertes is a non-negative integer
            try {
                pertes = Integer.parseInt(pertesText);
                if (pertes < 0) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", "Le nombre de pertes doit être un nombre entier positif.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Le nombre de pertes doit être un entier valide.");
                return;
            }

            // Validate that rang is a non-negative integer
            try {
                rang = Integer.parseInt(rangText);
                if (rang < 0) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", "Le rang doit être un nombre entier positif.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Le rang doit être un entier valide.");
                return;
            }

            String equipeName = equipeComboBox.getValue();
            Equipe equipe = servicePerformanceEquipe.getAllEquipes().stream()
                    .filter(e -> e.getNom().equals(equipeName))
                    .findFirst()
                    .orElse(null);

            String tournoisName = tournoisComboBox.getValue();
            Tournois tournois = servicePerformanceEquipe.getAllTournois().stream()
                    .filter(t -> t.getNom().equals(tournoisName))
                    .findFirst()
                    .orElse(null);

            if (equipe == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "L'équipe sélectionnée n'existe pas.");
                return;
            }

            if (tournois == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Le tournoi sélectionné n'existe pas.");
                return;
            }

            PerformanceEquipe performanceEquipe = new PerformanceEquipe(equipe, tournois, victoires, pertes, rang);

            try {
                servicePerformanceEquipe.add(performanceEquipe);
                showAlert(Alert.AlertType.INFORMATION, "Ajout réussi", "La performance de l'équipe a été ajoutée avec succès!");
                navigateToPerformance1();

            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Erreur lors de l'ajout de la performance : " + e.getMessage());
            }
        } catch (Exception e) {
            // Handle any unexpected errors
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToPerformance1() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Performance1.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) validerButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Erreur lors de la navigation vers Performance1.fxml");
        }
    }

}





