package tubespbol.view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import tubespbol.service.RequestService;

public class PanelRiwayat extends JPanel {

    private String nim;
    private RequestService requestService;
    
    // Tambahan: Variable untuk menyimpan data agar bisa dilihat detailnya
    private List<Map<String, String>> historyList;
    private JTable table;

    public PanelRiwayat(String nim, Map<String, String> userData, RequestService rs) {
        this.nim = nim;
        this.requestService = rs;
        this.historyList = new ArrayList<>();
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 245));

        // ========== HEADER ==========
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(236, 240, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 20));

        JLabel title = new JLabel("Riwayat Pertemuan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(41, 128, 185));
        
        JButton btnRefresh = new JButton("🔄 Refresh");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.setPreferredSize(new Dimension(100, 35));

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        // ========== TABLE ==========
        String[] columns = {"Tanggal", "Dosen", "Waktu", "Status", "Keterangan"};
        
        // Disable Edit agar tidak bisa diubah user
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(Color.BLACK);
        
        // Alignment & Coloring
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); 
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); 
        
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String status = (String) value;
                    if ("DITERIMA".equals(status)) c.setForeground(new Color(39, 174, 96));
                    else if ("DITOLAK".equals(status)) c.setForeground(new Color(231, 76, 60));
                    else if ("N/A".equals(status)) c.setForeground(new Color(230, 126, 34));
                    else c.setForeground(Color.BLACK);
                    setFont(getFont().deriveFont(Font.BOLD));
                }
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 25, 5, 25));
        add(scrollPane, BorderLayout.CENTER);
        
        // Info Panel di bawah
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(236, 240, 245));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 10, 25));
        JLabel lblInfo = new JLabel("💡 Klik 2x pada baris untuk melihat detail lengkap.");
        lblInfo.setForeground(Color.GRAY);
        infoPanel.add(lblInfo);
        add(infoPanel, BorderLayout.SOUTH);
        
        // ========== LOGIKA MOUSE LISTENER (DOUBLE CLICK) ==========
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    showDetail();
                }
            }
        });
        
        // ========== LOGIKA LOAD DATA ==========
        Runnable loadData = () -> {
            model.setRowCount(0);
            try {
                // Simpan ke variable global historyList
                historyList = requestService.getRequestMahasiswa(nim);
                
                for(Map<String, String> h : historyList) {
                    String ket = "";
                    String status = h.get("status");
                    
                    if ("DITOLAK".equals(status)) {
                        ket = h.get("alasan_penolakan");
                    } else if ("DITERIMA".equals(status)) {
                        ket = h.get("keterangan_dosen");
                    } else if ("N/A".equals(status)) {
                        ket = "Kadaluwarsa (Lewat Tanggal)";
                    }
                    
                    if (ket == null || ket.isEmpty()) ket = "-";

                    model.addRow(new Object[]{
                        h.get("tanggal"),
                        h.get("nama_dosen"),
                        h.get("jam_mulai") + " - " + h.get("jam_selesai"),
                        status,
                        ket
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        
        loadData.run();
        btnRefresh.addActionListener(e -> loadData.run());
    }
    
    // Method untuk menampilkan Popup Detail
    private void showDetail() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0 || historyList == null || selectedRow >= historyList.size()) return;
        
        Map<String, String> req = historyList.get(selectedRow);
        
        String status = req.get("status");
        String statusIcon = "DITERIMA".equals(status) ? "✓" : "DITOLAK".equals(status) ? "✗" : "⚠️";
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== DETAIL PENGAJUAN ===\n\n");
        sb.append("Dosen: ").append(req.get("nama_dosen")).append("\n");
        sb.append("Prodi: ").append(req.getOrDefault("prodi_dosen", "-")).append("\n\n");
        sb.append("Tanggal: ").append(req.get("tanggal")).append("\n");
        sb.append("Waktu: ").append(req.get("jam_mulai")).append(" - ").append(req.get("jam_selesai")).append("\n\n");
        sb.append("Status: ").append(statusIcon).append(" ").append(status).append("\n\n");
        sb.append("Keperluan:\n").append(req.get("keperluan")).append("\n\n");
        
        if ("DITERIMA".equals(status)) {
            sb.append("--------------------------\n");
            sb.append("Catatan Dosen:\n").append(req.get("keterangan_dosen"));
        } else if ("DITOLAK".equals(status)) {
            sb.append("--------------------------\n");
            sb.append("Alasan Penolakan:\n").append(req.get("alasan_penolakan"));
        } else if ("N/A".equals(status)) {
            sb.append("--------------------------\n");
            sb.append("CATATAN SISTEM:\n");
            sb.append("Permintaan ini otomatis kadaluwarsa karena tanggal pertemuan sudah terlewat\n");
            sb.append("tanpa ada respon dari dosen.");
        }
        
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scroll, "Detail Riwayat", JOptionPane.INFORMATION_MESSAGE);
    }
}