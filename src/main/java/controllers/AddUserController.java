package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.module1.User;
import services.module1.UserService;
import tools.DbConnection;
import enums.Role;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;

public class AddUserController {

    @FXML private TextField prenomField, nomField, emailField, telField, adresseField;
    @FXML private PasswordField passwordField, repasswordField;
    @FXML private DatePicker birthdayPicker;
    @FXML private Label errorLabel;
    @FXML private ComboBox<Role> roleComboBox;

    private final Connection conn = DbConnection.getInstance().getConn();
    private final UserService userService = new UserService(conn);
    private User selectedUser = null;


    @FXML
    public void initialize() throws IOException {
        ObservableList<Role> roles = FXCollections.observableArrayList(Role.values());
        roleComboBox.setItems(roles);

    }

    public void setUserData(User user) {
        if (user != null) {
            this.selectedUser = user;
            prenomField.setText(user.getPrenom());
            nomField.setText(user.getNom());
            emailField.setText(user.getEmail());
            telField.setText(user.getTel());
            adresseField.setText(user.getAdresse());
            birthdayPicker.setValue(LocalDate.parse(user.getBirthday()));
            roleComboBox.setValue(user.getRole());
        }
    }

    @FXML
    private void saveUser(ActionEvent actionEvent) {
        if (!validateFields()) return;

        // For new users, use the raw password from the PasswordField.
        String rawPassword = "";
        String passwordToStore = "";
        if (selectedUser == null) {
            rawPassword = passwordField.getText(); // Get raw password from the UI
            // Leave passwordToStore empty; it will be overwritten by userService.addUser()
        } else {
            if (!passwordField.getText().isEmpty()) {
                rawPassword = passwordField.getText();
                // If a new password is provided during update, we update it.
            } else {
                // Otherwise, keep the existing hash.
                passwordToStore = selectedUser.getMdpHash();
            }
        }

        // Construct the new User. Note: we're passing the passwordToStore (empty if new),
        // and then setting the raw password separately.
        User newUser = selectedUser == null ?
                new User(0, prenomField.getText(), nomField.getText(), roleComboBox.getValue(),
                        birthdayPicker.getValue().toString(), telField.getText(), adresseField.getText(),
                        "status", "imageUrl", emailField.getText(), passwordToStore) :
                new User(selectedUser.getId(), prenomField.getText(), nomField.getText(), roleComboBox.getValue(),
                        birthdayPicker.getValue().toString(), telField.getText(), adresseField.getText(),
                        "status", "imageUrl", emailField.getText(), passwordToStore);

        // Set the raw password so that UserService can perform BCrypt hashing
        newUser.setRawPassword(rawPassword);

        try {
            if (selectedUser == null) {
                userService.addUser(newUser);
            } else {
                userService.updateUser(newUser);
            }
            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors de l'ajout de l'utilisateur.");
        }
    }


    private boolean validateFields() {
        String prenom = prenomField.getText();
        String nom = nomField.getText();
        String email = emailField.getText();
        String tel = telField.getText();
        LocalDate birthday = birthdayPicker.getValue();
        Role role = roleComboBox.getValue();
        String password = passwordField.getText();
        String confirmPassword = repasswordField.getText();

        if (prenom.isEmpty() || nom.isEmpty() || email.isEmpty() || tel.isEmpty() || role == null) {
            errorLabel.setText("Tous les champs sont obligatoires !");
            return false;
        }

        if (selectedUser == null && (password.isEmpty() || !password.equals(confirmPassword))) {
            errorLabel.setText("Les mots de passe ne correspondent pas !");
            return false;
        }

        return true;
    }

    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(Integer.toHexString(0xff & b));
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return password;
        }
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) prenomField.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}