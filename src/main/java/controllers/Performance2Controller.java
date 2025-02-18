package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.module1.Equipe;
import models.module5.PerformanceEquipe;
import models.module5.Tournois;
import services.module1.ServiceEquipe;
import services.module5.ServicePerformanceEquipe;
import services.module5.ServiceTournois;

import java.sql.SQLException;

public class Performance2Controller {

    @FXML
    private TextField idField;
    @FXML
    private TextField equipeField;
    @FXML
    private TextField tournoisField;
    @FXML
    private TextField victoiresField;
    @FXML
    private TextField pertesField;
    @FXML
    private TextField rangField;

    private PerformanceEquipe performance;

    public void setPerformance(PerformanceEquipe performance) {
        this.performance = performance;
        equipeField.setText(String.valueOf(performance.getEquipe().getId()));
        tournoisField.setText(String.valueOf(performance.getTournois().getId()));
        victoiresField.setText(String.valueOf(performance.getVictoires()));
        pertesField.setText(String.valueOf(performance.getPertes()));
        rangField.setText(String.valueOf(performance.getRang()));
    }

    @FXML
    private void handleValiderButtonAction() {
        ServiceEquipe serviceEquipe = new ServiceEquipe();
        ServiceTournois serviceTournois = new ServiceTournois();

        try {
            // Parse the IDs from the text fields
            int equipeId = Integer.parseInt(equipeField.getText());
            int tournoisId = Integer.parseInt(tournoisField.getText());

            // Retrieve the Equipe and Tournois objects based on the IDs
            Equipe equipe = serviceEquipe.get(equipeId);
            Tournois tournois = serviceTournois.get(tournoisId);

            // Set the retrieved objects in the PerformanceEquipe object
            performance.setEquipe(equipe);
            performance.setTournois(tournois);
            performance.setVictoires(Integer.parseInt(victoiresField.getText()));
            performance.setPertes(Integer.parseInt(pertesField.getText()));
            performance.setRang(Integer.parseInt(rangField.getText()));

            // Update the performance
            ServicePerformanceEquipe servicePerformanceEquipe = new ServicePerformanceEquipe();
            servicePerformanceEquipe.update(performance);
            System.out.println("Performance has been updated!");
        } catch (NumberFormatException e) {
            System.err.println("Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}