package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import utils.NavigationUtils;

public class MatchController {

    @FXML private Button retourButton;
    
    @FXML
    private void initialize() {
        // ... existing initialization code ...
        
        // Ajouter l'action du bouton retour
        retourButton.setOnAction(e -> {
            NavigationUtils.navigateTo("/views/home_view.fxml", "Accueil");
            NavigationUtils.closeCurrentWindow(retourButton.getScene().getRoot());
        });
    }
} 