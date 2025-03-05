package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import models.module4.SessionManagement;

public class NavigationController {

    @FXML
    public Button navUserButton;
    @FXML
    public Button navTournoisButton;
    @FXML
    public Button navTeamsButton;
    @FXML
    public Button navMatchButton;
    @FXML
    public Button navEquipementButton;
    @FXML
    public Button navInstallationsButton;
    @FXML
    public Button navPerformanceButton;

    public void initialize() {
        switch (SessionManagement.getInstance().getCurrentUser().getRole()) {
            case ADMIN -> {
                break;
            }
            case MANAGER -> {
                navUserButton.setVisible(false);
                navTournoisButton.setVisible(false);
                navTeamsButton.setVisible(false);
                navMatchButton.setVisible(false);
                navEquipementButton.setVisible(true);
                navInstallationsButton.setVisible(true);
                navPerformanceButton.setVisible(false);
                break;
            }
            case COACH -> {
                navUserButton.setVisible(true);
                navTournoisButton.setVisible(true);
                navTeamsButton.setVisible(true);
                navMatchButton.setVisible(true);
                navEquipementButton.setVisible(false);
                navInstallationsButton.setVisible(false);
                navPerformanceButton.setVisible(true);

                break;
            }
            case ATHLETE -> {
                navUserButton.setVisible(false);
                navTournoisButton.setVisible(true);
                navTeamsButton.setVisible(false);
                navMatchButton.setVisible(true);
                navEquipementButton.setVisible(false);
                navInstallationsButton.setVisible(false);
                navPerformanceButton.setVisible(false);
                break;
            }
        }
    }

    @FXML
    private void navigateToPerformances(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Performancelist.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void navigateToUsers(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/User.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void navigateToTournois(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Tournoi1.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void navigateToTeams(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Team.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void navigateToMatches(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/MatchSportif.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void navigateToEquipement(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Equipement.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void navigateToInstallations(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/InstallationSportive.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }


}