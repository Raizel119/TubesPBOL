package tubespbol.view.panels;

import javax.swing.*;
import java.awt.*;

public class PanelDashboardDosen extends JPanel {

    public PanelDashboardDosen() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Dashboard Dosen");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(title, BorderLayout.NORTH);

        JPanel cardArea = new JPanel(new GridLayout(1, 3, 20, 20));
        cardArea.setBackground(Color.WHITE);
        cardArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cardArea.add(createCard("Permintaan Baru", "4"));
        cardArea.add(createCard("Disetujui", "7"));
        cardArea.add(createCard("Ditolak", "2"));

        add(cardArea, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblValue = new JLabel(value);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }
}
