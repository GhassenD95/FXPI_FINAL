package controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.MatchSportif;
import services.EquipeService;
import services.MatchService;
import services.TournoiService;
import services.WeatherService;
import tools.MyDataBase;
import utils.NavigationUtils;

public class MatchSportifController {

    @FXML private TextField tournoisIdField, equipe1IdField, equipe2IdField, lieuField;
    @FXML private DatePicker dateField;
    @FXML private Label errorLabel;
    @FXML private TableView<MatchSportif> matchTable;
    @FXML private TableColumn<MatchSportif, Integer> idColumn;
    @FXML private TableColumn<MatchSportif, String> tournoiColumn, equipe1Column, equipe2Column, lieuColumn;
    @FXML private TableColumn<MatchSportif, Timestamp> dateColumn;
    @FXML private Button retourButton;

    private Connection conn;
    private MatchService matchService;
    private EquipeService equipeService;
    private TournoiService tournoiService;

    @FXML
    public void initialize() {
        conn = MyDataBase.getInstance().getCnx();
        if (conn == null) {
            errorLabel.setText("Erreur de connexion à la base de données.");
            return;
        }

        matchService = new MatchService(conn);
        equipeService = new EquipeService(conn);
        tournoiService = new TournoiService(conn);

        // Masquer la colonne ID
        idColumn.setVisible(false);

        // Initialisation des colonnes de la TableView
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tournoiColumn.setCellValueFactory(cellData -> {
            try {
                return new javafx.beans.property.SimpleStringProperty(
                    tournoiService.getTournoiNameById(cellData.getValue().getTournoisId())
                );
            } catch (SQLException e) {
                return new javafx.beans.property.SimpleStringProperty("Erreur");
            }
        });
        equipe1Column.setCellValueFactory(cellData -> {
            try {
                return new javafx.beans.property.SimpleStringProperty(
                    equipeService.getEquipeNameById(cellData.getValue().getEquipe1Id())
                );
            } catch (SQLException e) {
                return new javafx.beans.property.SimpleStringProperty("Erreur");
            }
        });
        equipe2Column.setCellValueFactory(cellData -> {
            try {
                return new javafx.beans.property.SimpleStringProperty(
                    equipeService.getEquipeNameById(cellData.getValue().getEquipe2Id())
                );
            } catch (SQLException e) {
                return new javafx.beans.property.SimpleStringProperty("Erreur");
            }
        });
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieu"));

        // Ajout du double-clic pour modifier un match
        matchTable.setRowFactory(tv -> {
            TableRow<MatchSportif> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    MatchSportif match = row.getItem();
                    ouvrirFormulaireModification(match);
                }
            });
            return row;
        });

        // Ajouter l'action du bouton retour
        retourButton.setOnAction(e -> {
            NavigationUtils.navigateTo("/views/home_view.fxml", "Accueil");
            NavigationUtils.closeCurrentWindow(retourButton.getScene().getRoot());
        });

        loadMatches();
    }

    private void populateFormWithMatchData(MatchSportif match) {
        tournoisIdField.setText(String.valueOf(match.getTournoisId()));
        equipe1IdField.setText(String.valueOf(match.getEquipe1Id()));
        equipe2IdField.setText(String.valueOf(match.getEquipe2Id()));
        if (match.getTimestamp() != null) {
            dateField.setValue(match.getTimestamp().toLocalDateTime().toLocalDate());
        }
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
        ouvrirFormulaireAjout();
    }

    @FXML
    private void modifierMatch() {
        MatchSportif selected = matchTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ouvrirFormulaireModification(selected);
        } else {
            errorLabel.setText("Veuillez sélectionner un match à modifier.");
        }
    }

    private void ouvrirFormulaireAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MatchForm.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Match");
            stage.setScene(scene);

            MatchFormController controller = loader.getController();
            controller.setMatchService(matchService.getConnection());

            stage.showAndWait();
            loadMatches();
        } catch (Exception e) {
            errorLabel.setText("Erreur lors de l'ouverture du formulaire: " + e.getMessage());
        }
    }

    private void ouvrirFormulaireModification(MatchSportif match) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MatchForm.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Modifier un Match");
            stage.setScene(scene);

            MatchFormController controller = loader.getController();
            controller.setMatchService(matchService.getConnection());
            controller.setMatchToEdit(match);

            stage.showAndWait();
            loadMatches();
        } catch (Exception e) {
            errorLabel.setText("Erreur lors de l'ouverture du formulaire: " + e.getMessage());
        }
    }

    @FXML
    private void supprimerMatch() {
        MatchSportif selected = matchTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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
    @FXML
    private void afficherMeteo() {
        MatchSportif selectedMatch = matchTable.getSelectionModel().getSelectedItem();
        if (selectedMatch != null) {
            String lieu = selectedMatch.getLieu();
            String weatherInfo = WeatherService.getWeather(lieu);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Météo du match");
            alert.setHeaderText("Conditions météorologiques à " + lieu);
            alert.setContentText(weatherInfo);
            alert.showAndWait();
        } else {
            errorLabel.setText("Veuillez sélectionner un match.");
        }
    }

}
