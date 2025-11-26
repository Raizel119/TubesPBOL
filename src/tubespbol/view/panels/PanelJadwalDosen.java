package tubespbol.view.panels;

import tubespbol.view.components.DatePicker;
import tubespbol.view.components.TimePicker;
import javax.swing.*;
import java.awt.*;

public class PanelJadwalDosen extends JPanel {

    public PanelJadwalDosen() {
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 245));

        JLabel title = new JLabel("Atur Jadwal Ketersediaan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(41, 128, 185));
        title.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 20));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(15, 25, 25, 25),
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true)
        ));
        add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel lblTanggal = new JLabel("Tanggal:");
        lblTanggal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblTanggal, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        
        // Date Picker dengan kalender popup
        DatePicker datePicker = new DatePicker();
        formPanel.add(datePicker, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        JLabel lblWaktu = new JLabel("Waktu Available:");
        lblWaktu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblWaktu, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        
        // Time Picker dengan dialog popup
        TimePicker timePicker = new TimePicker();
        formPanel.add(timePicker, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblCatatan = new JLabel("Catatan Tambahan:");
        lblCatatan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblCatatan, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.WEST;
        JTextArea txtCatatan = new JTextArea(4, 20);
        txtCatatan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scrollCatatan = new JScrollPane(txtCatatan);
        scrollCatatan.setPreferredSize(new Dimension(250, 80));
        formPanel.add(scrollCatatan, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 15, 15, 15);
        JButton btnSimpan = new JButton("Simpan Ketersediaan") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(52, 152, 219),
                    getWidth(), getHeight(), new Color(41, 128, 185)
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
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSimpan.setPreferredSize(new Dimension(180, 38));
        btnSimpan.setFocusPainted(false);
        btnSimpan.setBorderPainted(false);
        btnSimpan.setContentAreaFilled(false);
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));

        formPanel.add(btnSimpan, gbc);
    }
}
