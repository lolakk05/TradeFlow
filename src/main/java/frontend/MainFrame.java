package frontend;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private StockChartPanel chartPanel;

    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setTitle("TradeFlow");

        chartPanel = new StockChartPanel();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(chartPanel, "CHART");

        add(mainPanel);

        cardLayout.show(mainPanel, "CHART");

        setVisible(true);
    }
}
