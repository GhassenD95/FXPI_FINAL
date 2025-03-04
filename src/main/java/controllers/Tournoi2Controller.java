package controllers;
import java.time.LocalDate;
import java.time.ZoneId;

import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.module4.Tournois;
import enums.Sport;
import services.module4.ServiceTournois;

public class Tournoi2Controller {

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

    private Tournois tournois;

    @FXML
    private void initialize() {
        disciplineComboBox.getItems().setAll(Sport.values());
    }


    public void setTournois(Tournois tournois) {
        this.tournois = tournois;
        nomField.setText(tournois.getNom());
        adresseField.setText(tournois.getAdresse());
        disciplineComboBox.setValue(tournois.getSport());

    }
    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }


    @FXML
    private void handleValiderButtonAction() {
        tournois.setNom(nomField.getText());
        tournois.setAdresse(adresseField.getText());
        tournois.setSport(disciplineComboBox.getValue());
        tournois.setDateDebut(java.sql.Date.valueOf(dateDebutPicker.getValue()));
        tournois.setDateFin(java.sql.Date.valueOf(dateFinPicker.getValue()));

        ServiceTournois serviceTournois = new ServiceTournois();

        serviceTournois.update(tournois);
        System.out.println("Tournoi has been updated!");
    }
}