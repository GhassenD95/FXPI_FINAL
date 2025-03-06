package services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

    public class WeatherService {
        private static final String API_KEY = "e1deb12c3277eecb50b223d065861c02";

        public static String getWeather(String city) {
            try {
                String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric&lang=fr";
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return parseWeather(response.toString());
            } catch (Exception e) {
                return "Erreur : " + e.getMessage();
            }
        }

        private static String parseWeather(String jsonResponse) {
            JsonObject json = JsonParser.parseString(jsonResponse).getAsJsonObject();
            String description = json.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
            double temperature = json.getAsJsonObject("main").get("temp").getAsDouble();
            int humidity = json.getAsJsonObject("main").get("humidity").getAsInt();
            double windSpeed = json.getAsJsonObject("wind").get("speed").getAsDouble();

            return "🌤️ Météo : " + description + "\n" +
                    "🌡️ Température : " + temperature + "°C\n" +
                    "💧 Humidité : " + humidity + "%\n" +
                    "💨 Vent : " + windSpeed + " m/s";
        }
    }