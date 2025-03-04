package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.module6.InstallationSportive;
import enums.TypeInstallation;
import services.module6.ServiceInstallationSportive;

import java.sql.SQLException;

public class InstallationSportiveController {

    @FXML
    private TextField nomField, adresseField, capaciteField, imageField;
    @FXML
    private ComboBox<TypeInstallation> typeComboBox;
    @FXML
    private CheckBox disponibleCheckBox;
    @FXML
    private TableView<InstallationSportive> installationTable;
    @FXML
    private TableColumn<InstallationSportive, String> nomColumn;
    @FXML
    private TableColumn<InstallationSportive, TypeInstallation> typeColumn;
    @FXML
    private TableColumn<InstallationSportive, String> adresseColumn;
    @FXML
    private TableColumn<InstallationSportive, Integer> capaciteColumn;
    @FXML
    private TableColumn<InstallationSportive, Boolean> disponibleColumn;

    private ServiceInstallationSportive serviceInstallationSportive;
    private ObservableList<InstallationSportive> installationList;

    @FXML
    public void initialize() {
        serviceInstallationSportive = new ServiceInstallationSportive();
        installationList = FXCollections.observableArrayList(serviceInstallationSportive.getAll());

        nomColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNom()));
        typeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTypeInstallation()));
        adresseColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAdresse()));
        capaciteColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCapacite()).asObject());
        disponibleColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().isDisponible()).asObject());

        installationTable.setItems(installationList);
        typeComboBox.setItems(FXCollections.observableArrayList(TypeInstallation.values()));

        installationTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillForm(newValue);
            }
        });

    }

    @FXML
    private void handleAdd() {
        try {
            String nom = nomField.getText().trim();
            String adresse = adresseField.getText().trim();
            String capaciteText = capaciteField.getText().trim();
            TypeInstallation type = typeComboBox.getValue();
            boolean disponible = disponibleCheckBox.isSelected();
            String imageUrl = imageField.getText().trim();

            if (nom.isEmpty() || adresse.isEmpty() || capaciteText.isEmpty() || type == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs requis.", Alert.AlertType.ERROR);
                return;
            }

            int capacite = Integer.parseInt(capaciteText);
            InstallationSportive newInstallation = new InstallationSportive(nom, type, adresse, capacite, disponible, imageUrl);
            serviceInstallationSportive.ajouterInstallation(newInstallation);
            installationList.add(newInstallation);
            clearFields();
            showAlert("Succès", "Installation ajoutée avec succès.", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Capacité doit être un nombre valide.", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Erreur", "Problème lors de l'ajout à la base de données.\n" + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEdit() {
        InstallationSportive selectedInstallation = installationTable.getSelectionModel().getSelectedItem();
        if (selectedInstallation == null) {
            showAlert("Erreur", "Veuillez sélectionner une installation à modifier.", Alert.AlertType.ERROR);
            return;
        }

        try {
            String nom = nomField.getText().trim();
            String adresse = adresseField.getText().trim();
            String capaciteText = capaciteField.getText().trim();
            TypeInstallation type = typeComboBox.getValue();
            boolean disponible = disponibleCheckBox.isSelected();
            String imageUrl = imageField.getText().trim();

            if (nom.isEmpty() || adresse.isEmpty() || capaciteText.isEmpty() || type == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs requis.", Alert.AlertType.ERROR);
                return;
            }

            int capacite = Integer.parseInt(capaciteText);

            selectedInstallation.setNom(nom);
            selectedInstallation.setAdresse(adresse);
            selectedInstallation.setCapacite(capacite);
            selectedInstallation.setDisponible(disponible);
            selectedInstallation.setImage_url(imageUrl);
            selectedInstallation.setTypeInstallation(type);

            serviceInstallationSportive.modifierInstallation(selectedInstallation);
            installationTable.refresh();
            showAlert("Succès", "Modification réussie.", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "La capacité doit être un nombre valide.", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Erreur", "Problème lors de la mise à jour de la base de données.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDelete() throws SQLException {
        InstallationSportive selected = installationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner une installation à supprimer.", Alert.AlertType.ERROR);
            return;
        }

        serviceInstallationSportive.supprimer(selected.getId());
        installationList.remove(selected);
        clearFields();
        showAlert("Succès", "Installation supprimée avec succès.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void clearFields() {
        nomField.clear();
        adresseField.clear();
        capaciteField.clear();
        typeComboBox.setValue(null);
        disponibleCheckBox.setSelected(false);
        imageField.clear();
    }

    private void fillForm(InstallationSportive installation) {
        System.out.println("Filling form with: " + installation.getNom()); // Debugging line
        nomField.setText(installation.getNom());
        adresseField.setText(installation.getAdresse());
        capaciteField.setText(String.valueOf(installation.getCapacite()));
        typeComboBox.setValue(installation.getTypeInstallation());
        disponibleCheckBox.setSelected(installation.isDisponible());
        imageField.setText(installation.getImage_url());
    }


    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
