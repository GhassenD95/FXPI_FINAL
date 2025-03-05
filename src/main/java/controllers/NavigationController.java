package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import models.module4.SessionManagement;
import services.FootballDataService;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class NavigationController {
    private FootballDataService footballDataService = new FootballDataService();


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
    @FXML
    public Button navperfoButton;

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
                navperfoButton.setVisible(false);

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
                navperfoButton.setVisible(true);

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
                navperfoButton.setVisible(false);
                break;
            }
        }
        fetchFootballNotifications();

    }

    private void fetchFootballNotifications() {
        try {
            JsonNode notification = footballDataService.getMatchesForToday();
            if (notification != null) {
                String homeTeam = notification.get("homeTeam").get("name").asText();
                String awayTeam = notification.get("awayTeam").get("name").asText();
                String matchDate = notification.get("utcDate").asText();
                String matchStatus = notification.get("status").asText();

                String matchInfo = String.format("%s vs %s\nDate: %s\nStatus: %s", homeTeam, awayTeam, matchDate, matchStatus);
                String matchUrl = "https://www.google.com/search?q=" + (homeTeam + " vs " + awayTeam).replace(" ", "+");

                displayDesktopNotification("Latest Football Match", matchInfo, matchUrl);
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void displayDesktopNotification(String title, String message, String url) throws AWTException {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            TrayIcon trayIcon = new TrayIcon(image, "Football Notification");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("Football Notification");
            tray.add(trayIcon);

            ActionListener listener = e -> {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            };
            trayIcon.addActionListener(listener);

            trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
        } else {
            System.err.println("System tray not supported!");
        }
    }
    @FXML
    private void navigateToPerformances(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Performance_view.fxml"));
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


    @FXML
    public void navigateToperfo(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Performancelist.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void HandleGoToCalendrier(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Calendar.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}