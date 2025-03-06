package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class NavigationUtils {
    
    public static void navigateTo(String fxmlPath, String title) {
        try {
            System.out.println("Tentative de chargement du fichier : " + fxmlPath);
            FXMLLoader loader = new FXMLLoader(NavigationUtils.class.getResource(fxmlPath));
            if (loader.getLocation() == null) {
                System.err.println("Le fichier FXML n'a pas été trouvé : " + fxmlPath);
                return;
            }
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du fichier FXML : " + fxmlPath);
            e.printStackTrace();
        }
    }
    
    public static void closeCurrentWindow(Parent root) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }
} 