package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.module1.User;
import services.module1.UserService;
import tools.DbConnection;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

public class UserController {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colPrenom, colNom, colEmail, colTel, colAdresse;

    @FXML private TextField prenomField, nomField, emailField, telField, adresseField;
    @FXML private PasswordField passwordField, repasswordField;
    @FXML private DatePicker birthdayPicker;
    @FXML private Label errorLabel;

    private Connection conn;
    private UserService userService;
    private ObservableList<User> userList = FXCollections.observableArrayList();
    private User selectedUser = null; // Stocke l'utilisateur en modification

    public UserController() {
        this.conn = DbConnection.getInstance().getConn();
        this.userService = new UserService(this.conn);
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        addActionButtonsToTable();
        loadUsers();
    }

    private void loadUsers() {
        try {
            List<User> users = userService.getUsers();
            userList.setAll(users);
            userTable.setItems(userList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ouvre la page pour ajouter un nouvel utilisateur
     */
    @FXML
    private void addUser() {
        selectedUser = null; // Aucune modification en cours
        openUserForm("Ajouter un Utilisateur");
    }

    /**
     * Ouvre la page pour modifier un utilisateur existant
     */
    @FXML
    private void editUser(User user) {
        selectedUser = user; // Utilise l'utilisateur passé en paramètre
        if (selectedUser == null) {
            errorLabel.setText("Veuillez sélectionner un utilisateur !");
            return;
        }
        openUserForm("Modifier un Utilisateur");
    }

    private void openUserForm(String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Adduser.fxml"));
            Parent root = loader.load();
            AddUserController addUserController = loader.getController();
            addUserController.setUserData(selectedUser); // Passe l'utilisateur sélectionné
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> loadUsers()); // Rafraîchit la table à la fermeture
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remplit le formulaire si un utilisateur est en cours de modification
     */
    public void setUserData(User user) {
        if (user != null) {
            prenomField.setText(user.getPrenom());
            nomField.setText(user.getNom());
            emailField.setText(user.getEmail());
            telField.setText(user.getTel());
            adresseField.setText(user.getAdresse());
            birthdayPicker.setValue(LocalDate.parse(user.getBirthday()));
        }
    }

    /**
     * Sauvegarde l'utilisateur (ajout ou modification)
     */
    @FXML
    private void saveUser(ActionEvent actionEvent) {
        if (validateFields()) {
            String rawPassword = "";
            String passwordToStore = "";
            if (selectedUser == null) {
                // For a new user, use the raw password (and let the service hash it)
                rawPassword = passwordField.getText();
                // Set an empty string or placeholder for the mdpHash field; it will be overwritten by addUser()
                passwordToStore = "";
            } else {
                // For updating, if the password field is not empty, update the password
                if (!passwordField.getText().isEmpty()) {
                    rawPassword = passwordField.getText();
                    passwordToStore = "";
                } else {
                    // Otherwise, keep the existing hash
                    passwordToStore = selectedUser.getMdpHash();
                }
            }

            User newUser = new User(
                    (selectedUser != null) ? selectedUser.getId() : 0,
                    prenomField.getText(),
                    nomField.getText(),
                    birthdayPicker.getValue().toString(),
                    telField.getText(),
                    adresseField.getText(),
                    "adherent",
                    emailField.getText(),
                    passwordToStore  // Pass the hash if updating without a new password
            );
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
                showError("Erreur lors de l'ajout/modification de l'utilisateur.");
            }
        }
    }

    private void showError(String s) {
    }


    private void addActionButtonsToTable() {
        TableColumn<User, Void> actionColumn = new TableColumn<>("Actions");

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox container = new HBox(10, editButton, deleteButton);

            {
                editButton.setStyle("-fx-background-color: #ac07ff; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #ff0019; -fx-text-fill: white;");

                editButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    editUser(user);
                });

                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    deleteUser(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });

        userTable.getColumns().add(actionColumn);
    }


    /**
     * Supprime un utilisateur sélectionné
     */

    @FXML
    private void deleteUser(User user) {
        if (user != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                try {
                    userService.deleteUser(user.getId());
                    loadUsers();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    @FXML
    private void closeWindow() {
        Stage stage = (Stage) prenomField.getScene().getWindow();
        stage.close();
    }

    /**
     * Vérifie la validité des champs avant d'ajouter/modifier un utilisateur
     */
    private boolean validateFields() {
        String prenom = prenomField.getText();
        String nom = nomField.getText();
        String email = emailField.getText();
        String tel = telField.getText();
        LocalDate birthday = birthdayPicker.getValue();
        String password = passwordField.getText();
        String confirmPassword = repasswordField.getText();

        if (!Pattern.matches("^[a-zA-Z]+$", prenom)) {
            errorLabel.setText("Le prénom ne doit contenir que des lettres !");
            return false;
        }

        if (!Pattern.matches("^[a-zA-Z]+$", nom)) {
            errorLabel.setText("Le nom ne doit contenir que des lettres !");
            return false;
        }

        if (birthday == null || Period.between(birthday, LocalDate.now()).getYears() < 6) {
            errorLabel.setText("L'utilisateur doit avoir plus de 6 ans !");
            return false;
        }

        if (!Pattern.matches("^\\d{8}$", tel)) {
            errorLabel.setText("Le téléphone doit contenir exactement 8 chiffres !");
            return false;
        }

        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", email)) {
            errorLabel.setText("Email invalide !");
            return false;
        }

        if (selectedUser == null && password.length() < 6) {
            errorLabel.setText("Le mot de passe doit contenir au moins 6 caractères !");
            return false;
        }

        if (selectedUser == null && !password.equals(confirmPassword)) {
            errorLabel.setText("Les mots de passe ne correspondent pas !");
            return false;
        }

        return true;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password;
        }
    }

    public void backToUserList(ActionEvent actionEvent) {
    }
}
