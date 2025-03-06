package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.StatistiquesAthlete;
import services.MatchService;
import services.StatistiquesService;
import tools.MyDataBase;
import utils.NavigationUtils;

public class StatistiquesController {
    @FXML private ComboBox<String> matchComboBox;
    @FXML private TableView<StatistiquesAthlete> statsMatchTable;
    @FXML private TableColumn<StatistiquesAthlete, String> athleteMatchColumn;
    @FXML private TableColumn<StatistiquesAthlete, Integer> minutesMatchColumn;
    @FXML private TableColumn<StatistiquesAthlete, Integer> butsMatchColumn;
    @FXML private TableColumn<StatistiquesAthlete, Integer> passesMatchColumn;
    @FXML private TableColumn<StatistiquesAthlete, Integer> tirsMatchColumn;
    @FXML private TableColumn<StatistiquesAthlete, Integer> interceptionsMatchColumn;
    @FXML private TableColumn<StatistiquesAthlete, Integer> fautesMatchColumn;
    @FXML private TableColumn<StatistiquesAthlete, Integer> cartonsJaunesMatchColumn;
    @FXML private TableColumn<StatistiquesAthlete, Integer> cartonsRougesMatchColumn;
    @FXML private TableColumn<StatistiquesAthlete, Integer> rebondsMatchColumn;

    @FXML private TableView<StatistiquesAthlete> statsAthletesTable;
    @FXML private TableColumn<StatistiquesAthlete, String> athleteNomColumn;
    @FXML private TableColumn<StatistiquesAthlete, Integer> matchsJouesAthleteColumn;
    @FXML private TableColumn<StatistiquesAthlete, Double> minutesMoyennesColumn;
    @FXML private TableColumn<StatistiquesAthlete, Integer> butsColumn;
    @FXML private TableColumn<StatistiquesAthlete, Double> moyenneButsColumn;
    @FXML private TableColumn<StatistiquesAthlete, Integer> passesColumn;
    @FXML private TableColumn<StatistiquesAthlete, Double> moyennePassesColumn;
    @FXML private TableColumn<StatistiquesAthlete, Double> efficaciteColumn;
    @FXML private TableColumn<StatistiquesAthlete, Integer> interceptionsColumn;
    @FXML private TableColumn<StatistiquesAthlete, Integer> fautesColumn;
    @FXML private TableColumn<StatistiquesAthlete, Integer> cartonsJaunesColumn;
    @FXML private TableColumn<StatistiquesAthlete, Integer> cartonsRougesColumn;
    @FXML private TableColumn<StatistiquesAthlete, Integer> rebondsColumn;

    @FXML private Button retourButton;

    private Connection conn;
    private StatistiquesService statistiquesService;
    private MatchService matchService;

    @FXML
    private void initialize() {
        conn = MyDataBase.getInstance().getCnx();
        if (conn == null) {
            System.err.println("Erreur : Impossible de se connecter à la base de données");
            return;
        }

        try {
            statistiquesService = new StatistiquesService(conn);
            matchService = new MatchService(conn);

            // Initialiser les colonnes des statistiques par match
            athleteMatchColumn.setCellValueFactory(new PropertyValueFactory<>("nomAthlete"));
            minutesMatchColumn.setCellValueFactory(new PropertyValueFactory<>("minutesTotales"));
            butsMatchColumn.setCellValueFactory(new PropertyValueFactory<>("buts"));
            passesMatchColumn.setCellValueFactory(new PropertyValueFactory<>("passesDecisives"));
            tirsMatchColumn.setCellValueFactory(new PropertyValueFactory<>("tirs"));
            interceptionsMatchColumn.setCellValueFactory(new PropertyValueFactory<>("interceptions"));
            fautesMatchColumn.setCellValueFactory(new PropertyValueFactory<>("fautes"));
            cartonsJaunesMatchColumn.setCellValueFactory(new PropertyValueFactory<>("cartonsJaunes"));
            cartonsRougesMatchColumn.setCellValueFactory(new PropertyValueFactory<>("cartonsRouges"));
            rebondsMatchColumn.setCellValueFactory(new PropertyValueFactory<>("rebonds"));

            // Initialiser les colonnes des statistiques des athlètes
            athleteNomColumn.setCellValueFactory(new PropertyValueFactory<>("nomAthlete"));
            matchsJouesAthleteColumn.setCellValueFactory(new PropertyValueFactory<>("matchsJoues"));
            minutesMoyennesColumn.setCellValueFactory(new PropertyValueFactory<>("moyenneMinutes"));
            butsColumn.setCellValueFactory(new PropertyValueFactory<>("buts"));
            moyenneButsColumn.setCellValueFactory(new PropertyValueFactory<>("moyenneButs"));
            passesColumn.setCellValueFactory(new PropertyValueFactory<>("passesDecisives"));
            moyennePassesColumn.setCellValueFactory(new PropertyValueFactory<>("moyennePasses"));
            efficaciteColumn.setCellValueFactory(new PropertyValueFactory<>("efficacite"));
            interceptionsColumn.setCellValueFactory(new PropertyValueFactory<>("interceptions"));
            fautesColumn.setCellValueFactory(new PropertyValueFactory<>("fautes"));
            cartonsJaunesColumn.setCellValueFactory(new PropertyValueFactory<>("cartonsJaunes"));
            cartonsRougesColumn.setCellValueFactory(new PropertyValueFactory<>("cartonsRouges"));
            rebondsColumn.setCellValueFactory(new PropertyValueFactory<>("rebonds"));

            // Charger la liste des matchs
            chargerListeMatchs();

            // Charger les statistiques générales par défaut
            afficherStatistiquesGenerales();

            // Ajouter l'action du bouton retour
            retourButton.setOnAction(e -> {
                NavigationUtils.navigateTo("/views/home_view.fxml", "Accueil");
                NavigationUtils.closeCurrentWindow(retourButton.getScene().getRoot());
            });

            // Ajouter l'action du ComboBox pour charger les performances du match sélectionné
            matchComboBox.setOnAction(e -> afficherStatistiquesMatch());
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void chargerListeMatchs() {
        try {
            List<String> matchs = matchService.getAllMatchNames();
            if (matchs.isEmpty()) {
                System.out.println("Aucun match disponible");
            }
            matchComboBox.getItems().addAll(matchs);
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des matchs : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void afficherMeilleursButeurs() {
        try {
            List<StatistiquesAthlete> meilleursButeurs = statistiquesService.getMeilleursButeurs();
            statsAthletesTable.getItems().setAll(meilleursButeurs);
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des meilleurs buteurs : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void afficherMeilleursPasseurs() {
        try {
            List<StatistiquesAthlete> meilleursPasseurs = statistiquesService.getMeilleursPasseurs();
            statsAthletesTable.getItems().setAll(meilleursPasseurs);
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des meilleurs passeurs : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void afficherStatistiquesGenerales() {
        try {
            List<StatistiquesAthlete> stats = statistiquesService.getStatistiquesAthletes();
            statsAthletesTable.getItems().setAll(stats);
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des statistiques générales : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void afficherStatistiquesMatch() {
        String matchSelectionne = matchComboBox.getValue();
        if (matchSelectionne != null) {
            try {
                int matchId = matchService.getMatchIdByName(matchSelectionne);
                List<StatistiquesAthlete> stats = statistiquesService.getStatistiquesParMatch(matchId);
                statsMatchTable.getItems().setAll(stats);
            } catch (SQLException e) {
                System.err.println("Erreur lors du chargement des statistiques du match : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
} 