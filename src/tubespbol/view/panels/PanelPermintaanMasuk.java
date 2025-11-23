package tubespbol.view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelPermintaanMasuk extends JPanel {

    public PanelPermintaanMasuk() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Permintaan Masuk");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(title, BorderLayout.NORTH);

        String[] cols = {"Mahasiswa", "Tanggal", "Waktu", "Keperluan"};
        Object[][] data = {
            {"Rafi", "12/11/2025", "10:00", "Bimbingan"},
            {"Fahri", "13/11/2025", "14:00", "Revisi Tugas"},};

        JTable table = new JTable(new DefaultTableModel(data, cols));
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        JButton btnSetujui = new JButton("Setujui");
        JButton btnTolak = new JButton("Tolak");

        actionPanel.add(btnSetujui);
        actionPanel.add(btnTolak);

        add(actionPanel, BorderLayout.SOUTH);
    }
}
