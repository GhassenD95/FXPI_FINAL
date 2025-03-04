package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import models.module5.MatchSportif;
import services.module5.MatchService;
import tools.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MatchSportifController {

    @FXML private TextField tournoisIdField, equipe1IdField, equipe2IdField, lieuField;
    @FXML private DatePicker dateField;
    @FXML private Label errorLabel;
    @FXML private TableView<MatchSportif> matchTable;
    @FXML private TableColumn<MatchSportif, Integer> idColumn, tournoiColumn, equipe1Column, equipe2Column;
    @FXML private TableColumn<MatchSportif, String> lieuColumn;
    @FXML private TableColumn<MatchSportif, java.util.Date> dateColumn;

    private Connection conn;
    private MatchService matchService;

    @FXML
    public void initialize() {
        conn = DbConnection.getInstance().getConn();
        if (conn == null) {
            errorLabel.setText("Erreur de connexion à la base de données.");
            return;
        }
        // Rest of the initialization code


        matchService = new MatchService(conn);

        // Initialisation des colonnes de la TableView
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tournoiColumn.setCellValueFactory(new PropertyValueFactory<>("tournoisId"));
        equipe1Column.setCellValueFactory(new PropertyValueFactory<>("equipe1Id"));
        equipe2Column.setCellValueFactory(new PropertyValueFactory<>("equipe2Id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieu"));

        // Ajout d'un écouteur pour la sélection de lignes
        matchTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateFormWithMatchData(newValue);
            }
        });

        loadMatches();
    }

    private void populateFormWithMatchData(MatchSportif match) {
        tournoisIdField.setText(String.valueOf(match.getTournoisId()));
        equipe1IdField.setText(String.valueOf(match.getEquipe1Id()));
        equipe2IdField.setText(String.valueOf(match.getEquipe2Id()));
        dateField.setValue(((java.sql.Date) match.getDate()).toLocalDate());
        lieuField.setText(match.getLieu());
    }

    private void loadMatches() {
        try {
            List<MatchSportif> matches = matchService.getMatches();
            matchTable.getItems().setAll(matches);
        } catch (SQLException e) {
            errorLabel.setText("Erreur lors du chargement des matchs.");
        }
    }

    @FXML
    private void ajouterMatch() {
        if (!isDateValid()) {
            return;
        }

        if (!areTeamsDifferent()) {
            return;
        }

        try {
            MatchSportif match = new MatchSportif(
                    0,
                    Integer.parseInt(tournoisIdField.getText()),
                    Integer.parseInt(equipe1IdField.getText()),
                    Integer.parseInt(equipe2IdField.getText()),
                    java.sql.Date.valueOf(dateField.getValue()),
                    lieuField.getText()
            );
            matchService.addMatch(match);
            loadMatches();
            clearFields();
            errorLabel.setText("Match ajouté avec succès !");
        } catch (Exception e) {
            errorLabel.setText("Erreur d'ajout du match.");
        }
    }

    @FXML
    private void modifierMatch() {
        MatchSportif selected = matchTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (!isDateValid()) {
                return;
            }

            if (!areTeamsDifferent()) {
                return;
            }

            try {
                selected.setTournoisId(Integer.parseInt(tournoisIdField.getText()));
                selected.setEquipe1Id(Integer.parseInt(equipe1IdField.getText()));
                selected.setEquipe2Id(Integer.parseInt(equipe2IdField.getText()));
                selected.setDate(java.sql.Date.valueOf(dateField.getValue()));
                selected.setLieu(lieuField.getText());

                matchService.updateMatch(selected);
                loadMatches();
                clearFields();
                errorLabel.setText("Match mis à jour avec succès !");
            } catch (Exception e) {
                errorLabel.setText("Erreur lors de la mise à jour du match.");
            }
        } else {
            errorLabel.setText("Veuillez sélectionner un match à modifier.");
        }
    }

    @FXML
    private void supprimerMatch() {
        MatchSportif selected = matchTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Voulez-vous vraiment supprimer ce match ?");
            alert.setContentText("Cette action est irréversible.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    matchService.deleteMatch(selected.getId());
                    loadMatches();
                    clearFields();
                    errorLabel.setText("Match supprimé avec succès !");
                } catch (SQLException e) {
                    errorLabel.setText("Erreur de suppression.");
                }
            }
        } else {
            errorLabel.setText("Veuillez sélectionner un match à supprimer.");
        }
    }

    private void clearFields() {
        tournoisIdField.clear();
        equipe1IdField.clear();
        equipe2IdField.clear();
        dateField.setValue(null);
        lieuField.clear();
    }

    private boolean isDateValid() {
        if (dateField.getValue() == null) {
            errorLabel.setText("Veuillez sélectionner une date.");
            return false;
        }

        if (dateField.getValue().isBefore(java.time.LocalDate.now())) {
            errorLabel.setText("La date ne peut pas être dans le passé.");
            return false;
        }

        return true;
    }

    private boolean areTeamsDifferent() {
        String equipe1Id = equipe1IdField.getText();
        String equipe2Id = equipe2IdField.getText();

        if (equipe1Id.equals(equipe2Id)) {
            errorLabel.setText("Les équipes 1 et 2 doivent être différentes.");
            return false;
        }

        return true;
    }
}
