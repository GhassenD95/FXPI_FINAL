package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.module4.PerformanceEquipe;
import services.module4.ServicePerformanceEquipe;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Performance1Controller {

    @FXML
    public Button navigateButtonStat;
    @FXML
    public Button navigateButton;
    @FXML
    private ListView<PerformanceEquipe> listView;
    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    private final ObservableList<PerformanceEquipe> performanceList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadPerformanceData();

        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(PerformanceEquipe item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create a container for the row with a VBox for the text and a HBox for buttons
                    VBox infoBox = new VBox(5);
                    infoBox.setStyle("-fx-padding: 10px;");

                    // Name and team info
                    String displayText = String.format("%-20s  %-20s  Buts: %-3d  Fautes: %-3d   Rang: %-3d",
                            item.getEquipe().getNom(),
                            item.getTournois().getNom(),
                            item.getVictoires(),
                            item.getPertes(),
                            item.getRang());

                    // Create label and set styles for the text
                    javafx.scene.control.Label label = new javafx.scene.control.Label(displayText);
                    label.setFont(Font.font("Arial", 14));
                    label.setTextFill(Color.web("#043873"));
                    infoBox.getChildren().add(label);

                    // Modify button
                    Button modifyButton = new Button();
                    ImageView modifyImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/vector.png")));
                    modifyImageView.setFitHeight(15);
                    modifyImageView.setFitWidth(15);
                    modifyButton.setGraphic(modifyImageView);
                    modifyButton.getStyleClass().add("icon-button");

                    // Delete button
                    Button deleteButton = new Button();
                    ImageView deleteImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/trash.png")));
                    deleteImageView.setFitHeight(15);
                    deleteImageView.setFitWidth(15);
                    deleteButton.setGraphic(deleteImageView);
                    deleteButton.getStyleClass().add("icon-button");

                    // Buttons box
                    HBox buttonsBox = new HBox(10, modifyButton, deleteButton);
                    buttonsBox.setStyle("-fx-padding: 10px;");
                    infoBox.getChildren().add(buttonsBox);

                    // Set the background style for each row to make it more distinct
                    infoBox.setStyle("-fx-background-color: #f8f9fa; -fx-border-radius: 5px; -fx-border-color: #ddd;");

                    // Add the content to the cell
                    setGraphic(infoBox);

                    // Event actions for buttons
                    modifyButton.setOnAction(event -> openUpdateDialog(item));
                    deleteButton.setOnAction(event -> deletePerformance(item));
                }
            }

    });
    }

    private void loadPerformanceData() {
        ServicePerformanceEquipe servicePerformanceEquipe = new ServicePerformanceEquipe();
        try {
            List<PerformanceEquipe> performances = servicePerformanceEquipe.getAll();
            performanceList.setAll(performances);
            listView.setItems(performanceList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deletePerformance(PerformanceEquipe performance) {
        ServicePerformanceEquipe servicePerformanceEquipe = new ServicePerformanceEquipe();
        try {
            servicePerformanceEquipe.delete(performance);
            performanceList.remove(performance);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openUpdateDialog(PerformanceEquipe performance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Performance2.fxml"));
            Parent parent = loader.load();

            Performance2Controller controller = loader.getController();
            controller.setPerformance(performance);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(parent));
            stage.showAndWait();

            loadPerformanceData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void navigateToPerformance() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Performance.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setOnHidden(event -> loadPerformanceData());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void navigateToPerformanceCharts() {
        try {
            // Load the new FXML file for the performance charts
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PerformanceCharts.fxml"));
            AnchorPane root = loader.load();

            // Create a new stage (window)
            Stage chartStage = new Stage();
            chartStage.setTitle("Performance Charts");
            chartStage.setScene(new Scene(root));

            // Show the new window
            chartStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleSearch(ActionEvent event) {
        String searchText = searchField.getText();
        ServicePerformanceEquipe servicePerformanceEquipe = new ServicePerformanceEquipe();
        try {
            List<PerformanceEquipe> filteredPerformances = servicePerformanceEquipe.rechercheDynamique(searchText);
            performanceList.setAll(filteredPerformances);
            listView.setItems(performanceList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}