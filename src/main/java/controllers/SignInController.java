package controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.module1.User;
import models.module4.SessionManagement;
import services.module1.UserService;
import mains.fxpi_final.Main;
import tools.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class SignInController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Hyperlink signUpLink;

    private Connection conn;
    private UserService userService;
    private Main mainApp;  // Add reference to Main app

    public SignInController() {
        conn = DbConnection.getInstance().getConn();
        userService = new UserService(conn);
    }

    // Set the Main application (Main instance)
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Called when user clicks 'Sign In' button
     */
    @FXML
    private void handleSignIn() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Email and password cannot be empty.");
            return;
        }

        try {
            User user = userService.signIn(email, password);
            if (user != null) {
                // Sign in successful
                errorLabel.setText("");
                SessionManagement.getInstance().setCurrentUser(user);

                mainApp.handleSuccessfulSignIn();  // Show the Landing Page
            } else {
                // Invalid credentials
                errorLabel.setText("Invalid email or password!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Database error: " + e.getMessage());
        }
    }

    /**
     * Optionally open a SignUp form or show a pop-up
     */
    @FXML
    private void handleSignUpLink() {
        mainApp.loadSignUpScene();  // Load the Sign Up page
    }
}
