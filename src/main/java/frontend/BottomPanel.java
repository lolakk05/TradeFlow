package frontend;

import javax.swing.*;
import java.awt.*;

public class BottomPanel extends JPanel {
    public BottomPanel() {
        this.setBackground(Color.white);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel walletLabel = new JLabel("Wallet");
        this.add(walletLabel);

        JTextField buyAmount =  new JTextField(20);
        this.add(buyAmount);

        JButton buyButton = new JButton("Buy");
        this.add(buyButton);

        JTextField sellAmount =  new JTextField(20);
        this.add(sellAmount);

        JButton sellButton = new JButton("Sell");
        this.add(sellButton);
    }
}
