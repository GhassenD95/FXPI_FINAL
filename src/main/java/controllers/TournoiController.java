package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.module4.Tournois;
import enums.Sport;
import services.module4.ServiceTournois;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class TournoiController {

    @FXML
    private TextField nomField;

    @FXML
    private ComboBox<Sport> disciplineComboBox;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private ComboBox<String> addressComboBox;

    @FXML
    private Button validerButton;

    private ObservableList<String> addressSuggestions = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        disciplineComboBox.getItems().setAll(Sport.values());
        addressComboBox.setEditable(true);
        addressComboBox.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() > 2) {
                fetchAddressSuggestions(newText);
            }

        });

        addressComboBox.setOnAction(event -> {
            String selectedAddress = addressComboBox.getSelectionModel().getSelectedItem();
            if (selectedAddress != null) {
                addressComboBox.getEditor().setText(selectedAddress);
            }
        })
        ;
    }

    @FXML
    private void handleValiderButtonAction() {
        String nom = nomField.getText();
        Sport sport = disciplineComboBox.getValue();
        Date dateDebut = java.sql.Date.valueOf(dateDebutPicker.getValue());
        Date dateFin = java.sql.Date.valueOf(dateFinPicker.getValue());
        String address = addressComboBox.getValue();

        if (!validateInputs(nom, address, sport, dateDebut, dateFin)) {
            return;
        }

        Tournois tournois = new Tournois(nom, sport, dateDebut, dateFin, address);
        ServiceTournois serviceTournois = new ServiceTournois();

        try {
            serviceTournois.add(tournois);
            showDialog("Ajout réussi", "Le tournoi a été ajouté avec succès!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showDialog("Erreur", "Une erreur est survenue lors de l'ajout du tournoi.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void fetchAddressSuggestions(String query) {
        new Thread(() -> {
            int retries = 3;
            int delay = 1000; // initial delay in milliseconds

            for (int attempt = 1; attempt <= retries; attempt++) {
                try {
                    String urlString = "https://nominatim.openstreetmap.org/search?q=" +
                            URLEncoder.encode(query, "UTF-8") + "&format=json&addressdetails=1&limit=5";
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0");

                    if (conn.getResponseCode() == 429) {
                        throw new IOException("Too many requests");
                    }

                    Scanner scanner = new Scanner(conn.getInputStream(), "UTF-8");
                    StringBuilder jsonResponse = new StringBuilder();
                    while (scanner.hasNext()) {
                        jsonResponse.append(scanner.nextLine());
                    }
                    scanner.close();

                    JSONArray results = new JSONArray(jsonResponse.toString());
                    ObservableList<String> newSuggestions = FXCollections.observableArrayList();

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);
                        String displayName = result.getString("display_name");
                        newSuggestions.add(displayName);
                    }

                    Platform.runLater(() -> {
                        String currentText = addressComboBox.getEditor().getText(); // Store current text
                        addressSuggestions.setAll(newSuggestions);
                        addressComboBox.setItems(addressSuggestions);

                        // Restore previous text
                        addressComboBox.getEditor().setText(currentText);
                        addressComboBox.getEditor().positionCaret(currentText.length()); // Keep cursor position
                    });

                    break; // exit the loop if successful

                } catch (IOException e) {
                    if (attempt == retries) {
                        e.printStackTrace();
                        Platform.runLater(() -> showDialog("Error", "Failed to fetch address suggestions. Please try again.", Alert.AlertType.ERROR));
                    } else {
                        try {
                            Thread.sleep(delay);
                            delay *= 2; // exponential backoff
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> showDialog("Error", "An unexpected error occurred. Please try again.", Alert.AlertType.ERROR));
                    break;
                }
            }
        }).start();
    }
    private boolean validateInputs(String nom, String adresse, Sport sport, Date dateDebut, Date dateFin) {
        if (nom == null || nom.trim().isEmpty()) {
            showDialog("Erreur", "Veuillez saisir un nom pour le tournoi.", Alert.AlertType.ERROR);
            return false;
        }

        if (dateDebut == null || dateFin == null || dateDebut.after(dateFin) || dateDebut.before(new Date())) {
            showDialog("Erreur", "La date de début doit être postérieure à la date d'aujourd'hui et antérieure à la date de fin.", Alert.AlertType.ERROR);
            return false;
        }

        if (adresse == null || adresse.trim().isEmpty()) {
            showDialog("Erreur", "L'adresse du tournoi est requise.", Alert.AlertType.ERROR);
            return false;
        }

        if (sport == null) {
            showDialog("Erreur", "Veuillez sélectionner un sport.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void showDialog(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}