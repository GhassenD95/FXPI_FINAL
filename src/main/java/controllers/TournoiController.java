package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.module4.Tournois;
import enums.Sport;
import tools.DbConnection;
import services.module4.Service1Tournois;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class TournoiController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField adresseField;

    @FXML
    private ComboBox<Sport> disciplineComboBox;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private Button validerButton;

    @FXML
    private void initialize() {
        // Initialize ComboBox with Sport values
        disciplineComboBox.getItems().setAll(Sport.values());
    }

    @FXML
    private void handleValiderButtonAction() {
        String nom = nomField.getText();
        String adresse = adresseField.getText();
        Sport sport = disciplineComboBox.getValue();
        Date dateDebut = java.sql.Date.valueOf(dateDebutPicker.getValue());
        Date dateFin = java.sql.Date.valueOf(dateFinPicker.getValue());

        // Input validation
        if (!validateInputs(nom, adresse, sport, dateDebut, dateFin)) {
            return; // Exit if validation fails
        }

        Tournois tournois = new Tournois(nom, sport, dateDebut, dateFin, adresse);
        Service1Tournois serviceTournois = new Service1Tournois();

        try {
            serviceTournois.add(tournois);
            showDialog("Ajout réussi", "Le tournoi a été ajouté avec succès!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showDialog("Erreur", "Une erreur est survenue lors de l'ajout du tournoi.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private boolean validateInputs(String nom, String adresse, Sport sport, Date dateDebut, Date dateFin) {
        // Check if the name is at least 5 characters long
        if (nom == null || nom.trim().isEmpty() || nom.length() < 5) {
            showDialog("Erreur", "Le nom du tournoi doit contenir au moins 5 caractères.", Alert.AlertType.ERROR);
            return false;
        }

        // Check if the start date is before the end date
        if (dateDebut == null || dateFin == null || dateDebut.after(dateFin)) {
            showDialog("Erreur", "La date de début doit être antérieure à la date de fin.", Alert.AlertType.ERROR);
            return false;
        }

        // Check if the address is empty
        if (adresse == null || adresse.trim().isEmpty()) {
            showDialog("Erreur", "L'adresse du tournoi est requise.", Alert.AlertType.ERROR);
            return false;
        }

        // Check if the sport is selected
        if (sport == null) {
            showDialog("Erreur", "Veuillez sélectionner un sport.", Alert.AlertType.ERROR);
            return false;
        }

        return true; // All validations passed
    }

    private void showDialog(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveTournoisToDatabase(Tournois tournois) {
        String sql = "INSERT INTO tournois (nom, sport, date_debut, date_fin, adresse) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getInstance().getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tournois.getNom());
            pstmt.setString(2, tournois.getSport().name());
            pstmt.setDate(3, new java.sql.Date(tournois.getDateDebut().getTime()));
            pstmt.setDate(4, new java.sql.Date(tournois.getDateFin().getTime()));
            pstmt.setString(5, tournois.getAdresse());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
