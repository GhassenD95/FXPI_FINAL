package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class SceneLoader {
    public SceneLoader(String fxml, javafx.scene.Node currentNode) {
        FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource(fxml));
        try {
            Parent root = loader.load();
            currentNode.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
/*
*
*  FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestion-utilisateur-view.fxml"));
        try {
            Parent root = loader.load();
            cardGestionUtilisateur.getScene().setRoot(root);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
*
*
*
*
* */