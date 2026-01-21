package frontend;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {
    private JLabel wallet;

    private JButton search;
    private JTextField symbolText;

    public TopPanel() {
        this.setBackground(Color.white);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel("TradeFlow");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(title);

        JLabel wallet = new  JLabel("Wallet: ");
        wallet.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(wallet);

        JTextField walletText =  new JTextField(20);
        this.add(walletText);

        JLabel symbol = new JLabel("Symbol: ");
        symbol.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(symbol);

        symbolText =  new JTextField(20);
        this.add(symbolText);

        search = new JButton("Search");
        this.add(search);
    }

    public JButton getSearchButton() {
        return search;
    }

    public String getSymbol() {
        return symbolText.getText();
    }
}
