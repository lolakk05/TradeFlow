package data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class proccessJson {
    public static ArrayList<StockRecord> parseJson(String jsonData, String symbol) {
        JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
        ArrayList<StockRecord> stockRecords = new ArrayList<>();

        JsonObject timeSeries = null;
        if (jsonObject.has("Time Series (Digital Currency Daily)")) {
            timeSeries = jsonObject.getAsJsonObject("Time Series (Digital Currency Daily)");
        } else if (jsonObject.has("Time Series (Daily)")) {
            timeSeries = jsonObject.getAsJsonObject("Time Series (Daily)");
        } else {
            System.err.println("Błąd: Nie znaleziono danych Time Series. Sprawdź limit zapytań API.");
            return stockRecords;
        }

        int counter = 0;

        for (String date : timeSeries.keySet()) {
            if (counter >= 30) break;

            JsonObject dailyData = timeSeries.getAsJsonObject(date);
            double open = Double.parseDouble(dailyData.get("1. open").getAsString());
            double high = Double.parseDouble(dailyData.get("2. high").getAsString());
            double low = Double.parseDouble(dailyData.get("3. low").getAsString());
            double close = Double.parseDouble(dailyData.get("4. close").getAsString());
            double volume = Double.parseDouble(dailyData.get("5. volume").getAsString());

            stockRecords.add(new StockRecord(symbol, date, open, high, low, close, volume));

            counter++;
        }
        RepositoryData repo =  new RepositoryData();
        Collections.reverse(stockRecords);
        repo.upload(stockRecords);
        return stockRecords;
    }
}
