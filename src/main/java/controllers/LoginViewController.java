package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import utils.Auth;
import utils.SceneLoader;

public class LoginViewController {
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label appMessage;

    @FXML
    public void onLoginButtonClick(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();
        if (email.isEmpty() || password.isEmpty()) {
            appMessage.setText("Veuillez remplir tous les champs");
        }else{
            boolean auth = new Auth().login(email, password);
            if (auth) {
                String fxml = "/hello-view.fxml";
                new SceneLoader(fxml, emailField);
            }else{
                appMessage.setText("identifiant ou mot de passe incorrect");
            }
        }
    }

}
