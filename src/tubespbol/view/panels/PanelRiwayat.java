package tubespbol.view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelRiwayat extends JPanel {

    public PanelRiwayat() {
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 245));

        JLabel title = new JLabel("Riwayat Pertemuan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(41, 128, 185));
        title.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 20));

        add(title, BorderLayout.NORTH);

        String[] columns = {"Tanggal", "Dosen", "Status"};
        Object[][] rows = {
            {"10/11/2025", "Dr. Budi", "Approved"},
            {"12/11/2025", "Dr. Rina", "Rejected"}
        };

        JTable table = new JTable(new DefaultTableModel(rows, columns));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(220, 220, 220));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        add(scrollPane, BorderLayout.CENTER);
    }
}
