package controllers;

import enums.Role;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.module1.User;
import org.mindrot.jbcrypt.BCrypt;
import services.module1.UserService;
import mains.fxpi_final.Main;
import tools.DbConnection;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class SignUpController {

    @FXML private TextField prenomField;
    @FXML private TextField nomField;
    @FXML private DatePicker birthdayPicker;
    @FXML private TextField telField;
    @FXML private TextField adresseField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField repasswordField;

    @FXML private ImageView imageView;
    @FXML private Label imagePathLabel;
    @FXML private Label errorLabel;

    private Connection conn;
    private UserService userService;
    private String imagePath = ""; // path to the chosen image (optional)

    public SignUpController() {
        // Use your DB singleton
        conn = DbConnection.getInstance().getConn();
        userService = new UserService(conn);
    }

    /**
     * Allows user to choose an image from the file system
     */
    @FXML
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imagePath = selectedFile.toURI().toString();
            imageView.setImage(new Image(imagePath));
            imagePathLabel.setText(selectedFile.getName());
        }
    }


    /**
     * Main method triggered when user clicks "Sign Up"
     */
    @FXML
    private void handleSignUp() {
        // Grab form values
        String prenom = prenomField.getText().trim();
        String nom = nomField.getText().trim();
        LocalDate birthday = birthdayPicker.getValue();
        String tel = telField.getText().trim();
        String adresse = adresseField.getText().trim();
        String email = emailField.getText().trim();
        String pass = passwordField.getText().trim();
        String confirm = repasswordField.getText().trim();

        // 1) Validate name fields (no digits)
        if (!validateNomPrenom(prenom, nom)) return;

        // 2) Validate age > 6
        if (!validateAge(birthday)) return;

        // 3) Validate phone = 8 digits
        if (!tel.matches("\\d{8}")) {
            errorLabel.setText("Téléphone doit contenir 8 chiffres !");
            return;
        }

        // 4) Check mandatory fields
        if (email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            errorLabel.setText("Veuillez remplir au moins email et mot de passe !");
            return;
        }

        // 5) Check password confirmation
        if (!pass.equals(confirm)) {
            errorLabel.setText("Les mots de passe ne correspondent pas !");
            return;
        }

        // 6) Check minimal email format
        if (!email.contains("@")) {
            errorLabel.setText("Email invalide (manque '@').");
            return;
        }

        // 7) Check if email is already in DB
        try {
            if (userService.checkEmailExists(email)) {
                errorLabel.setText("Cet email est déjà utilisé !");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Erreur BD (email) : " + e.getMessage());
            return;
        }

        // If all validations pass, create user in DB
        createUser(prenom, nom, birthday, tel, adresse, email, pass);
    }

    /**
     * Actually inserts the user in the DB, forcing role= 'adherant' and status= 'active'
     * and hashing the password with BCrypt.
     */
    private void createUser(String prenom, String nom, LocalDate birthday,
                            String tel, String adresse,
                            String email, String rawPassword) {
        try {
            // Convert birthday to string if not null
            String birthdayStr = (birthday != null) ? birthday.toString() : "";

            // 1) Hash the password with BCrypt
            String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));
            Role role = Role.ATHLETE; // Use the actual enum value


            // 2) Force role = 'adherant' & status = 'active'
            User newUser = new User(
                    0,
                    prenom,
                    nom,
                    role,// role
                    birthdayStr,
                    tel,
                    adresse,
                    "active",     // status
                    imagePath,
                    email,
                    hashedPassword
            );

            // Insert in DB
            userService.addUser(newUser);
            errorLabel.setText("Compte créé avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Erreur BD : " + e.getMessage());
        }
    }

    /**
     * Validate that prenom & nom don't contain digits
     */
    private boolean validateNomPrenom(String prenom, String nom) {
        // Regex that only allows letters (with accents if needed)
        String pattern = "^[A-Za-zÀ-ÖØ-öø-ÿ]+$";
        if (!prenom.matches(pattern)) {
            errorLabel.setText("Le prénom ne doit pas contenir de chiffres !");
            return false;
        }
        if (!nom.matches(pattern)) {
            errorLabel.setText("Le nom ne doit pas contenir de chiffres !");
            return false;
        }
        return true;
    }

    /**
     * Validate that birthday is older than 6 years from today
     */
    private boolean validateAge(LocalDate birthday) {
        if (birthday == null) {
            errorLabel.setText("Veuillez sélectionner une date de naissance !");
            return false;
        }
        int age = Period.between(birthday, LocalDate.now()).getYears();
        if (age < 6) {
            errorLabel.setText("L'utilisateur doit avoir plus de 6 ans !");
            return false;
        }
        return true;
    }

    /**
     * Return to sign in screen
     */
    @FXML
    private void handleBackToSignIn() {
        try {
            Stage currentStage = (Stage) errorLabel.getScene().getWindow();
            Main mainApp = (Main) currentStage.getUserData();
            if (mainApp != null) {
                mainApp.loadSignInScene();
            } else {
                errorLabel.setText("MainApp introuvable !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erreur retour Sign In : " + e.getMessage());
        }
    }
}
