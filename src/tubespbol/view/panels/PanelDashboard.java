package tubespbol.view.panels;

import javax.swing.*;
import java.awt.*;

public class PanelDashboard extends JPanel {

    public PanelDashboard() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Dashboard Mahasiswa");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(title, BorderLayout.NORTH);

        JPanel cardArea = new JPanel();
        cardArea.setBackground(Color.WHITE);
        cardArea.setLayout(new GridLayout(1, 3, 20, 20));
        cardArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cardArea.add(createCard("Jadwal Berikutnya", "Belum ada"));
        cardArea.add(createCard("Total Permintaan", "5"));
        cardArea.add(createCard("Pertemuan Disetujui", "3"));

        add(cardArea, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String value) {
        JPanel card = new JPanel();
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }
}
