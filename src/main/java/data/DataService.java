package data;

import javax.swing.*;
import java.util.ArrayList;

public class DataService {
    private RepositoryData repositoryData = new RepositoryData();

    public ArrayList<StockRecord> getStockRecords(String symbol) {
        ArrayList<StockRecord> stockRecords = repositoryData.load(symbol);

        if(stockRecords.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Requesting data from API");
            stockRecords = GetData.downloadData(symbol);

            if(!stockRecords.isEmpty()) {
                repositoryData.save(symbol);
            }
        }
        return stockRecords;
    }
}
