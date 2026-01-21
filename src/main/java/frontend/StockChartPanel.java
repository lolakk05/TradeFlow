package frontend;

import data.GetData;
import data.RepositoryData;
import data.StockRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class StockChartPanel extends JPanel {
    private RepositoryData repo;
    private TopPanel topPanel;
    private BottomPanel bottomPanel;

    private Component currentCenterComponent;

    public StockChartPanel() {
        this.repo = new RepositoryData();
        this.topPanel = new TopPanel();
        this.bottomPanel = new BottomPanel();

        this.setLayout(new BorderLayout());

        this.add(topPanel, BorderLayout.NORTH);
        this.add(bottomPanel, BorderLayout.SOUTH);

        JLabel placeholder = new JLabel("Wpisz symbol (np. BTC) i kliknij Szukaj", SwingConstants.CENTER);
        this.currentCenterComponent = placeholder;
        this.add(placeholder, BorderLayout.CENTER);

        topPanel.getSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String symbol = topPanel.getSymbol().trim().toUpperCase();
                if (!symbol.isEmpty()) {
                    updateChart(symbol);
                }
            }
        });
    }

    public void updateChart(String symbol) {
        ArrayList<StockRecord> stockRecords;


        if(repo.load(symbol).isEmpty()) {
            stockRecords = GetData.downloadData(symbol);
            repo.upload(stockRecords, symbol);
        } else {
            stockRecords = repo.load(symbol);
        }

        if (currentCenterComponent != null) {
            this.remove(currentCenterComponent);
        }

        if (stockRecords == null || stockRecords.isEmpty()) {
            currentCenterComponent = new JLabel("Brak danych dla: " + symbol, SwingConstants.CENTER);
            this.add(currentCenterComponent, BorderLayout.CENTER);
        } else {

            SimpleDateFormat sortFormat = new SimpleDateFormat("yyyy-MM-dd"); // Upewnij się, że ten format pasuje do Twoich danych!
            stockRecords.sort((o1, o2) -> {
                try {
                    Date d1 = sortFormat.parse(o1.getDate());
                    Date d2 = sortFormat.parse(o2.getDate());
                    return d1.compareTo(d2); // Sortowanie od najstarszej do najnowszej
                } catch (Exception e) {
                    return 0;
                }
            });;
            OHLCDataset dataset = createDataset(stockRecords);
            JFreeChart chart = createChart(dataset, symbol, stockRecords);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setMouseWheelEnabled(true);

            currentCenterComponent = chartPanel;
            this.add(chartPanel, BorderLayout.CENTER);
        }

        this.revalidate();
        this.repaint();
    }

    private JFreeChart createChart(OHLCDataset dataset, String symbol, ArrayList<StockRecord> stockRecords) {
        JFreeChart chart = ChartFactory.createCandlestickChart(
                "Price chart of " + symbol, "Date", "Stock Value", dataset, false);

        XYPlot plot = chart.getXYPlot();
        DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
        domainAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
        domainAxis.setAutoRange(false);

        if(!stockRecords.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                StockRecord lastRecord = repo.load(symbol).getLast();

                Date lastDate = sdf.parse(lastRecord.getDate());

                Calendar cal = Calendar.getInstance();
                cal.setTime(lastDate);
                cal.add(Calendar.MONTH, -1);
                Date startDate = cal.getTime();

                cal.setTime(lastDate);
                Date endDate = cal.getTime();

                domainAxis.setRange(startDate, endDate);
                System.out.println(startDate + " " + endDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setUpperMargin(0.05);
        rangeAxis.setLowerMargin(0.05);

        CandlestickRenderer renderer = (CandlestickRenderer) plot.getRenderer();
        renderer.setUpPaint(Color.GREEN);
        renderer.setDownPaint(Color.RED);
        renderer.setUseOutlinePaint(true);

        return chart;
    }

    public OHLCDataset createDataset(ArrayList<StockRecord> stockRecords) {
        int size = stockRecords.size();
        Date[] dates = new Date[size];
        double[] opens = new double[size];
        double[] highs = new double[size];
        double[] lows = new double[size];
        double[] closes = new double[size];
        double[] volumes = new double[size];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < size; i++) {
            StockRecord r = stockRecords.get(i);
            try { dates[i] = sdf.parse(r.getDate()); }
            catch (Exception e) { dates[i] = new Date(); }
            opens[i] = r.getOpen();
            highs[i] = r.getHigh();
            lows[i] = r.getLow();
            closes[i] = r.getClose();
            volumes[i] = r.getVolume();
        }

        return new DefaultHighLowDataset(stockRecords.get(0).getSymbol(), dates, highs, lows, opens, closes, volumes);
    }
}