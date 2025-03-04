package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.module5.PerformanceAthlete;
import services.module5.PerformanceService;
import tools.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PerformanceEController {

    @FXML private TextField athleteIdField, matchIdField, minutesJoueesField, butsField, passesDecisivesField, tirsField,
            interceptionsField, fautesField, cartonsJaunesField, cartonsRougesField, rebondsField;

    @FXML private Label errorLabel;
    @FXML private TableView<PerformanceAthlete> performanceTable;
    @FXML private TableColumn<PerformanceAthlete, Integer> idColumn, athleteIdColumn, matchIdColumn, minutesJoueesColumn,
            butsColumn, passesDecisivesColumn, tirsColumn, interceptionsColumn, fautesColumn, cartonsJaunesColumn, cartonsRougesColumn, rebondsColumn;

    private Connection conn;
    private PerformanceService performanceService;

    @FXML
    public void initialize() {
        conn = DbConnection.getInstance().getConn();
        if (conn == null) {
            errorLabel.setText("Erreur de connexion à la base de données.");
            return;
        }
        // Rest of the initialization code

        performanceService = new PerformanceService(conn);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        athleteIdColumn.setCellValueFactory(new PropertyValueFactory<>("athleteId"));
        matchIdColumn.setCellValueFactory(new PropertyValueFactory<>("matchId"));
        minutesJoueesColumn.setCellValueFactory(new PropertyValueFactory<>("minutesJouees"));
        butsColumn.setCellValueFactory(new PropertyValueFactory<>("buts"));
        passesDecisivesColumn.setCellValueFactory(new PropertyValueFactory<>("passesDecisives"));
        tirsColumn.setCellValueFactory(new PropertyValueFactory<>("tirs"));
        interceptionsColumn.setCellValueFactory(new PropertyValueFactory<>("interceptions"));
        fautesColumn.setCellValueFactory(new PropertyValueFactory<>("fautes"));
        cartonsJaunesColumn.setCellValueFactory(new PropertyValueFactory<>("cartonsJaunes"));
        cartonsRougesColumn.setCellValueFactory(new PropertyValueFactory<>("cartonsRouges"));
        rebondsColumn.setCellValueFactory(new PropertyValueFactory<>("rebonds"));

        performanceTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) populateFormWithPerformanceData(newValue);
        });

        loadPerformances();
    }

    private void populateFormWithPerformanceData(PerformanceAthlete performance) {
        athleteIdField.setText(String.valueOf(performance.getAthleteId()));
        matchIdField.setText(String.valueOf(performance.getMatchId()));
        minutesJoueesField.setText(String.valueOf(performance.getMinutesJouees()));
        butsField.setText(String.valueOf(performance.getButs()));
        passesDecisivesField.setText(String.valueOf(performance.getPassesDecisives()));
        tirsField.setText(String.valueOf(performance.getTirs()));
        interceptionsField.setText(String.valueOf(performance.getInterceptions()));
        fautesField.setText(String.valueOf(performance.getFautes()));
        cartonsJaunesField.setText(String.valueOf(performance.getCartonsJaunes()));
        cartonsRougesField.setText(String.valueOf(performance.getCartonsRouges()));
        rebondsField.setText(String.valueOf(performance.getRebonds()));
    }

    private void loadPerformances() {
        try {
            List<PerformanceAthlete> performances = performanceService.getPerformances();
            performanceTable.getItems().setAll(performances);
        } catch (SQLException e) {
            errorLabel.setText("Erreur lors du chargement des performances.");
        }
    }

    @FXML
    private void ajouterPerformance() {
        // Add validation and handling logic here...
    }

    @FXML
    private void modifierPerformance() {
        // Modify existing performance record...
    }

    @FXML
    private void supprimerPerformance() {
        // Delete selected performance...
    }
}
