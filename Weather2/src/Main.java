import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) {
        double totalTemperature = 0;
        int limit = 5;

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.yandex.ru/v2/forecast?lat=55.75&lon=37.62&limit=" + limit))
                .GET()
                .header("X-Yandex-Weather-Key", "6c7ec543-fe44-472e-a783-0289f1229041")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            String responseString = response.body();
            JSONObject responseJSON = new JSONObject(responseString);
//            FileOutputStream outputStream = new FileOutputStream("output.json");
//            outputStream.write(responseString.getBytes());
            JSONArray forecasts = responseJSON.getJSONArray("forecasts");
            int count = forecasts.length();

            for (int i = 0; i < count; i++) {
                JSONObject forecast = forecasts.getJSONObject(i);
                double temperature = forecast.getJSONObject("parts").getJSONObject("day").getDouble("temp_avg");

                System.out.println("Температура день " + (i + 1) + ": " + temperature + "C");
                totalTemperature += temperature;
            }

            double averageTemperature = totalTemperature / count;
            System.out.println("Средняя температура за период: " + averageTemperature + "C");

        } catch (Exception e) {
            System.err.println("Error making HTTP request: " + e.getMessage());
        }
    }
}