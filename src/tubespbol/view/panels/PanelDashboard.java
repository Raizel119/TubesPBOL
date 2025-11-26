package tubespbol.view.panels;

import javax.swing.*;
import java.awt.*;

public class PanelDashboard extends JPanel {

    public PanelDashboard() {
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 245)); // Background abu-abu terang

        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(41, 128, 185));
        title.setHorizontalAlignment(SwingConstants.LEFT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 20));

        add(title, BorderLayout.NORTH);

        JPanel cardArea = new JPanel();
        cardArea.setBackground(new Color(236, 240, 245));
        cardArea.setLayout(new GridLayout(1, 3, 20, 0));
        cardArea.setBorder(BorderFactory.createEmptyBorder(15, 25, 25, 25));

        cardArea.add(createCard("Jadwal Berikutnya", "Belum Ada", new Color(247, 207, 175), new Color(169, 110, 71)));
        cardArea.add(createCard("Total Permintaan", "5", new Color(193, 220, 230), new Color(68, 130, 152)));
        cardArea.add(createCard("Disetujui", "3", new Color(190, 230, 201), new Color(69, 130, 75)));

        add(cardArea, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String value, Color bg, Color textColor) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow effect
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 15, 15);
            }
        };
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(3, 3, 3, 3),
                BorderFactory.createEmptyBorder(25, 20, 25, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalBg = bg;
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(originalBg.brighter());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(originalBg);
            }
        });

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(textColor);

        JLabel lblValue = new JLabel(value);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValue.setForeground(textColor);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }
}
