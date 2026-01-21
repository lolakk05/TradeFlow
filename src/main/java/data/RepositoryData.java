package data;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class RepositoryData {
    private ArrayList<StockRecord> stockRecords;

    public RepositoryData() {
        this.stockRecords = new ArrayList<>();

        load();
    }

    public ArrayList<StockRecord> load() {
        this.stockRecords.clear();
        try(ObjectInputStream is = new ObjectInputStream(new FileInputStream("records.ser"))) {
            int size =is.readInt();
            for(int i = 0; i < size; i++) {stockRecords.add((StockRecord) is.readObject());
            }
            Collections.reverse(stockRecords);
        }
        catch(java.io.FileNotFoundException e) {
            System.out.println("Brak pliku z danymi (to normalne przy pierwszym uruchomieniu).");
        }
        catch(java.io.EOFException e) {
            System.out.println("Brak pliku z danymi (to normalne przy pierwszym uruchomieniu).");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return stockRecords;
    }

    public void save() {
        try(ObjectOutputStream so = new ObjectOutputStream(new FileOutputStream("records.ser"))) {
            so.writeInt(stockRecords.size());
            for(StockRecord stock : stockRecords) {
                so.writeObject(stock);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void upload(ArrayList<StockRecord> stockRecords) {
        this.stockRecords = stockRecords;
        save();
    }
}
