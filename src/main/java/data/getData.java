package data;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import io.github.cdimascio.dotenv.Dotenv;

public class getData {
    public static ArrayList<StockRecord> downloadData() {
        Dotenv dotenv = Dotenv.load();
        ArrayList<StockRecord> records = new ArrayList<>();

        String apiKey = dotenv.get("API_KEY").trim();
        String symbol = "BTC";

        String url = "https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_DAILY&symbol="
                + symbol + "&market=USD&apikey=" + apiKey;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            records = proccessJson.parseJson(response.body(), symbol);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }
}
