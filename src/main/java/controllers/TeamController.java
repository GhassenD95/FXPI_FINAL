package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import enums.Divisions;
import enums.Sport;
import models.module1.Team;
import models.module1.User;
import services.module1.TeamService;
import services.module1.UserService;
import tools.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TeamController {

    @FXML private TextField nomField;
    @FXML private ComboBox<Divisions> divisionCombo;  // Énum Divisions (JUNIOR, SENIOR, VETERAN)
    @FXML private ComboBox<Sport> sportCombo;         // Énum Sport (FOOTBALL, BASKETBALL, HANDBALL)
    @FXML private ComboBox<User> coachCombo;

    @FXML private TableView<Team> teamTable;
    @FXML private TableColumn<Team, Integer> colId;
    @FXML private TableColumn<Team, String> colNom, colDivision, colSport;
    @FXML private TableColumn<Team, Integer> colCoach;

    private Connection conn;
    private TeamService teamService;
    private UserService userService;
    private ObservableList<Team> teamList = FXCollections.observableArrayList();
    private ObservableList<User> userList = FXCollections.observableArrayList();

    public TeamController() {
        // Use your DB singleton
        conn = DbConnection.getInstance().getConn();
        teamService = new TeamService(conn);
        userService = new UserService(conn);
    }

    @FXML
    public void initialize() {
        // Associer chaque colonne de la table aux propriétés du modèle Team
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDivision.setCellValueFactory(new PropertyValueFactory<>("division"));
        colCoach.setCellValueFactory(new PropertyValueFactory<>("coachId"));
        colSport.setCellValueFactory(new PropertyValueFactory<>("sport"));

        // Remplir la ComboBox division avec l'énum Divisions
        divisionCombo.setItems(FXCollections.observableArrayList(Divisions.values()));

        // Remplir la ComboBox sport avec l'énum Sport
        sportCombo.setItems(FXCollections.observableArrayList(Sport.values()));

        // Charger la liste initiale des teams + la liste des users (pour le coach)
        loadTeams();
        loadUsers();

        // Listener : quand on clique sur un team dans la table, on remplit le form
        teamTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fillForm(newVal);
            }
        });
    }

    private void loadTeams() {
        try {
            List<Team> teams = teamService.getTeams();
            teamList.setAll(teams);
            teamTable.setItems(teamList);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Erreur SQL", e.getMessage());
        }
    }

    private void loadUsers() {
        try {
            List<User> users = userService.getUsers();
            userList.setAll(users);
            coachCombo.setItems(userList);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Erreur SQL", e.getMessage());
        }
    }

    /**
     * Remplir le formulaire quand on sélectionne un Team dans la table
     */
    private void fillForm(Team team) {
        // Nom
        nomField.setText(team.getNom());

        // Division (string -> enum)
        try {
            Divisions div = Divisions.valueOf(team.getDivision());
            divisionCombo.setValue(div);
        } catch (Exception e) {
            divisionCombo.setValue(null);
        }

        // Sport (string -> enum)
        try {
            Sport sp = Sport.valueOf(team.getSport());
            sportCombo.setValue(sp);
        } catch (Exception e) {
            sportCombo.setValue(null);
        }

        // Coach
        for (User u : userList) {
            if (u.getId() == team.getCoachId()) {
                coachCombo.setValue(u);
                break;
            }
        }
    }

    /**
     * Ajouter un nouveau Team (avec contrôle de saisie minimal)
     */
    @FXML
    private void addTeam() {
        String nom = nomField.getText().trim();
        Divisions divValue = divisionCombo.getValue(); // énum
        Sport sportValue = sportCombo.getValue();      // énum

        User selectedCoach = coachCombo.getValue();
        int coachId = (selectedCoach != null) ? selectedCoach.getId() : 0;

        // Contrôle de saisie basique
        if (nom.isEmpty() || divValue == null || sportValue == null) {
            showErrorAlert("Champs obligatoires", "Veuillez saisir un nom, une division et un sport !");
            return;
        }

        // Construction de l'objet Team
        Team team = new Team(
                0,
                nom,
                divValue.name(),
                coachId,
                sportValue.name()
                );  // Set a default value or get it from the form if needed



        try {
            teamService.addTeam(team);
            loadTeams();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Erreur SQL", e.getMessage());
        }
    }

    /**
     * Mettre à jour un team sélectionné
     */
    @FXML
    private void updateTeam() {
        Team selectedTeam = teamTable.getSelectionModel().getSelectedItem();
        if (selectedTeam == null) {
            showErrorAlert("Aucun team sélectionné", "Veuillez sélectionner un team dans la table !");
            return;
        }

        // Récupération des champs
        String nom = nomField.getText().trim();
        Divisions divValue = divisionCombo.getValue();
        Sport sportValue = sportCombo.getValue();

        User selectedCoach = coachCombo.getValue();
        int coachId = (selectedCoach != null) ? selectedCoach.getId() : 0;

        // Vérification minimum
        if (nom.isEmpty() || divValue == null || sportValue == null) {
            showErrorAlert("Champs obligatoires", "Veuillez saisir un nom, une division et un sport !");
            return;
        }

        // Mise à jour de l'objet
        selectedTeam.setNom(nom);
        selectedTeam.setDivision(divValue.name());
        selectedTeam.setSport(sportValue.name());
        selectedTeam.setCoachId(coachId);

        try {
            teamService.updateTeam(selectedTeam);
            loadTeams();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Erreur SQL", e.getMessage());
        }
    }

    /**
     * Supprimer un team sélectionné
     */
    @FXML
    private void deleteTeam() {
        Team selectedTeam = teamTable.getSelectionModel().getSelectedItem();
        if (selectedTeam == null) {
            showErrorAlert("Aucun team sélectionné", "Veuillez sélectionner un team !");
            return;
        }

        try {
            teamService.deleteTeam(selectedTeam.getId());
            loadTeams();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Erreur SQL", e.getMessage());
        }
    }

    /**
     * Réinitialiser le formulaire
     */
    private void clearForm() {
        nomField.clear();
        divisionCombo.setValue(null);
        sportCombo.setValue(null);
        coachCombo.setValue(null);
        teamTable.getSelectionModel().clearSelection();
    }

    /**
     * Afficher une erreur dans une Alert
     */
    private void showErrorAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
