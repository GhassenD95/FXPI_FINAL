package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.MatchSportif;
import services.EquipeService;
import services.MatchService;
import services.TournoiService;

public class MatchFormController {
    @FXML private Label titleLabel;
    @FXML private ComboBox<String> tournoisComboBox;
    @FXML private ComboBox<String> equipe1ComboBox;
    @FXML private ComboBox<String> equipe2ComboBox;
    @FXML private DatePicker dateField;
    @FXML private TextField heureField;
    @FXML private TextField minuteField;
    @FXML private TextField lieuField;
    @FXML private Label errorLabel;

    private MatchService matchService;
    private EquipeService equipeService;
    private TournoiService tournoiService;
    private MatchSportif matchToEdit;
    private boolean isEditMode = false;

    public void initialize() {
        // Initialisation si nécessaire
    }

    public void setMatchService(Connection conn) {
        this.matchService = new MatchService(conn);
        this.equipeService = new EquipeService(conn);
        this.tournoiService = new TournoiService(conn);
        loadComboBoxes();
    }

    private void loadComboBoxes() {
        try {
            // Charger les noms des équipes
            List<String> equipeNames = equipeService.getAllEquipeNames();
            equipe1ComboBox.getItems().addAll(equipeNames);
            equipe2ComboBox.getItems().addAll(equipeNames);

            // Charger les noms des tournois
            List<String> tournoiNames = tournoiService.getAllTournoiNames();
            tournoisComboBox.getItems().addAll(tournoiNames);
        } catch (SQLException e) {
            errorLabel.setText("Erreur lors du chargement des listes: " + e.getMessage());
        }
    }

    public void setMatchToEdit(MatchSportif match) {
        this.matchToEdit = match;
        this.isEditMode = true;
        titleLabel.setText("Modifier un Match");
        
        // Remplir les champs avec les données du match
        try {
            // Remplir le tournoi
            String tournoiNom = tournoiService.getTournoiNameById(match.getTournoisId());
            tournoisComboBox.setValue(tournoiNom);

            // Remplir les équipes
            String equipe1Nom = equipeService.getEquipeNameById(match.getEquipe1Id());
            String equipe2Nom = equipeService.getEquipeNameById(match.getEquipe2Id());
            equipe1ComboBox.setValue(equipe1Nom);
            equipe2ComboBox.setValue(equipe2Nom);
            
            // Remplir la date et l'heure à partir du timestamp
            if (match.getTimestamp() != null) {
                Timestamp ts = match.getTimestamp();
                dateField.setValue(ts.toLocalDateTime().toLocalDate());
                heureField.setText(String.format("%02d", ts.getHours()));
                minuteField.setText(String.format("%02d", ts.getMinutes()));
            }
            
            lieuField.setText(match.getLieu());
        } catch (SQLException e) {
            errorLabel.setText("Erreur lors du chargement des données: " + e.getMessage());
        }
    }

    private boolean validerDonnees() {
        // Vérifier que les champs ne sont pas vides
        if (tournoisComboBox.getValue() == null || equipe1ComboBox.getValue() == null || 
            equipe2ComboBox.getValue() == null || lieuField.getText().isEmpty() || 
            dateField.getValue() == null || heureField.getText().isEmpty() || 
            minuteField.getText().isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return false;
        }

        // Vérifier que les équipes sont différentes
        if (equipe1ComboBox.getValue().equals(equipe2ComboBox.getValue())) {
            errorLabel.setText("Les équipes 1 et 2 doivent être différentes.");
            return false;
        }

        // Vérifier que l'heure est valide
        try {
            int heure = Integer.parseInt(heureField.getText());
            int minute = Integer.parseInt(minuteField.getText());

            if (heure < 0 || heure > 23) {
                errorLabel.setText("L'heure doit être comprise entre 0 et 23.");
                return false;
            }

            if (minute < 0 || minute > 59) {
                errorLabel.setText("Les minutes doivent être comprises entre 0 et 59.");
                return false;
            }

            // Vérification de la date
            if (isEditMode && matchToEdit != null) {
                // Pour la mise à jour, la nouvelle date doit être supérieure à la date existante
                Timestamp dateExistante = matchToEdit.getTimestamp();
                LocalDate nouvelleDate = dateField.getValue();
                Timestamp nouvelleTimestamp = Timestamp.valueOf(nouvelleDate.atTime(heure, minute));
                
                if (nouvelleTimestamp.before(dateExistante)) {
                    errorLabel.setText("La nouvelle date doit être supérieure à la date existante du match.");
                    return false;
                }
            } else {
                // Pour l'ajout, la date ne doit pas être dans le passé
                if (dateField.getValue().isBefore(LocalDate.now())) {
                    errorLabel.setText("La date du match ne peut pas être dans le passé.");
                    return false;
                }
            }

            return true;
        } catch (NumberFormatException e) {
            errorLabel.setText("Veuillez entrer des nombres valides pour l'heure.");
            return false;
        }
    }

    @FXML
    private void enregistrerMatch() {
        if (!validerDonnees()) {
            return;
        }

        try {
            // Récupérer les IDs à partir des noms
            int tournoisId = tournoiService.getTournoiIdByName(tournoisComboBox.getValue());
            int equipe1Id = equipeService.getEquipeIdByName(equipe1ComboBox.getValue());
            int equipe2Id = equipeService.getEquipeIdByName(equipe2ComboBox.getValue());
            int heure = Integer.parseInt(heureField.getText());
            int minute = Integer.parseInt(minuteField.getText());
            String lieu = lieuField.getText();

            // Créer le timestamp avec la date et l'heure
            LocalDate date = dateField.getValue();
            Timestamp timestamp = Timestamp.valueOf(date.atTime(heure, minute));

            if (isEditMode && matchToEdit != null) {
                matchToEdit.setTournoisId(tournoisId);
                matchToEdit.setEquipe1Id(equipe1Id);
                matchToEdit.setEquipe2Id(equipe2Id);
                matchToEdit.setTimestamp(timestamp);
                matchToEdit.setLieu(lieu);
                matchService.updateMatch(matchToEdit);
            } else {
                MatchSportif newMatch = new MatchSportif(0, tournoisId, equipe1Id, equipe2Id, timestamp, lieu);
                matchService.addMatch(newMatch);
            }

            // Fermer la fenêtre
            Stage stage = (Stage) titleLabel.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            errorLabel.setText("Erreur lors de l'enregistrement: " + e.getMessage());
        }
    }

    @FXML
    private void annuler() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }
} 