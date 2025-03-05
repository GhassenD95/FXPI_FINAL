package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class FootballDataService {

    // API Key and URL for Football Data API
    private static final String API_KEY = "8a97b9cbd8054bb6a6807987751d6753";
    private static final String API_URL = "https://api.football-data.org/v4/matches";

    // Method to get today's matches
    public JsonNode getMatchesForToday() {
        // Create HttpClient and request
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(API_URL);
            request.addHeader("X-Auth-Token", API_KEY);

            // Send the request and get the response
            HttpResponse response = httpClient.execute(request);

            // Check if the response is successful
            if (response.getStatusLine().getStatusCode() == 200) {
                // Use Jackson to parse the response
                ObjectMapper mapper = new ObjectMapper();
                JsonNode matches = mapper.readTree(response.getEntity().getContent()).get("matches");

                // Return the first match (if available)
                if (matches != null && matches.size() > 0) {
                    return matches.get(0); // Return the first match
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle any exceptions during HTTP request/response processing
        }

        return null; // Return null if no match is found or there's an error
    }

    public static void main(String[] args) {
        // Test the FootballDataService
        FootballDataService service = new FootballDataService();
        JsonNode match = service.getMatchesForToday();

        if (match != null) {
            System.out.println("Upcoming Match: " + match.toString());
        } else {
            System.out.println("No matches scheduled for today.");
        }
    }
}
