package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.MatchSportif;
import models.PerformanceAthlete;
import services.AthleteService;
import services.MatchService;
import services.PerformanceService;

public class PerformanceFormController {
    @FXML private Label titleLabel;
    @FXML private ComboBox<String> athleteComboBox;
    @FXML private ComboBox<String> matchComboBox;
    @FXML private TextField minutesJoueesField;
    @FXML private TextField butsField;
    @FXML private TextField passesDecisivesField;
    @FXML private TextField tirsField;
    @FXML private TextField interceptionsField;
    @FXML private TextField fautesField;
    @FXML private TextField cartonsJaunesField;
    @FXML private TextField cartonsRougesField;
    @FXML private TextField rebondsField;
    @FXML private Label errorLabel;

    private PerformanceService performanceService;
    private MatchService matchService;
    private AthleteService athleteService;
    private PerformanceAthlete performanceToEdit;
    private boolean isEditMode = false;

    public void initialize() {
        // Initialisation si nécessaire
    }

    public void setPerformanceService(Connection conn) {
        this.performanceService = new PerformanceService(conn);
        this.matchService = new MatchService(conn);
        this.athleteService = new AthleteService(conn);
        loadComboBoxes();
    }

    private void loadComboBoxes() {
        try {
            // Charger les athlètes
            List<String> athleteNames = performanceService.getAllAthleteNames();
            athleteComboBox.getItems().addAll(athleteNames);
            
            // Charger les matchs
            List<String> matchNames = matchService.getAllMatchNames();
            matchComboBox.getItems().addAll(matchNames);
        } catch (SQLException e) {
            errorLabel.setText("Erreur lors du chargement des listes: " + e.getMessage());
        }
    }

    public void setPerformanceToEdit(PerformanceAthlete performance) {
        this.performanceToEdit = performance;
        this.isEditMode = true;
        titleLabel.setText("Modifier une Performance");
        
        // Remplir les champs avec les données de la performance
        try {
            // Remplir l'athlète
            String athleteName = performanceService.getAthleteNameById(performance.getAthleteId());
            athleteComboBox.setValue(athleteName);

            // Remplir le match
            String matchNom = matchService.getMatchNameById(performance.getMatchId());
            matchComboBox.setValue(matchNom);
            
            // Remplir les autres champs
            minutesJoueesField.setText(String.valueOf(performance.getMinutesJouees()));
            butsField.setText(String.valueOf(performance.getButs()));
            passesDecisivesField.setText(String.valueOf(performance.getPassesDecisives()));
            tirsField.setText(String.valueOf(performance.getTirs()));
            interceptionsField.setText(String.valueOf(performance.getInterceptions()));
            fautesField.setText(String.valueOf(performance.getFautes()));
            cartonsJaunesField.setText(String.valueOf(performance.getCartonsJaunes()));
            cartonsRougesField.setText(String.valueOf(performance.getCartonsRouges()));
            rebondsField.setText(String.valueOf(performance.getRebonds()));
        } catch (SQLException e) {
            errorLabel.setText("Erreur lors du chargement des données: " + e.getMessage());
        }
    }

