package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.MatchSportif;
import models.PerformanceAthlete;
import services.AthleteService;
import services.MatchService;
import services.PerformanceService;
import tools.MyDataBase;
import utils.NavigationUtils;

public class PerformanceAthleteController {
    @FXML private Label errorLabel;
    @FXML private TableView<PerformanceAthlete> performanceTable;
    @FXML private TableColumn<PerformanceAthlete, String> athleteColumn;
    @FXML private TableColumn<PerformanceAthlete, String> matchColumn;
    @FXML private TableColumn<PerformanceAthlete, Integer> minutesColumn;
    @FXML private TableColumn<PerformanceAthlete, Integer> butsColumn;
    @FXML private TableColumn<PerformanceAthlete, Integer> passesColumn;
    @FXML private TableColumn<PerformanceAthlete, Integer> tirsColumn;
    @FXML private TableColumn<PerformanceAthlete, Integer> interceptionsColumn;
    @FXML private TableColumn<PerformanceAthlete, Integer> fautesColumn;
    @FXML private TableColumn<PerformanceAthlete, Integer> jaunesColumn;
    @FXML private TableColumn<PerformanceAthlete, Integer> rougesColumn;
    @FXML private TableColumn<PerformanceAthlete, Integer> rebondsColumn;
    @FXML private Button retourButton;
    @FXML private ComboBox<String> matchComboBox;
    @FXML private ComboBox<String> athleteComboBox;

    private Connection conn;
    private PerformanceService performanceService;
    private MatchService matchService;
    private AthleteService athleteService;

    @FXML
    private void initialize() {
        conn = MyDataBase.getInstance().getCnx();
        if (conn == null) {
            errorLabel.setText("Erreur de connexion à la base de données.");
            return;
        }

        performanceService = new PerformanceService(conn);
        matchService = new MatchService(conn);
        athleteService = new AthleteService(conn);

        // Initialiser les colonnes du tableau
        athleteColumn.setCellValueFactory(cellData -> {
            try {
                return new javafx.beans.property.SimpleStringProperty(
                    performanceService.getAthleteNameById(cellData.getValue().getAthleteId())
                );
            } catch (SQLException e) {
                return new javafx.beans.property.SimpleStringProperty("Erreur");
            }
        });
        matchColumn.setCellValueFactory(cellData -> {
            try {
                return new javafx.beans.property.SimpleStringProperty(
                    matchService.getMatchNameById(cellData.getValue().getMatchId())
                );
            } catch (SQLException e) {
                return new javafx.beans.property.SimpleStringProperty("Erreur");
            }
        });
        minutesColumn.setCellValueFactory(new PropertyValueFactory<>("minutesJouees"));
        butsColumn.setCellValueFactory(new PropertyValueFactory<>("buts"));
        passesColumn.setCellValueFactory(new PropertyValueFactory<>("passesDecisives"));
        tirsColumn.setCellValueFactory(new PropertyValueFactory<>("tirs"));
        interceptionsColumn.setCellValueFactory(new PropertyValueFactory<>("interceptions"));
        fautesColumn.setCellValueFactory(new PropertyValueFactory<>("fautes"));
        jaunesColumn.setCellValueFactory(new PropertyValueFactory<>("cartonsJaunes"));
        rougesColumn.setCellValueFactory(new PropertyValueFactory<>("cartonsRouges"));
        rebondsColumn.setCellValueFactory(new PropertyValueFactory<>("rebonds"));

        loadPerformances();

        // Ajouter l'action du bouton retour
        retourButton.setOnAction(e -> {
            NavigationUtils.navigateTo("/views/home_view.fxml", "Accueil");
            NavigationUtils.closeCurrentWindow(retourButton.getScene().getRoot());
        });
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/performance_form.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Ajouter une Performance");
            stage.setScene(scene);

            PerformanceFormController controller = loader.getController();
            controller.setPerformanceService(conn);

            stage.showAndWait();
            loadPerformances();
        } catch (Exception e) {
            errorLabel.setText("Erreur lors de l'ouverture du formulaire.");
            e.printStackTrace();
        }
    }

    @FXML
    private void modifierPerformance() {
        PerformanceAthlete selected = performanceTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/performance_form.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setTitle("Modifier une Performance");
                stage.setScene(scene);

                PerformanceFormController controller = loader.getController();
                controller.setPerformanceService(conn);
                controller.setPerformanceToEdit(selected);

                stage.showAndWait();
                loadPerformances();
            } catch (Exception e) {
                errorLabel.setText("Erreur lors de l'ouverture du formulaire.");
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Veuillez sélectionner une performance à modifier.");
        }
    }

    @FXML
    private void supprimerPerformance() {
        PerformanceAthlete selected = performanceTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Voulez-vous vraiment supprimer cette performance ?");
            alert.setContentText("Cette action est irréversible.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    performanceService.deletePerformance(selected.getId());
                    loadPerformances();
                    errorLabel.setText("Performance supprimée avec succès !");
                } catch (SQLException e) {
                    errorLabel.setText("Erreur de suppression.");
                }
            }
        } else {
            errorLabel.setText("Veuillez sélectionner une performance à supprimer.");
        }
    }

    private void afficherErreur(String message) {
        errorLabel.setText(message);
    }

    private void chargerListeMatchs() {
        try {
            List<MatchSportif> matchs = matchService.getAllMatchs();
            matchComboBox.getItems().clear();
            for (MatchSportif match : matchs) {
                matchComboBox.getItems().add(match.getTournoi() + " - " + match.getEquipe1() + " vs " + match.getEquipe2());
            }
        } catch (SQLException e) {
            afficherErreur("Erreur lors du chargement des matchs");
            e.printStackTrace();
        }
    }

    private boolean verifierEquipeAthlete(int athleteId, int matchId) {
        try {
            // Récupérer les équipes du match
            MatchSportif match = matchService.getMatchById(matchId);
            if (match == null) return false;

            // Récupérer l'équipe de l'athlète
            String equipeAthlete = athleteService.getEquipeAthlete(athleteId);
            if (equipeAthlete == null) return false;

            // Vérifier si l'équipe de l'athlète est l'une des équipes du match
            return equipeAthlete.equals(match.getEquipe1()) || equipeAthlete.equals(match.getEquipe2());
        } catch (SQLException e) {
            afficherErreur("Erreur lors de la vérification de l'équipe");
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void enregistrerPerformance() {
        try {
            String matchSelectionne = matchComboBox.getValue();
            String athleteSelectionne = athleteComboBox.getValue();
            
            if (matchSelectionne == null || athleteSelectionne == null) {
                afficherErreur("Veuillez sélectionner un match et un athlète");
                return;
            }

            // Récupérer l'ID du match et de l'athlète
            int matchId = matchService.getMatchIdByName(matchSelectionne);
            int athleteId = athleteService.getAthleteIdByName(athleteSelectionne);

            // Vérifier si l'athlète appartient à l'une des équipes du match
            if (!verifierEquipeAthlete(athleteId, matchId)) {
                afficherErreur("L'athlète sélectionné n'appartient pas à l'une des équipes du match");
                return;
            }

            // ... rest of the existing code ...
        } catch (Exception e) {
            afficherErreur("Erreur lors de l'enregistrement de la performance");
            e.printStackTrace();
        }
    }
}
