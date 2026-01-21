package data;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class RepositoryData {
    private ArrayList<StockRecord> stockRecords;

    public RepositoryData() {
        this.stockRecords = new ArrayList<>();
    }

    public ArrayList<StockRecord> load(String symbol) {
        this.stockRecords.clear();
        try(ObjectInputStream is = new ObjectInputStream(new FileInputStream("records/" + symbol + ".ser"))) {
            int size =is.readInt();
            for(int i = 0; i < size; i++) {stockRecords.add((StockRecord) is.readObject());
            }
        }
        catch(java.io.FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(java.io.EOFException e) {
            e.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return stockRecords;
    }

    public void save(String symbol) {
        try(ObjectOutputStream so = new ObjectOutputStream(new FileOutputStream("records/" + symbol + ".ser"))) {
            so.writeInt(stockRecords.size());
            for(StockRecord stock : stockRecords) {
                so.writeObject(stock);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void upload(ArrayList<StockRecord> stockRecords, String symbol) {
        this.stockRecords = stockRecords;
        save(symbol);
    }
}
