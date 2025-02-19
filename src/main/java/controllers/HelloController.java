package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import utils.SceneLoader;

import java.io.IOException;

public class HelloController {

    @FXML
    private VBox cardGestionUtilisateur;

    @FXML
    private VBox cardGestionEquipe;

    @FXML
    private VBox cardGestionInstEq;

    @FXML
    private VBox cardGestionTournoisMatch;

    @FXML
    public void onClickCardGestionUtilisateur(MouseEvent event) {
        String fxml = "/gestion-utilisateur-view.fxml";
        new SceneLoader(fxml, cardGestionUtilisateur);
    }

    @FXML
    public void onClickCardGestionEquipe(MouseEvent event) {
        String fxml = "/gestion-equipe-view.fxml";
        new SceneLoader(fxml, cardGestionEquipe);
    }
    @FXML
    public void onClickCardGestionInstEq(MouseEvent event) {
        String fxml = "/gestion-inst-eq-view.fxml";
        new SceneLoader(fxml, cardGestionInstEq);
    }

    @FXML
    public void onClickCardGestionTournoisMatch(MouseEvent event) {
        String fxml = "/gestion-tournois-match-view.fxml";
        new SceneLoader(fxml, cardGestionTournoisMatch);
    }
}