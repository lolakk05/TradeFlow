package frontend;

import data.RepositoryData;
import data.StockRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class StockChartPanel extends JPanel {
    private RepositoryData repo;

    private String[] dateLabels;

    public StockChartPanel() {
        this.repo =  new RepositoryData();
        ArrayList<StockRecord> stockRecords = new ArrayList<>();

        if(repo.load().isEmpty()) {
            stockRecords = data.getData.downloadData();
            repo.upload(stockRecords);
        }
        else {
            stockRecords =  repo.load();
            Collections.reverse(stockRecords);
        }

        this.setLayout(new BorderLayout());

        if (stockRecords == null || stockRecords.isEmpty()) {
            add(new JLabel("Brak danych do wyświetlenia"), BorderLayout.CENTER);
            return;
        }

        int size = stockRecords.size();
        dateLabels = new String[size];

        OHLCDataset dataset = createDataset(stockRecords);

        JFreeChart chart = ChartFactory.createCandlestickChart(
                "History of " + stockRecords.get(0).getSymbol(), "Date", "Stock Value", dataset, false);

        XYPlot plot = chart.getXYPlot();

        SymbolAxis domainAxis = new SymbolAxis("Data", dateLabels);

        domainAxis.setGridBandsVisible(false);
        plot.setDomainAxis(domainAxis);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();

        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setUpperMargin(0.05);
        rangeAxis.setLowerMargin(0.05);

        CandlestickRenderer renderer = (CandlestickRenderer) plot.getRenderer();

        renderer.setUpPaint(Color.GREEN);
        renderer.setDownPaint(Color.RED);
        renderer.setUseOutlinePaint(true);

        ChartPanel chartPanel = new ChartPanel(chart);
        this.add(chartPanel, BorderLayout.CENTER);
    }

    public OHLCDataset createDataset(ArrayList<StockRecord> stockRecords) {
        int size = stockRecords.size();

        Date[] dates = new Date[size];
        double[] opens = new double[size];
        double[] highs = new double[size];
        double[] lows = new double[size];
        double[] closes = new double[size];
        double[] volumes = new double[size];

        this.dateLabels = new String[size];

        for (int i = 0; i < size; i++) {
            StockRecord r = stockRecords.get(i);

            dates[i] = new Date(i);

            String dateText = r.getDate();
            if (dateText == null) {
                System.err.println("BŁĄD: Rekord nr " + i + " ma datę NULL! Wstawiam wartość zastępczą.");
                this.dateLabels[i] = "-";
            } else {
                this.dateLabels[i] = dateText;
            }

            opens[i] = r.getOpen();
            highs[i] = r.getHigh();
            lows[i] = r.getLow();
            closes[i] = r.getClose();
            volumes[i] = r.getVolume();
        }

        String symbolKey = "Unknown";
        if (!stockRecords.isEmpty() && stockRecords.get(0).getSymbol() != null) {
            symbolKey = stockRecords.get(0).getSymbol();
        }

        return new DefaultHighLowDataset(
                symbolKey,
                dates,
                highs,
                lows,
                opens,
                closes,
                volumes
        );
    }
}
