package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.module5.PerformanceEquipe;
import services.module5.ServicePerformanceEquipe;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Performance1Controller {

    @FXML
    private TableView<PerformanceEquipe> tableView;

    @FXML
    private TableColumn<PerformanceEquipe, String> equipeColumn;

    @FXML
    private TableColumn<PerformanceEquipe, String> tournoisColumn;

    @FXML
    private TableColumn<PerformanceEquipe, Integer> victoiresColumn;

    @FXML
    private TableColumn<PerformanceEquipe, Integer> pertesColumn;

    @FXML
    private TableColumn<PerformanceEquipe, Integer> rangColumn;

    @FXML
    private TableColumn<PerformanceEquipe, Void> actionColumn;

    private ObservableList<PerformanceEquipe> performanceList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize the columns
        equipeColumn.setCellValueFactory(new PropertyValueFactory<>("equipe"));
        tournoisColumn.setCellValueFactory(new PropertyValueFactory<>("tournois"));
        victoiresColumn.setCellValueFactory(new PropertyValueFactory<>("victoires"));
        pertesColumn.setCellValueFactory(new PropertyValueFactory<>("pertes"));
        rangColumn.setCellValueFactory(new PropertyValueFactory<>("rang"));

        // Add cell factory for action buttons
        addButtonToTable();

        // Load data from the database
        loadPerformanceData();
    }

    private void loadPerformanceData() {
        ServicePerformanceEquipe servicePerformanceEquipe = new ServicePerformanceEquipe();
        try {
            List<PerformanceEquipe> performances = servicePerformanceEquipe.getAll();
            performanceList.setAll(performances);
            tableView.setItems(performanceList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addButtonToTable() {
        Callback<TableColumn<PerformanceEquipe, Void>, TableCell<PerformanceEquipe, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<PerformanceEquipe, Void> call(final TableColumn<PerformanceEquipe, Void> param) {
                final TableCell<PerformanceEquipe, Void> cell = new TableCell<>() {

                    private final Button modifyButton = new Button();
                    private final Button deleteButton = new Button();

                    {
                        ImageView modifyImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/vector.png")));
                        modifyImageView.setFitHeight(12);
                        modifyImageView.setFitWidth(12);
                        modifyButton.setGraphic(modifyImageView);
                        modifyButton.getStyleClass().add("modify-button");

                        ImageView deleteImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/trash.png")));
                        deleteImageView.setFitHeight(12);
                        deleteImageView.setFitWidth(12);
                        deleteButton.setGraphic(deleteImageView);
                        deleteButton.getStyleClass().add("delete-button");

                        modifyButton.setOnAction(event -> {
                            PerformanceEquipe data = getTableView().getItems().get(getIndex());
                            openUpdateDialog(data);
                        });

                        deleteButton.setOnAction(event -> {
                            PerformanceEquipe data = getTableView().getItems().get(getIndex());
                            deletePerformance(data);
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttons = new HBox(10, modifyButton, deleteButton); // 10 is the spacing between buttons
                            buttons.setAlignment(javafx.geometry.Pos.CENTER); // Align buttons in the center
                            setGraphic(buttons);
                        }
                    }
                };
                return cell;
            }
        };

        actionColumn.setCellFactory(cellFactory);
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

            // Refresh the table after the update
            loadPerformanceData();
        } catch (IOException e) {
            e.printStackTrace();
        }
}}
