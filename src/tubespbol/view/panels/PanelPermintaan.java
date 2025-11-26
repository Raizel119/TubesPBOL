package tubespbol.view.panels;

import tubespbol.view.components.DatePicker;
import tubespbol.view.components.TimePicker;
import javax.swing.*;
import java.awt.*;

public class PanelPermintaan extends JPanel {

    public PanelPermintaan() {
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 245));

        JLabel title = new JLabel("Ajukan Permintaan Pertemuan");
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
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;

        JLabel lblDosen = new JLabel("Pilih Dosen:");
        lblDosen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblDosen, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JComboBox<String> cbDosen = new JComboBox<>(new String[]{"Dr. Budi", "Dr. Rina", "Pak Andi"});
        cbDosen.setPreferredSize(new Dimension(250, 28));
        formPanel.add(cbDosen, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        JLabel lblDate = new JLabel("Tanggal:");
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblDate, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        
        // Date Picker dengan kalender popup
        DatePicker datePicker = new DatePicker();
        formPanel.add(datePicker, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        JLabel lblTime = new JLabel("Waktu:");
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblTime, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        
        // Time Picker dengan dialog popup
        TimePicker timePicker = new TimePicker();
        formPanel.add(timePicker, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblDesc = new JLabel("Keperluan:");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblDesc, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.WEST;
        JTextArea txtDesc = new JTextArea(4, 20);
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scrollDesc = new JScrollPane(txtDesc);
        scrollDesc.setPreferredSize(new Dimension(250, 80));
        formPanel.add(scrollDesc, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 15, 15, 15);

        JButton btnSubmit = new JButton("Kirim Permintaan") {
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
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.setPreferredSize(new Dimension(180, 38));
        btnSubmit.setFocusPainted(false);
        btnSubmit.setBorderPainted(false);
        btnSubmit.setContentAreaFilled(false);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        formPanel.add(btnSubmit, gbc);
    }
}
