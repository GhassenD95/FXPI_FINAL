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
import models.module5.Tournois;
import services.module5.ServiceTournois;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Tournoi1Controller {

    @FXML
    private TableView<Tournois> tableView;

    @FXML
    private TableColumn<Tournois, String> nameColumn;

    @FXML
    private TableColumn<Tournois, String> addressColumn;

    @FXML
    private TableColumn<Tournois, String> sportColumn;

    @FXML
    private TableColumn<Tournois, Date> dateDebutColumn;

    @FXML
    private TableColumn<Tournois, Date> dateFinColumn;

    @FXML
    private TableColumn<Tournois, Void> actionColumn;

    private ObservableList<Tournois> tournoisList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize the columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        sportColumn.setCellValueFactory(new PropertyValueFactory<>("sport"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));

        // Add cell factory for action buttons
        addButtonToTable();

        // Load data from the database
        loadTournoisData();
    }

    private void loadTournoisData() {
        ServiceTournois serviceTournois = new ServiceTournois();
        try {
            List<Tournois> tournois = serviceTournois.getAll();
            tournoisList.setAll(tournois);
            tableView.setItems(tournoisList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addButtonToTable() {
        Callback<TableColumn<Tournois, Void>, TableCell<Tournois, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Tournois, Void> call(final TableColumn<Tournois, Void> param) {
                final TableCell<Tournois, Void> cell = new TableCell<>() {

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
                            Tournois data = getTableView().getItems().get(getIndex());
                            openUpdateDialog(data);
                        });

                        deleteButton.setOnAction(event -> {
                            Tournois data = getTableView().getItems().get(getIndex());
                            deleteTournois(data);
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

            // Refresh the table after the update
            loadTournoisData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}