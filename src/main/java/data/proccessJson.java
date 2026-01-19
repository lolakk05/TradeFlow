package data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class proccessJson {
    public static void parseJson(String jsonData) {
        JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();

        JsonObject timeSeries = jsonObject.getAsJsonObject("Time Series (Daily)");
        if (timeSeries == null) {
            System.out.println("Time Series data not found in the JSON response.");
            return;
        }
        
        int counter = 0;

        for(String date: timeSeries.keySet()) {
            if (counter >= 7) break;

            JsonObject dailyData = timeSeries.getAsJsonObject(date);
            double price = Double.parseDouble(dailyData.get("4. close").getAsString());

            System.out.println("Date: " + date + ", Price: " + price);

            counter++;
        }
    }
    
}
