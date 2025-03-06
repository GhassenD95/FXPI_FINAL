package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import utils.NavigationUtils;

public class HomeController {
    @FXML private VBox mainContainer;
    
    @FXML
    private void initialize() {
        // Créer les boutons de navigation
        Button matchsButton = new Button("Gestion des Matchs");
        Button performancesButton = new Button("Gestion des Performances");
        Button statistiquesButton = new Button("Statistiques");
        
        // Ajouter les actions aux boutons avec les bons chemins
        matchsButton.setOnAction(e -> NavigationUtils.navigateTo("/MatchSportif.fxml", "Gestion des Matchs"));
        performancesButton.setOnAction(e -> NavigationUtils.navigateTo("/performance_view.fxml", "Gestion des Performances"));
        statistiquesButton.setOnAction(e -> NavigationUtils.navigateTo("/statistiques_view.fxml", "Statistiques"));
        
        // Ajouter les boutons au conteneur
        mainContainer.getChildren().addAll(matchsButton, performancesButton, statistiquesButton);
        
        // Ajouter du style aux boutons
        String buttonStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px; -fx-font-size: 14px;";
        matchsButton.setStyle(buttonStyle);
        performancesButton.setStyle(buttonStyle);
        statistiquesButton.setStyle(buttonStyle);
    }
} 