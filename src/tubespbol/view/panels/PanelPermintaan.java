package tubespbol.view.panels;

import javax.swing.*;
import java.awt.*;

public class PanelPermintaan extends JPanel {

    public PanelPermintaan() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Ajukan Permintaan Pertemuan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        add(new JLabel("Pilih Dosen:"), gbc);
        gbc.gridx = 1;

        JComboBox<String> cbDosen = new JComboBox<>(new String[]{"Dr. Budi", "Dr. Rina", "Pak Andi"});
        add(cbDosen, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Tanggal (dd/mm/yyyy):"), gbc);
        gbc.gridx = 1;

        JTextField txtDate = new JTextField();
        add(txtDate, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Waktu:"), gbc);
        gbc.gridx = 1;

        JTextField txtTime = new JTextField();
        add(txtTime, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Keperluan:"), gbc);
        gbc.gridx = 1;

        JTextArea txtDesc = new JTextArea(3, 20);
        add(new JScrollPane(txtDesc), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        JButton btnSubmit = new JButton("Kirim");
        btnSubmit.setBackground(new Color(46, 134, 193));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 16));

        add(btnSubmit, gbc);
    }
}
