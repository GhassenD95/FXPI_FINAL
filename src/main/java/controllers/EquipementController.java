package controllers;

import enums.EtatEquipement;
import enums.TypeEquipement;
import models.module6.Equipement;
import models.module6.InstallationSportive;
import services.EquipementService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class EquipementController {

    @FXML
    private TextField nomField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private ComboBox<EtatEquipement> etatComboBox;

    @FXML
    private ComboBox<TypeEquipement> typeComboBox;

    @FXML
    private TextField quantiteField;

    @FXML
    private TableView<Equipement> equipementTable;

    @FXML
    private TableColumn<Equipement, String> nomColumn;

    @FXML
    private TableColumn<Equipement, String> descriptionColumn;

    @FXML
    private TableColumn<Equipement, EtatEquipement> etatColumn;

    @FXML
    private TableColumn<Equipement, TypeEquipement> typeColumn;

    @FXML
    private TableColumn<Equipement, Integer> quantiteColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    private EquipementService equipementService;

    public EquipementController() {
        equipementService = new EquipementService();
    }

    @FXML
    public void initialize() {
        // Initialiser les ComboBox
        etatComboBox.getItems().setAll(EtatEquipement.values());
        typeComboBox.getItems().setAll(TypeEquipement.values());

        // Initialiser les colonnes de la TableView
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().getNom());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescription());
        etatColumn.setCellValueFactory(cellData -> cellData.getValue().getEtat());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeEquipement());
        quantiteColumn.setCellValueFactory(cellData -> cellData.getValue().getQuantite());

        // Charger les équipements
        loadEquipements();
    }

    @FXML
    public void handleAdd() {
        try {
            String nom = nomField.getText();
            String description = descriptionField.getText();
            EtatEquipement etat = etatComboBox.getValue();
            TypeEquipement type = typeComboBox.getValue();
            int quantite = Integer.parseInt(quantiteField.getText());

            // Créer un nouvel équipement
            Equipement equipement = new Equipement(nom, description, etat, type, null, quantite, null);

            // Ajouter l'équipement
            equipementService.add(equipement);

            // Réactualiser la TableView
            loadEquipements();

            showAlert("Ajout Réussi", "L'équipement a été ajouté avec succès", AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Erreur", "Veuillez remplir tous les champs correctement", AlertType.ERROR);
        }
    }

    @FXML
    public void handleEdit() {
        Equipement selectedEquipement = equipementTable.getSelectionModel().getSelectedItem();

        if (selectedEquipement != null) {
            try {
                String nom = nomField.getText();
                String description = descriptionField.getText();
                EtatEquipement etat = etatComboBox.getValue();
                TypeEquipement type = typeComboBox.getValue();
                int quantite = Integer.parseInt(quantiteField.getText());

                // Modifier l'équipement sélectionné
                selectedEquipement.setNom(nom);
                selectedEquipement.setDescription(description);
                selectedEquipement.setEtat(etat);
                selectedEquipement.setTypeEquipement(type);
                selectedEquipement.setQuantite(quantite);

                // Mettre à jour l'équipement
                equipementService.edit(selectedEquipement);

                // Réactualiser la TableView
                loadEquipements();

                showAlert("Modification Réussie", "L'équipement a été modifié avec succès", AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Erreur", "Veuillez remplir tous les champs correctement", AlertType.ERROR);
            }
        } else {
            showAlert("Sélectionnez un équipement", "Veuillez sélectionner un équipement à modifier", AlertType.WARNING);
        }
    }

    @FXML
    public void handleDelete() {
        Equipement selectedEquipement = equipementTable.getSelectionModel().getSelectedItem();

        if (selectedEquipement != null) {
            try {
                // Supprimer l'équipement
                equipementService.delete(selectedEquipement);

                // Réactualiser la TableView
                loadEquipements();

                showAlert("Suppression Réussie", "L'équipement a été supprimé avec succès", AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Erreur", "Une erreur est survenue lors de la suppression de l'équipement", AlertType.ERROR);
            }
        } else {
            showAlert("Sélectionnez un équipement", "Veuillez sélectionner un équipement à supprimer", AlertType.WARNING);
        }
    }

    private void loadEquipements() {
        equipementTable.getItems().setAll(equipementService.getAll());
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
