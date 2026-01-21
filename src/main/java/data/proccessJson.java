package data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class proccessJson {
    public static ArrayList<StockRecord> parseJson(String jsonData, String symbol) {
        JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
        ArrayList<StockRecord> stockRecords = new ArrayList<>();

        JsonObject timeSeries = jsonObject.getAsJsonObject("Time Series (Daily)");
        if (timeSeries == null) {
            System.out.println("Time Series data not found in the JSON response.");
            return stockRecords;
        }

        int counter = 0;

        for (String date : timeSeries.keySet()) {
            if (counter >= 7) break;

            JsonObject dailyData = timeSeries.getAsJsonObject(date);
            double open = Double.parseDouble(dailyData.get("1. open").getAsString());
            double high = Double.parseDouble(dailyData.get("2. high").getAsString());
            double low = Double.parseDouble(dailyData.get("3. low").getAsString());
            double close = Double.parseDouble(dailyData.get("4. close").getAsString());

            stockRecords.add(new StockRecord(date, symbol, open, high, low, close));

            counter++;
        }
        return stockRecords;
    }
}
