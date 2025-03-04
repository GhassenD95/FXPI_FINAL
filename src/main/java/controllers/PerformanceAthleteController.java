package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import models.module5.PerformanceAthlete;
import services.module5.PerformanceService;
import tools.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PerformanceAthleteController {
    @FXML private TextField athleteIdField, matchIdField, minutesJoueesField, butsField, passesDecisivesField, tirsField,
            interceptionsField, fautesField, cartonsJaunesField, cartonsRougesField, rebondsField;
    @FXML private Label errorLabel;
    @FXML private TableView<PerformanceAthlete> performanceTable;
    @FXML private TableColumn<PerformanceAthlete, Integer> idColumn, athleteColumn, matchColumn, minutesColumn,
            butsColumn, passesColumn, tirsColumn, interceptionsColumn, fautesColumn, jaunesColumn, rougesColumn, rebondsColumn;

    private Connection conn;
    private PerformanceService performanceService;

    @FXML
    public void initialize() {
        conn = DbConnection.getInstance().getConn();
        if (conn == null) {
            errorLabel.setText("Erreur de connexion à la base de données.");
            return;
        }


        performanceService = new PerformanceService(conn);

        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        athleteColumn.setCellValueFactory(new PropertyValueFactory<>("athleteId"));
        matchColumn.setCellValueFactory(new PropertyValueFactory<>("matchId"));
        minutesColumn.setCellValueFactory(new PropertyValueFactory<>("minutesJouees"));
        butsColumn.setCellValueFactory(new PropertyValueFactory<>("buts"));
        passesColumn.setCellValueFactory(new PropertyValueFactory<>("passesDecisives"));
        tirsColumn.setCellValueFactory(new PropertyValueFactory<>("tirs"));
        interceptionsColumn.setCellValueFactory(new PropertyValueFactory<>("interceptions"));
        fautesColumn.setCellValueFactory(new PropertyValueFactory<>("fautes"));
        jaunesColumn.setCellValueFactory(new PropertyValueFactory<>("cartonsJaunes"));
        rougesColumn.setCellValueFactory(new PropertyValueFactory<>("cartonsRouges"));
        rebondsColumn.setCellValueFactory(new PropertyValueFactory<>("rebonds"));

        // Add listener for row selection
        performanceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFormWithPerformance(newSelection);
            }
        });

        loadPerformances();
    }

    private void loadPerformances() {
        try {
            List<PerformanceAthlete> performances = performanceService.getPerformances();
            performanceTable.getItems().setAll(performances);
        } catch (SQLException e) {
            errorLabel.setText("Erreur lors du chargement des performances.");
        }
    }

    private void populateFormWithPerformance(PerformanceAthlete performance) {
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

    @FXML
    private void ajouterPerformance() {
        try {
            PerformanceAthlete performance = new PerformanceAthlete(
                    0,
                    Integer.parseInt(athleteIdField.getText()),
                    Integer.parseInt(matchIdField.getText()),
                    Integer.parseInt(minutesJoueesField.getText()),
                    Integer.parseInt(butsField.getText()),
                    Integer.parseInt(passesDecisivesField.getText()),
                    Integer.parseInt(tirsField.getText()),
                    Integer.parseInt(interceptionsField.getText()),
                    Integer.parseInt(fautesField.getText()),
                    Integer.parseInt(cartonsJaunesField.getText()),
                    Integer.parseInt(cartonsRougesField.getText()),
                    Integer.parseInt(rebondsField.getText())
            );
            performanceService.addPerformance(performance);
            loadPerformances();
            clearFields();
            errorLabel.setText("Performance ajoutée avec succès !");
        } catch (Exception e) {
            errorLabel.setText("Erreur d'ajout.");
        }
    }

    @FXML
    private void modifierPerformance() {
        PerformanceAthlete selected = performanceTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setAthleteId(Integer.parseInt(athleteIdField.getText()));
                selected.setMatchId(Integer.parseInt(matchIdField.getText()));
                selected.setMinutesJouees(Integer.parseInt(minutesJoueesField.getText()));
                selected.setButs(Integer.parseInt(butsField.getText()));
                selected.setPassesDecisives(Integer.parseInt(passesDecisivesField.getText()));
                selected.setTirs(Integer.parseInt(tirsField.getText()));
                selected.setInterceptions(Integer.parseInt(interceptionsField.getText()));
                selected.setFautes(Integer.parseInt(fautesField.getText()));
                selected.setCartonsJaunes(Integer.parseInt(cartonsJaunesField.getText()));
                selected.setCartonsRouges(Integer.parseInt(cartonsRougesField.getText()));
                selected.setRebonds(Integer.parseInt(rebondsField.getText()));

                performanceService.updatePerformance(selected);
                loadPerformances();
                clearFields();
                errorLabel.setText("Performance mise à jour avec succès !");
            } catch (Exception e) {
                errorLabel.setText("Erreur de modification.");
            }
        } else {
            errorLabel.setText("Veuillez sélectionner une performance à modifier.");
        }
    }

    @FXML
    private void supprimerPerformance() {
        PerformanceAthlete selected = performanceTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // 🔹 Création d'une boîte de dialogue de confirmation
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Voulez-vous vraiment supprimer cette performance ?");
            alert.setContentText("Cette action est irréversible.");

            // 🔹 Attente de la réponse de l'utilisateur
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    performanceService.deletePerformance(selected.getId());
                    loadPerformances();
                    clearFields();
                    errorLabel.setText("Performance supprimée avec succès !");
                } catch (SQLException e) {
                    errorLabel.setText("Erreur de suppression.");
                }
            }
        } else {
            errorLabel.setText("Veuillez sélectionner une performance à supprimer.");
        }
    }

    private void clearFields() {
        athleteIdField.clear();
        matchIdField.clear();
        minutesJoueesField.clear();
        butsField.clear();
        passesDecisivesField.clear();
        tirsField.clear();
        interceptionsField.clear();
        fautesField.clear();
        cartonsJaunesField.clear();
        cartonsRougesField.clear();
        rebondsField.clear();
    }
}
