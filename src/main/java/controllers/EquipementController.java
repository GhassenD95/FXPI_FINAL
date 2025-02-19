package controllers;

import enums.EtatEquipement;
import enums.TypeEquipement;
import models.module6.Equipement;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import services.module6.ServiceEquipement;

import java.sql.SQLException;
import java.util.List;

public class EquipementController {

    @FXML private TextField nomField;
    @FXML private TextArea descriptionField;
    @FXML private ComboBox<EtatEquipement> etatComboBox;
    @FXML private ComboBox<TypeEquipement> typeComboBox;
    @FXML private TextField quantiteField;
    @FXML private TableView<Equipement> equipementTable;
    @FXML private TableColumn<Equipement, String> nomColumn;
    @FXML private TableColumn<Equipement, String> descriptionColumn;
    @FXML private TableColumn<Equipement, String> etatColumn;
    @FXML private TableColumn<Equipement, String> typeColumn;
    @FXML private TableColumn<Equipement, String> quantiteColumn;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private final ServiceEquipement equipementService = new ServiceEquipement();

    @FXML
    public void initialize() {
        // Initialisation des ComboBox
        etatComboBox.setItems(FXCollections.observableArrayList(EtatEquipement.values()));
        typeComboBox.setItems(FXCollections.observableArrayList(TypeEquipement.values()));

        // Configuration des colonnes de la TableView
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        etatColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtat().toString()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeEquipement().toString()));
        quantiteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getQuantite())));

        // Chargement initial des équipements
        loadEquipements();
    }

    @FXML
    public void handleAdd() {
        try {
            String nom = nomField.getText().trim();
            String description = descriptionField.getText().trim();
            EtatEquipement etat = etatComboBox.getValue();
            TypeEquipement type = typeComboBox.getValue();
            int quantite = parseQuantite(quantiteField.getText().trim());

            if (nom.isEmpty() || description.isEmpty() || etat == null || type == null || quantite < 0) {
                showAlert("Erreur", "Veuillez remplir tous les champs correctement.", Alert.AlertType.ERROR);
                return;
            }

            Equipement equipement = new Equipement(nom, description, etat, type, null, quantite, null);
            equipementService.add(equipement);

            loadEquipements();
            clearFields();
            showAlert("Ajout Réussi", "L'équipement a été ajouté avec succès.", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ajouter l'équipement.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleEdit() {
        Equipement selectedEquipement = equipementTable.getSelectionModel().getSelectedItem();

        if (selectedEquipement != null) {
            try {
                String nom = nomField.getText().trim();
                String description = descriptionField.getText().trim();
                EtatEquipement etat = etatComboBox.getValue();
                TypeEquipement type = typeComboBox.getValue();
                int quantite = parseQuantite(quantiteField.getText().trim());

                if (nom.isEmpty() || description.isEmpty() || etat == null || type == null || quantite < 0) {
                    showAlert("Erreur", "Veuillez remplir tous les champs correctement.", Alert.AlertType.ERROR);
                    return;
                }

                selectedEquipement.setNom(nom);
                selectedEquipement.setDescription(description);
                selectedEquipement.setEtat(etat);
                selectedEquipement.setTypeEquipement(type);
                selectedEquipement.setQuantite(quantite);

                equipementService.edit(selectedEquipement);

                loadEquipements();
                clearFields();
                showAlert("Modification Réussie", "L'équipement a été modifié avec succès.", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de modifier l'équipement.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Sélectionnez un équipement", "Veuillez sélectionner un équipement à modifier.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void handleDelete() {
        Equipement selectedEquipement = equipementTable.getSelectionModel().getSelectedItem();

        if (selectedEquipement != null) {
            try {
                equipementService.delete(selectedEquipement);
                loadEquipements();
                clearFields();
                showAlert("Suppression Réussie", "L'équipement a été supprimé avec succès.", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur est survenue lors de la suppression de l'équipement.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Sélectionnez un équipement", "Veuillez sélectionner un équipement à supprimer.", Alert.AlertType.WARNING);
        }
    }

    private void loadEquipements() {
        try {
            List<Equipement> equipements = equipementService.getAll();
            equipementTable.getItems().setAll(equipements);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de récupérer la liste des équipements.", Alert.AlertType.ERROR);
        }
    }

    private void clearFields() {
        nomField.clear();
        descriptionField.clear();
        etatComboBox.getSelectionModel().clearSelection();
        typeComboBox.getSelectionModel().clearSelection();
        quantiteField.clear();
    }

    private int parseQuantite(String text) {
        try {
            int quantite = Integer.parseInt(text);
            return Math.max(0, quantite); // Empêche les valeurs négatives
        } catch (NumberFormatException e) {
            return -1; // Code d'erreur
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

