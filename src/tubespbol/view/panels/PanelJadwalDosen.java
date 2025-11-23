package tubespbol.view.panels;

import javax.swing.*;
import java.awt.*;

public class PanelJadwalDosen extends JPanel {

    public PanelJadwalDosen() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Atur Jadwal Ketersediaan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        add(new JLabel("Tanggal:"), gbc);
        gbc.gridx = 1;
        JTextField txtTanggal = new JTextField();
        add(txtTanggal, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Waktu Available:"), gbc);
        gbc.gridx = 1;
        JTextField txtWaktu = new JTextField();
        add(txtWaktu, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Catatan Tambahan:"), gbc);
        gbc.gridx = 1;
        JTextArea txtCatatan = new JTextArea(4, 20);
        add(new JScrollPane(txtCatatan), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton btnSimpan = new JButton("Simpan Ketersediaan");
        btnSimpan.setBackground(new Color(46, 134, 193));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 16));

        add(btnSimpan, gbc);
    }
}
