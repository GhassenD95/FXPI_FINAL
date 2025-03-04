package controllers;

import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.module4.Tournois;
import services.module4.ServiceTournois;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Tournoi1Controller {

    @FXML
    public Label weatherLabel;
    @FXML
    public ImageView weatherIcon;
    @FXML
    private ListView<Tournois> listView;
    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    private ObservableList<Tournois> tournoisList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        loadTournoisData();
        loadTournoisData();
        listView.setCellFactory(param -> new TournoiListCell());
    }

    private void loadTournoisData() {
        ServiceTournois serviceTournois = new ServiceTournois();
        try {
            List<Tournois> tournois = serviceTournois.getAll();
            tournoisList.setAll(tournois);
            listView.setItems(tournoisList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleSearch(ActionEvent event) {
        String searchText = searchField.getText();
        ServiceTournois serviceTournois = new ServiceTournois();
        try {
            List<Tournois> filteredTournois = serviceTournois.rechercheDynamique(searchText);
            tournoisList.setAll(filteredTournois);
            listView.setItems(tournoisList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void navigateToTournoi(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Tournoi.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.setOnHidden(event -> loadTournoisData());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class TournoiListCell extends ListCell<Tournois> {
        private final HBox content;
        private final VBox infoBox;
        private final Text nameText;
        private final Text addressText;
        private final Text sportText;
        private final Button modifyButton;
        private final Button deleteButton;

        public TournoiListCell() {
            nameText = new Text();
            nameText.setFont(Font.font("Arial", 14));
            nameText.setFill(Color.web("#043873"));

            addressText = new Text();
            addressText.setFont(Font.font("Arial", 14));
            addressText.setFill(Color.web("#555"));

            sportText = new Text();
            sportText.setFont(Font.font("Arial", 14));
            sportText.setFill(Color.web("#28a745"));

            infoBox = new VBox(5, nameText, addressText, sportText);
            infoBox.setStyle("-fx-padding: 10px;");

            modifyButton = new Button();
            ImageView modifyImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/vector.png")));
            modifyImageView.setFitHeight(15);
            modifyImageView.setFitWidth(15);
            modifyButton.setGraphic(modifyImageView);
            modifyButton.getStyleClass().add("icon-button");

            deleteButton = new Button();
            ImageView deleteImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/trash.png")));
            deleteImageView.setFitHeight(15);
            deleteImageView.setFitWidth(15);
            deleteButton.setGraphic(deleteImageView);
            deleteButton.getStyleClass().add("icon-button");

            HBox buttonsBox = new HBox(10, modifyButton, deleteButton);
            buttonsBox.setStyle("-fx-padding: 10px;");

            content = new HBox(20, infoBox, buttonsBox);
            content.setSpacing(15);
            content.setStyle("-fx-padding: 10px; -fx-border-color: #ddd; -fx-background-color: #f8f9fa;");

            modifyButton.setOnAction(event -> openUpdateDialog(getItem()));
            deleteButton.setOnAction(event -> deleteTournois(getItem()));
        }

        @Override
        protected void updateItem(Tournois tournois, boolean empty) {
            super.updateItem(tournois, empty);
            if (empty || tournois == null) {
                setGraphic(null);
            } else {
                nameText.setText("Name: " + tournois.getNom());
                addressText.setText("Address: " + tournois.getAdresse());
                sportText.setText("Sport: " + tournois.getSport());
                setGraphic(content);
            }
        }
    }

    private void deleteTournois(Tournois tournois) {
        ServiceTournois serviceTournois = new ServiceTournois();
        serviceTournois.delete(tournois);
        tournoisList.remove(tournois);
    }

    private void openUpdateDialog(Tournois tournois) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Tournoi2.fxml"));
            Parent parent = loader.load();

            Tournoi2Controller controller = loader.getController();
            controller.setTournois(tournois);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(parent));
            stage.showAndWait();

            loadTournoisData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void openTournoisDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Tournoi.fxml"));
            Parent parent = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(parent));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
