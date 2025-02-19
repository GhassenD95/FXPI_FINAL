package controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.module6.InstallationSportive;
import enums.TypeInstallation;
import javafx.scene.control.Button;

public class InstallationSportiveController {

    @FXML
    private TextField nomField;
    @FXML
    private ComboBox<TypeInstallation> typeComboBox;
    @FXML
    private TextField adresseField;
    @FXML
    private TextField capaciteField;
    @FXML
    private ComboBox<Boolean> disponibleComboBox;
    @FXML
    private TableView<InstallationSportive> installationSportiveTable;
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

    @FXML
    public void initialize() {
        // Initialisation des valeurs de type d'installation et de disponibilité
        typeComboBox.getItems().setAll(TypeInstallation.values());
        disponibleComboBox.getItems().setAll(true, false);

        // Initialisation des colonnes de la TableView avec des propriétés observables
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        typeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTypeInstallation()));
        adresseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse()));
        capaciteColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCapacite()).asObject());
        disponibleColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isDisponible()).asObject());
    }

    @FXML
    public void handleAdd() {
        String nom = nomField.getText();
        TypeInstallation type = typeComboBox.getValue();
        String adresse = adresseField.getText();
        int capacite = Integer.parseInt(capaciteField.getText());
        Boolean disponible = disponibleComboBox.getValue();

        InstallationSportive newInstallation = new InstallationSportive(nom, type, null, adresse, capacite, disponible, null); // manager peut être null pour l'instant
        installationSportiveTable.getItems().add(newInstallation);

        clearForm();
    }

    // Méthode pour modifier une installation sportive sélectionnée
    @FXML
    public void handleEdit() {
        InstallationSportive selectedInstallation = installationSportiveTable.getSelectionModel().getSelectedItem();
        if (selectedInstallation != null) {
            selectedInstallation.setNom(nomField.getText());
            selectedInstallation.setTypeInstallation(typeComboBox.getValue());
            selectedInstallation.setAdresse(adresseField.getText());
            selectedInstallation.setCapacite(Integer.parseInt(capaciteField.getText()));
            selectedInstallation.setDisponible(disponibleComboBox.getValue());

            installationSportiveTable.refresh();
            clearForm();
        }
    }

    // Méthode pour supprimer une installation sportive
    @FXML
    public void handleDelete() {
        InstallationSportive selectedInstallation = installationSportiveTable.getSelectionModel().getSelectedItem();
        if (selectedInstallation != null) {
            installationSportiveTable.getItems().remove(selectedInstallation);
        }
    }

    // Méthode pour vider les champs du formulaire
    private void clearForm() {
        nomField.clear();
        typeComboBox.getSelectionModel().clearSelection();
        adresseField.clear();
        capaciteField.clear();
        disponibleComboBox.getSelectionModel().clearSelection();
    }
}
