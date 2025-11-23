package tubespbol.view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelJadwal extends JPanel {

    public PanelJadwal() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Jadwal Pertemuan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(title, BorderLayout.NORTH);

        String[] columns = {"Tanggal", "Waktu", "Dosen", "Status"};
        Object[][] data = {
            {"12/11/2025", "10:00", "Dr. Budi", "Approved"},
            {"13/11/2025", "13:00", "Dr. Rina", "Pending"},};

        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
    }
}
