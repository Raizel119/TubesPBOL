package tubespbol.view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelRiwayat extends JPanel {

    public PanelRiwayat() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Riwayat Pertemuan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(title, BorderLayout.NORTH);

        String[] columns = {"Tanggal", "Dosen", "Status"};
        Object[][] rows = {
            {"10/11/2025", "Dr. Budi", "Approved"},
            {"12/11/2025", "Dr. Rina", "Rejected"}
        };

        JTable table = new JTable(new DefaultTableModel(rows, columns));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
