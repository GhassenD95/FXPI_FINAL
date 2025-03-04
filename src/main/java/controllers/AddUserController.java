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
    private VBox sidebarContainer;

    @FXML
    public void initialize() throws IOException {
        ObservableList<Role> roles = FXCollections.observableArrayList(Role.values());
        roleComboBox.setItems(roles);
        VBox sidebar = FXMLLoader.load(getClass().getResource("/navigation.fxml"));
        sidebarContainer.getChildren().setAll(sidebar);
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

        String hashedPassword = selectedUser == null ? hashPassword(passwordField.getText()) : selectedUser.getMdpHash();
        String status = "active"; // Ensure this value is within the allowed size


        User newUser = selectedUser == null ?
                new User(0, prenomField.getText(), nomField.getText(), roleComboBox.getValue(),
                        birthdayPicker.getValue().toString(), telField.getText(), adresseField.getText(),
                        "status", "imageUrl", emailField.getText(), hashedPassword) :
                new User(selectedUser.getId(), prenomField.getText(), nomField.getText(), roleComboBox.getValue(),
                        birthdayPicker.getValue().toString(), telField.getText(), adresseField.getText(),
                        "status", "imageUrl", emailField.getText(), hashedPassword);
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