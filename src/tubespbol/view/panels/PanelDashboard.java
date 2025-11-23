package tubespbol.view.panels;

import javax.swing.*;
import java.awt.*;

public class PanelDashboard extends JPanel {

    public PanelDashboard() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 250, 255)); // Biru muda

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Montserrat", Font.BOLD, 30));
        title.setForeground(new Color(31, 78, 121));
        title.setHorizontalAlignment(SwingConstants.LEFT);
        title.setBorder(BorderFactory.createEmptyBorder(24, 30, 16, 16));

        add(title, BorderLayout.NORTH);

        JPanel cardArea = new JPanel();
        cardArea.setBackground(new Color(245, 250, 255));
        cardArea.setLayout(new GridLayout(1, 3, 24, 0));
        cardArea.setBorder(BorderFactory.createEmptyBorder(18, 30, 24, 30));

        cardArea.add(createCard("Jadwal Berikutnya", "Belum Ada", new Color(247, 207, 175), new Color(169, 110, 71)));
        cardArea.add(createCard("Total Permintaan", "5", new Color(193, 220, 230), new Color(68, 130, 152)));
        cardArea.add(createCard("Disetujui", "3", new Color(190, 230, 201), new Color(69, 130, 75)));

        add(cardArea, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String value, Color bg, Color textColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2, true),
                BorderFactory.createEmptyBorder(28, 28, 28, 28)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Montserrat", Font.BOLD, 15));
        lblTitle.setForeground(textColor);

        JLabel lblValue = new JLabel(value);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblValue.setFont(new Font("Montserrat", Font.BOLD, 36));
        lblValue.setForeground(textColor);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }
}
