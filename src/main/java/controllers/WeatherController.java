package controllers;

import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import services.WeatherService;

import java.io.IOException;

public class WeatherController {
    @FXML
    private Label weatherLabel;

    @FXML
    public void initialize() {
        WeatherService weatherService = new WeatherService();

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

            JsonObject currentWeather = weatherData.getAsJsonObject("current");
            if (currentWeather != null) {
                String temperature = currentWeather.get("temp_c").getAsString();
                String condition = currentWeather.getAsJsonObject("condition").get("text").getAsString();
                weatherLabel.setText("Temperature: " + temperature + "°C\nCondition: " + condition);
            } else {
                weatherLabel.setText("Weather data is missing.");
            }
        });

        weatherTask.setOnFailed(event -> {
            weatherLabel.setText("Failed to fetch weather data.");
            event.getSource().getException().printStackTrace();
        });

        new Thread(weatherTask).start();
    }
}
