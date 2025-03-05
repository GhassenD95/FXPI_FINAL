package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    public VBox sidebarContainer;
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
        listView.setCellFactory(param -> new PerformanceListCell());
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

    private class PerformanceListCell extends ListCell<PerformanceEquipe> {
        private final HBox content;
        private final VBox infoBox;
        private final Text nameText;
        private final Text tournamentText;
        private final Text statsText;
        private final Button modifyButton;
        private final Button deleteButton;

        public PerformanceListCell() {
            nameText = new Text();
            nameText.setFont(Font.font("Arial", 14));
            nameText.setFill(Color.web("#043873"));

            tournamentText = new Text();
            tournamentText.setFont(Font.font("Arial", 14));
            tournamentText.setFill(Color.web("#555"));

            statsText = new Text();
            statsText.setFont(Font.font("Arial", 14));
            statsText.setFill(Color.web("#28a745"));

            infoBox = new VBox(5, nameText, tournamentText, statsText);
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
            deleteButton.setOnAction(event -> {
                try {
                    deletePerformance(getItem());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        @Override
        protected void updateItem(PerformanceEquipe performance, boolean empty) {
            super.updateItem(performance, empty);
            if (empty || performance == null) {
                setGraphic(null);
            } else {
                nameText.setText("Equipe: " + performance.getEquipe().getNom());
                tournamentText.setText("Tournoi: " + performance.getTournois().getNom());
                statsText.setText("Buts: " + performance.getVictoires() + "  Fautes: " + performance.getPertes() + "  Rang: " + performance.getRang());
                setGraphic(content);
            }
        }
    }

    private void deletePerformance(PerformanceEquipe performance) throws SQLException {
        ServicePerformanceEquipe servicePerformanceEquipe = new ServicePerformanceEquipe();
        servicePerformanceEquipe.delete(performance);
        performanceList.remove(performance);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PerformanceCharts.fxml"));
            Parent root = loader.load();

            Stage chartStage = new Stage();
            chartStage.setTitle("Performance Charts");
            chartStage.setScene(new Scene(root));

            chartStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
