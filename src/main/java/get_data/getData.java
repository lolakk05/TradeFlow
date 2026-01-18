package get_data;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import io.github.cdimascio.dotenv.Dotenv;

public class getData {
    public static void downloadData() {
        Dotenv dotenv = Dotenv.load();

        String apiKey = dotenv.get("API_KEY").trim();
        String symbol = "BTC";

        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="
                + symbol + "&outputsize=compact&apikey=" + apiKey + "&datatype=json";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
