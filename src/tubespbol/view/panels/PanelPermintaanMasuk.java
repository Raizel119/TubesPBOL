package tubespbol.view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelPermintaanMasuk extends JPanel {

    public PanelPermintaanMasuk() {
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 245));

        JLabel title = new JLabel("Permintaan Masuk");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(41, 128, 185));
        title.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 20));

        add(title, BorderLayout.NORTH);

        String[] cols = {"Mahasiswa", "Tanggal", "Waktu", "Keperluan"};
        Object[][] data = {
            {"Rafi", "12/11/2025", "10:00", "Bimbingan"},
            {"Fahri", "13/11/2025", "14:00", "Revisi Tugas"},};

        JTable table = new JTable(new DefaultTableModel(data, cols));
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(220, 220, 220));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 25, 15, 25));
        add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(new Color(236, 240, 245));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));
        
        JButton btnSetujui = new JButton("Setujui") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(39, 174, 96),
                    getWidth(), getHeight(), new Color(34, 153, 84)
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
            }
        };
        btnSetujui.setForeground(Color.WHITE);
        btnSetujui.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSetujui.setPreferredSize(new Dimension(120, 36));
        btnSetujui.setFocusPainted(false);
        btnSetujui.setBorderPainted(false);
        btnSetujui.setContentAreaFilled(false);
        btnSetujui.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton btnTolak = new JButton("Tolak") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(231, 76, 60),
                    getWidth(), getHeight(), new Color(192, 57, 43)
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
            }
        };
        btnTolak.setForeground(Color.WHITE);
        btnTolak.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnTolak.setPreferredSize(new Dimension(120, 36));
        btnTolak.setFocusPainted(false);
        btnTolak.setBorderPainted(false);
        btnTolak.setContentAreaFilled(false);
        btnTolak.setCursor(new Cursor(Cursor.HAND_CURSOR));

        actionPanel.add(btnSetujui);
        actionPanel.add(Box.createHorizontalStrut(15));
        actionPanel.add(btnTolak);

        add(actionPanel, BorderLayout.SOUTH);
    }
}
