package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.module1.Equipe;
import models.module4.PerformanceEquipe;
import models.module4.Tournois;
import services.module4.Service1PerformanceEquipe;

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

    private final Service1PerformanceEquipe servicePerformanceEquipe = new Service1PerformanceEquipe();

    @FXML
    public void initialize() {
        initializeEquipes();
        initializeTournois();
    }

    private void initializeEquipes() {
        try {
            List<Equipe> equipeList = servicePerformanceEquipe.getAllEquipes();
            if (equipeList != null && !equipeList.isEmpty()) {
                List<Integer> equipeIds = equipeList.stream()
                        .map(Equipe::getId)
                        .collect(Collectors.toList());
                equipeComboBox.getItems().setAll(equipeIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.toList()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            // Vérification si les champs sont vides ou non sélectionnés
            if (equipeComboBox.getValue() == null || equipeComboBox.getValue().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Veuillez sélectionner une équipe.");
                return;
            }
            if (tournoisComboBox.getValue() == null || tournoisComboBox.getValue().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Veuillez sélectionner un tournoi.");
                return;
            }

            // Vérification des champs numériques
            String victoiresText = victoiresField.getText().trim();
            String pertesText = pertesField.getText().trim();
            String rangText = rangField.getText().trim();

            if (victoiresText.isEmpty() || pertesText.isEmpty() || rangText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Tous les champs doivent être remplis.");
                return;
            }

            int victoires, pertes, rang;

            try {
                victoires = Integer.parseInt(victoiresText);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Le nombre de victoires doit être un entier valide.");
                return;
            }

            try {
                pertes = Integer.parseInt(pertesText);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Le nombre de pertes doit être un entier valide.");
                return;
            }

            try {
                rang = Integer.parseInt(rangText);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Le rang doit être un entier valide.");
                return;
            }

            // Récupérer l'équipe sélectionnée
            Integer equipeId = Integer.valueOf(equipeComboBox.getValue());
            Equipe equipe = servicePerformanceEquipe.getAllEquipes().stream()
                    .filter(e -> e.getId() == equipeId)
                    .findFirst()
                    .orElse(null);

            // Récupérer le tournoi sélectionné
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

            // Créer l'objet PerformanceEquipe
            PerformanceEquipe performanceEquipe = new PerformanceEquipe(equipe, tournois, victoires, pertes, rang);

            // Ajouter la performance
            try {
                servicePerformanceEquipe.add(performanceEquipe);
                showAlert(Alert.AlertType.INFORMATION, "Ajout réussi", "La performance de l'équipe a été ajoutée avec succès!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Erreur lors de l'ajout de la performance : " + e.getMessage());
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "General Error", "Erreur générale : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }}