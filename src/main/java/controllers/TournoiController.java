package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.module5.Tournois;
import enums.Sport;
import tools.DbConnection;
import services.module5.ServiceTournois;

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

        Tournois tournois = new Tournois(nom, sport, dateDebut, dateFin, adresse);
        ServiceTournois serviceTournois = new ServiceTournois();

        try {
            serviceTournois.add(tournois);
            System.out.println("Tournoi has been added!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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