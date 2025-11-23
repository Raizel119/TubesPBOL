package tubespbol.view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelRiwayatDosen extends JPanel {

    public PanelRiwayatDosen() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Riwayat Pertemuan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(title, BorderLayout.NORTH);

        String[] cols = {"Mahasiswa", "Tanggal", "Keputusan"};
        Object[][] data = {
            {"Rafi", "10/11/2025", "Disetujui"},
            {"Fahri", "09/11/2025", "Ditolak"},};

        JTable table = new JTable(new DefaultTableModel(data, cols));
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
