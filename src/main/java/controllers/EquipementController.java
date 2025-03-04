package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.module6.Equipement;
import enums.EtatEquipement;
import enums.TypeEquipement;
import services.module6.ServiceEquipement;

import java.sql.SQLException;

public class EquipementController {

    @FXML
    private TextField nomField, descriptionField, imageField, quantiteField;
    @FXML
    private ComboBox<EtatEquipement> etatComboBox;
    @FXML
    private ComboBox<TypeEquipement> typeComboBox;
    @FXML
    private TableView<Equipement> equipementTable;
    @FXML
    private TableColumn<Equipement, String> nomColumn;
    @FXML
    private TableColumn<Equipement, EtatEquipement> etatColumn;
    @FXML
    private TableColumn<Equipement, TypeEquipement> typeColumn;
    @FXML
    private TableColumn<Equipement, Integer> quantiteColumn;

    private ServiceEquipement serviceEquipement;
    private ObservableList<Equipement> equipementList;

    @FXML
    public void initialize() {
        serviceEquipement = new ServiceEquipement();
        try {
            equipementList = FXCollections.observableArrayList((java.util.Collection<? extends Equipement>) serviceEquipement.getAllEquipements());
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les équipements.", Alert.AlertType.ERROR);
            return;
        }

        nomColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNom()));
        etatColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getEtat()));
        typeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTypeEquipement()));
        quantiteColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getQuantite()).asObject());


        equipementTable.setItems(equipementList);
        etatComboBox.setItems(FXCollections.observableArrayList(EtatEquipement.values()));
        typeComboBox.setItems(FXCollections.observableArrayList(TypeEquipement.values()));

        equipementTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillForm(newValue);
            }
        });
    }

    @FXML
    private void handleAdd() {
        try {
            String nom = (nomField.getText() != null) ? nomField.getText().trim() : "";
            String description = (descriptionField.getText() != null) ? descriptionField.getText().trim() : "";
            String imageUrl = (imageField.getText() != null) ? imageField.getText().trim() : "";
            String quantiteText = (quantiteField.getText() != null) ? quantiteField.getText().trim() : "";

            EtatEquipement etat = etatComboBox.getValue();
            TypeEquipement type = typeComboBox.getValue();

            if (nom.isEmpty() || description.isEmpty() || etat == null || type == null || quantiteText.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs requis.", Alert.AlertType.ERROR);
                return;
            }

            int quantite = Integer.parseInt(quantiteText);

            Equipement newEquipement = new Equipement(nom, description, etat, type, imageUrl, quantite);
            serviceEquipement.ajouterEquipement(newEquipement);
            equipementList.add(newEquipement);
            clearFields();
            showAlert("Succès", "Équipement ajouté avec succès.", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Quantité doit être un nombre valide.", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Erreur", "Problème lors de l'ajout à la base de données.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleEdit() {
        Equipement selectedEquipement = equipementTable.getSelectionModel().getSelectedItem();

        if (selectedEquipement == null) {
            showAlert("Erreur", "Veuillez sélectionner un équipement à modifier.", Alert.AlertType.ERROR);
            return;
        }

        String nom = (nomField.getText() != null) ? nomField.getText().trim() : "";
        String description = (descriptionField.getText() != null) ? descriptionField.getText().trim() : "";
        String imageUrl = (imageField.getText() != null) ? imageField.getText().trim() : "";
        String quantiteText = (quantiteField.getText() != null) ? quantiteField.getText().trim() : "";

        EtatEquipement etat = etatComboBox.getValue();
        TypeEquipement type = typeComboBox.getValue();

        if (nom.isEmpty() || description.isEmpty() || etat == null || type == null || quantiteText.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs requis.", Alert.AlertType.ERROR);
            return;
        }

        try {
            int quantite = Integer.parseInt(quantiteText);

            System.out.println("Modification de l'équipement : " + selectedEquipement);
            System.out.println("Nom: " + nom);
            System.out.println("Description: " + description);
            System.out.println("État: " + etat);
            System.out.println("Type: " + type);
            System.out.println("Image: " + imageUrl);
            System.out.println("Quantité: " + quantite);

            selectedEquipement.setNom(nom);
            selectedEquipement.setDescription(description);
            selectedEquipement.setEtat(etat);
            selectedEquipement.setTypeEquipement(type);
            selectedEquipement.setImage_url(imageUrl);
            selectedEquipement.setQuantite(quantite);

            serviceEquipement.modifierEquipement(selectedEquipement);
            equipementTable.refresh();
            showAlert("Succès", "Modification réussie.", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Quantité doit être un nombre valide.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDelete() {
        Equipement selected = equipementTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner un équipement à supprimer.", Alert.AlertType.ERROR);
            return;
        }

        try {
            serviceEquipement.supprimerEquipement(selected.getId());
            equipementList.remove(selected);
            clearFields();
            showAlert("Succès", "Équipement supprimé avec succès.", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Erreur", "Problème lors de la suppression de l'équipement.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clearFields() {
        nomField.clear();
        descriptionField.clear();
        etatComboBox.setValue(null);
        typeComboBox.setValue(null);
        imageField.clear();
        quantiteField.clear();
    }

    private void fillForm(Equipement equipement) {
        nomField.setText(equipement.getNom());
        descriptionField.setText(equipement.getDescription());
        etatComboBox.setValue(equipement.getEtat());
        typeComboBox.setValue(equipement.getTypeEquipement());
        imageField.setText(equipement.getImage_url());
        quantiteField.setText(String.valueOf(equipement.getQuantite()));
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
