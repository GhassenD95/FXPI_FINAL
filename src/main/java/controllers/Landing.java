package controllers;

import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import services.WeatherService;
import java.io.IOException;

public class Landing {

    @FXML
    private Label weatherLabel;

    @FXML
    private Button performanceListButton;

    @FXML
    private Button tournoiButton;

    @FXML
    public void initialize() {
        fetchWeatherData();
    }

    private void fetchWeatherData() {
        WeatherService weatherService = new WeatherService();

        // Use a background thread to fetch data
        Task<JsonObject> weatherTask = new Task<>() {
            @Override
            protected JsonObject call() throws Exception {
                return weatherService.getWeather("London");
            }
        };

        weatherTask.setOnSucceeded(event -> {
            JsonObject weatherData = weatherTask.getValue();
            if (weatherData == null) {
                weatherLabel.setText("No weather data available.");
                return;
            }

            try {
                JsonObject currentWeather = weatherData.getAsJsonObject("current");
                String temperature = currentWeather.get("temp_c").getAsString();
                String condition = currentWeather.getAsJsonObject("condition").get("text").getAsString();
                weatherLabel.setText("Temperature: " + temperature + "°C\nCondition: " + condition);
            } catch (Exception e) {
                weatherLabel.setText("Error fetching weather.");
                e.printStackTrace();
            }
        });

        weatherTask.setOnFailed(event -> {
            weatherLabel.setText("Failed to fetch weather data.");
            event.getSource().getException().printStackTrace();
        });

        new Thread(weatherTask).start();
    }

    @FXML
    private void handlePerformanceListButtonAction() throws IOException {
        navigateTo("/Performancelist.fxml", performanceListButton);
    }

    @FXML
    private void handleTournoiButtonAction() throws IOException {
        navigateTo("/Tournoi1.fxml", tournoiButton);
    }

    private void navigateTo(String fxmlFile, Button button) throws IOException {
        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
