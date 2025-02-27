package services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class WeatherService {
    private static final String API_KEY = "d5d8c78a18c34d12a96101118252602";
    private static final String BASE_URL = "http://api.weatherapi.com/v1";

    public JsonObject getWeather(String location) throws IOException {
        String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8.toString());
        String urlString = BASE_URL + "/current.json?key=" + API_KEY + "&q=" + encodedLocation;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            System.out.println("Error: HTTP response code " + responseCode);
            return null;
        }

        StringBuilder inline = new StringBuilder();
        Scanner scanner = new Scanner(conn.getInputStream());

        while (scanner.hasNext()) {
            inline.append(scanner.nextLine());
        }
        scanner.close();

        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(inline.toString(), JsonObject.class);

        System.out.println("Weather API Response: " + jsonResponse); // Debugging output
        return jsonResponse;
    }
    public String searchLocation(String address) throws IOException {
        // Extract the country name from the address
        String country = extractCountryFromAddress(address);
        if (country == null) {
            return null;
        }

        String encodedCountry = URLEncoder.encode(country, StandardCharsets.UTF_8.toString());
        String urlString = BASE_URL + "/search.json?key=" + API_KEY + "&q=" + encodedCountry;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            System.out.println("Error: HTTP response code " + responseCode);
            return null;
        }

        StringBuilder inline = new StringBuilder();
        Scanner scanner = new Scanner(conn.getInputStream());

        while (scanner.hasNext()) {
            inline.append(scanner.nextLine());
        }
        scanner.close();

        Gson gson = new Gson();
        JsonArray jsonResponse = gson.fromJson(inline.toString(), JsonArray.class);

        if (jsonResponse.size() > 0) {
            JsonObject firstMatch = jsonResponse.get(0).getAsJsonObject();
            return firstMatch.get("name").getAsString();
        }

        return null;
    }

    private String extractCountryFromAddress(String address) {
        // Assuming the country name is the last part of the address
        String[] parts = address.split(",");
        if (parts.length > 0) {
            return parts[parts.length - 1].trim();
        }
        return null;
    }
}