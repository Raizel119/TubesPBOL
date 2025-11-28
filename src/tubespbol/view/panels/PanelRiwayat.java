package tubespbol.view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;
import tubespbol.service.RequestService;

public class PanelRiwayat extends JPanel {

    private String nim;
    private RequestService requestService;

    // CONSTRUCTOR LENGKAP (Agar cocok dengan DashboardMahasiswa)
    public PanelRiwayat(String nim, Map<String, String> userData, RequestService rs) {
        this.nim = nim;
        this.requestService = rs;
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 245));

        JLabel title = new JLabel("Riwayat Pertemuan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(41, 128, 185));
        title.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 20));
        add(title, BorderLayout.NORTH);

        String[] columns = {"Tanggal", "Dosen", "Waktu", "Status", "Keterangan"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        add(scrollPane, BorderLayout.CENTER);
        
        // Load Data
        JButton btnRefresh = new JButton("Refresh");
        add(btnRefresh, BorderLayout.SOUTH);
        
        Runnable loadData = () -> {
            model.setRowCount(0);
            try {
                java.util.List<Map<String, String>> history = requestService.getRequestMahasiswa(nim);
                for(Map<String, String> h : history) {
                    String ket = h.get("status").equals("DITOLAK") ? h.get("alasan_penolakan") : h.get("keterangan_dosen");
                    model.addRow(new Object[]{
                        h.get("tanggal"),
                        h.get("nama_dosen"),
                        h.get("jam_mulai"),
                        h.get("status"),
                        ket
                    });
                }
            } catch (Exception e) {}
        };
        
        loadData.run();
        btnRefresh.addActionListener(e -> loadData.run());
    }
}