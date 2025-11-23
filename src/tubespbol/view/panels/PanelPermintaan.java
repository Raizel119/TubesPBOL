package tubespbol.view.panels;

import javax.swing.*;
import java.awt.*;

public class PanelPermintaan extends JPanel {

    public PanelPermintaan() {
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());

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
        JComboBox<String> cbDosen = new JComboBox<>(new String[]{
            "Dr. Budi", "Dr. Rina", "Pak Andi"
        });
        add(cbDosen, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Tanggal:"), gbc);
        gbc.gridx = 1;
        JTextField txtTanggal = new JTextField();
        add(txtTanggal, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Waktu:"), gbc);
        gbc.gridx = 1;
        JTextField txtWaktu = new JTextField();
        add(txtWaktu, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Keperluan:"), gbc);
        gbc.gridx = 1;
        JTextArea txtAlasan = new JTextArea(4, 20);
        add(new JScrollPane(txtAlasan), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton btnKirim = new JButton("Kirim Permintaan");
        btnKirim.setBackground(new Color(46, 134, 193));
        btnKirim.setForeground(Color.WHITE);
        btnKirim.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(btnKirim, gbc);
    }
}
