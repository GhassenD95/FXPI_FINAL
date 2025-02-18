package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.module5.PerformanceEquipe;
import models.module1.Equipe;
import models.module5.Tournois;
import services.module5.ServicePerformanceEquipe;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class PerformanceController {

    @FXML
    private ComboBox<String> equipeComboBox;

    @FXML
    public void initialize() {
        List<Equipe> equipeList = servicePerformanceEquipe.getAllEquipes();
        if (equipeList != null && !equipeList.isEmpty()) {
            List<String> equipeNames = equipeList.stream()
                    .map(Equipe::getNom)
                    .collect(Collectors.toList());
            equipeComboBox.getItems().setAll(equipeNames);
        }
    }

    @FXML
    private TextField victoiresField;

    @FXML
    private TextField pertesField;

    @FXML
    private TextField rangField;

    @FXML
    private ComboBox<String> tournoisComboBox;

    @FXML
    private Button validerButton;

    private final ServicePerformanceEquipe servicePerformanceEquipe = new ServicePerformanceEquipe();

    private void initializeTournois() {
        List<Tournois> tournoisList = servicePerformanceEquipe.getAllTournois();
        if (tournoisList != null && !tournoisList.isEmpty()) {
            List<String> tournoisNames = tournoisList.stream()
                    .map(Tournois::getNom)
                    .collect(Collectors.toList());
            tournoisComboBox.getItems().setAll(tournoisNames);
        }
    }

    @FXML
    private void handleValiderButtonAction() {
        try {
            String equipeName = equipeComboBox.getValue();
            int victoires = Integer.parseInt(victoiresField.getText().trim());
            int pertes = Integer.parseInt(pertesField.getText().trim());
            int rang = Integer.parseInt(rangField.getText().trim());
            String tournoisName = tournoisComboBox.getValue();

            if (equipeName.isEmpty() || tournoisName == null) {
                System.err.println("Please fill in all required fields.");
                return;
            }

            // Assuming Equipe constructor takes (nom, other params as needed)
            Equipe equipe = new Equipe(equipeName, null, null, null);
            Tournois tournois = new Tournois(tournoisName, null, null, null, null); // Adjust constructor as needed
            PerformanceEquipe performanceEquipe = new PerformanceEquipe(equipe, victoires, pertes, rang, tournois);

            servicePerformanceEquipe.add(performanceEquipe);
            System.out.println("PerformanceEquipe has been added!");

        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Database error: " + e.getMessage());
        }
    }
}