    private boolean validerDonnees() {
        // Vérifier que les champs ne sont pas vides
        if (athleteComboBox.getValue() == null || matchComboBox.getValue() == null || 
            minutesJoueesField.getText().isEmpty() || butsField.getText().isEmpty() || 
            passesDecisivesField.getText().isEmpty() || tirsField.getText().isEmpty() || 
            interceptionsField.getText().isEmpty() || fautesField.getText().isEmpty() || 
            cartonsJaunesField.getText().isEmpty() || cartonsRougesField.getText().isEmpty() || 
            rebondsField.getText().isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return false;
        }

        // Vérifier que les valeurs sont des nombres valides
        try {
            int minutesJouees = Integer.parseInt(minutesJoueesField.getText());
            int buts = Integer.parseInt(butsField.getText());
            int passesDecisives = Integer.parseInt(passesDecisivesField.getText());
            int tirs = Integer.parseInt(tirsField.getText());
            int interceptions = Integer.parseInt(interceptionsField.getText());
            int fautes = Integer.parseInt(fautesField.getText());
            int cartonsJaunes = Integer.parseInt(cartonsJaunesField.getText());
            int cartonsRouges = Integer.parseInt(cartonsRougesField.getText());
            int rebonds = Integer.parseInt(rebondsField.getText());

            // Vérifications de validité
            if (minutesJouees < 0 || minutesJouees > 120) {
                errorLabel.setText("Les minutes jouées doivent être comprises entre 0 et 120.");
                return false;
            }
            if (buts < 0) {
                errorLabel.setText("Le nombre de buts ne peut pas être négatif.");
                return false;
            }
            if (passesDecisives < 0) {
                errorLabel.setText("Le nombre de passes décisives ne peut pas être négatif.");
                return false;
            }
            if (tirs < 0) {
                errorLabel.setText("Le nombre de tirs ne peut pas être négatif.");
                return false;
            }
            if (buts > tirs) {
                errorLabel.setText("Le nombre de buts ne peut pas être supérieur au nombre de tirs.");
                return false;
            }
            if (interceptions < 0) {
                errorLabel.setText("Le nombre d'interceptions ne peut pas être négatif.");
                return false;
            }
            if (fautes < 0) {
                errorLabel.setText("Le nombre de fautes ne peut pas être négatif.");
                return false;
            }
            if (cartonsJaunes < 0 || cartonsJaunes > 2) {
                errorLabel.setText("Le nombre de cartons jaunes doit être compris entre 0 et 2.");
                return false;
            }
            if (cartonsRouges < 0 || cartonsRouges > 1) {
                errorLabel.setText("Le nombre de cartons rouges doit être compris entre 0 et 1.");
                return false;
            }
            if (rebonds < 0) {
                errorLabel.setText("Le nombre de rebonds ne peut pas être négatif.");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            errorLabel.setText("Veuillez entrer des nombres valides dans tous les champs.");
            return false;
        }
    }

    private boolean verifierEquipeAthlete(int athleteId, int matchId) {
        try {
            // Récupérer les équipes du match
            MatchSportif match = matchService.getMatchById(matchId);
            if (match == null) {
                System.out.println("Match non trouvé pour l'ID: " + matchId);
                return false;
            }
            System.out.println("Match trouvé: " + match.getEquipe1() + " vs " + match.getEquipe2());
            System.out.println("ID du match: " + matchId);

            // Récupérer l'équipe de l'athlète
            String equipeAthlete = athleteService.getEquipeAthlete(athleteId);
            if (equipeAthlete == null) {
                System.out.println("Équipe non trouvée pour l'athlète ID: " + athleteId);
                return false;
            }
            System.out.println("Équipe de l'athlète: " + equipeAthlete);
            System.out.println("ID de l'athlète: " + athleteId);

            // Vérifier si l'équipe de l'athlète est l'une des équipes du match
            boolean appartient = equipeAthlete.equals(match.getEquipe1()) || equipeAthlete.equals(match.getEquipe2());
            System.out.println("Comparaison des équipes:");
            System.out.println("Équipe1 du match: '" + match.getEquipe1() + "'");
            System.out.println("Équipe2 du match: '" + match.getEquipe2() + "'");
            System.out.println("Équipe de l'athlète: '" + equipeAthlete + "'");
            System.out.println("L'athlète appartient-il à l'une des équipes? " + appartient);
            return appartient;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'équipe: " + e.getMessage());
            e.printStackTrace();
            errorLabel.setText("Erreur lors de la vérification de l'équipe");
            return false;
        }
    }

    @FXML
    private void enregistrerPerformance() {
        if (!validerDonnees()) {
            return;
        }

        try {
            // Récupérer les IDs à partir des noms
            int athleteId = performanceService.getAthleteIdByName(athleteComboBox.getValue());
            int matchId = matchService.getMatchIdByName(matchComboBox.getValue());
            
            // Vérifier si l'athlète appartient à l'une des équipes du match
            if (!verifierEquipeAthlete(athleteId, matchId)) {
                errorLabel.setText("L'athlète sélectionné n'appartient pas à l'une des équipes du match");
                return;
            }

            // Créer l'objet PerformanceAthlete
            PerformanceAthlete performance = new PerformanceAthlete(
                isEditMode ? performanceToEdit.getId() : 0,
                athleteId,
                matchId,
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

            if (isEditMode) {
                performanceService.updatePerformance(performance);
            } else {
                try {
                    performanceService.addPerformance(performance);
                } catch (SQLException e) {
                    if (e.getMessage().contains("Une performance existe déjà")) {
                        errorLabel.setText("Une performance existe déjà pour cet athlète dans ce match");
                        return;
                    }
                    throw e;
                }
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