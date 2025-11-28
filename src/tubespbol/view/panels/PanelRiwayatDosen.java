package tubespbol.view.panels;

import tubespbol.service.RequestService;
import tubespbol.model.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class PanelRiwayatDosen extends JPanel {
    
    private String idDosen;
    private Map<String, String> userData;
    private RequestService requestService;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbFilter;
    private ArrayList<Map<String, String>> riwayatList;
    
    // Constructor LAMA
    public PanelRiwayatDosen() {
        this(null, null, null);
    }
    
    // Constructor BARU
    public PanelRiwayatDosen(String idDosen, Map<String, String> userData, RequestService requestService) {
        this.idDosen = idDosen;
        this.userData = userData;
        this.requestService = requestService;
        
        initComponents();
        
        if (requestService != null && idDosen != null) {
            loadRiwayat("SEMUA");
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 245));

        // ========== HEADER ==========
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(236, 240, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 20));
        
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftHeader.setBackground(new Color(236, 240, 245));
        
        JLabel title = new JLabel("Riwayat Pertemuan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(41, 128, 185));
        
        JLabel lblFilter = new JLabel("Filter:");
        lblFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        cmbFilter = new JComboBox<>(new String[]{"SEMUA", "DITERIMA", "DITOLAK", "N/A"});
        cmbFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbFilter.setPreferredSize(new Dimension(120, 30));
        cmbFilter.addActionListener(e -> {
            String filter = (String) cmbFilter.getSelectedItem();
            loadRiwayat(filter);
        });
        
        leftHeader.add(title);
        leftHeader.add(Box.createHorizontalStrut(20));
        leftHeader.add(lblFilter);
        leftHeader.add(cmbFilter);
        
        JButton btnRefresh = new JButton("🔄 Refresh");
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadRiwayat((String) cmbFilter.getSelectedItem()));
        
        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // ========== TABLE ==========
        String[] cols = {"No", "Mahasiswa", "NIM", "Tanggal", "Waktu", "Status", "Keterangan"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(220, 220, 220));
        
        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(40);  // No
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Nama
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // NIM
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Tanggal
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Waktu
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Status
        table.getColumnModel().getColumn(6).setPreferredWidth(250); // Keterangan
        
        // Custom cell renderer untuk status
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    String status = value != null ? value.toString() : "";
                    if (status.equals("DITERIMA")) {
                        c.setForeground(new Color(39, 174, 96)); // Green
                        setFont(getFont().deriveFont(Font.BOLD));
                    } else if (status.equals("DITOLAK")) {
                        c.setForeground(new Color(231, 76, 60)); // Red
                        setFont(getFont().deriveFont(Font.BOLD));
                    } else if (status.equals("N/A")) {
                        c.setForeground(new Color(230, 126, 34)); // Orange
                        setFont(getFont().deriveFont(Font.BOLD));
                    } else {
                        c.setForeground(Color.BLACK);
                        setFont(getFont().deriveFont(Font.PLAIN));
                    }
                }
                
                setHorizontalAlignment(CENTER);
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 25, 15, 25));
        add(scrollPane, BorderLayout.CENTER);
        
        // ========== INFO PANEL ==========
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(236, 240, 245));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 25, 15, 25));
        
        JLabel lblInfo = new JLabel("<html><small>💡 Klik row untuk melihat detail lengkap | Status N/A = Permintaan lewat tanggal tanpa tindakan</small></html>");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInfo.setForeground(new Color(120, 120, 120));
        
        infoPanel.add(lblInfo);
        add(infoPanel, BorderLayout.SOUTH);
        
        // Double click untuk detail
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    showDetail();
                }
            }
        });
    }
    
    // ========== DATABASE OPERATIONS ==========
    
    private void loadRiwayat(String filter) {
        if (idDosen == null) return;
        
        SwingWorker<ArrayList<Map<String, String>>, Void> worker = new SwingWorker<ArrayList<Map<String, String>>, Void>() {
            @Override
            protected ArrayList<Map<String, String>> doInBackground() {
                ArrayList<Map<String, String>> riwayat = new ArrayList<>();
                Connection conn = null;
                
                try {
                    conn = Database.getConnection();
                    if (conn == null) return riwayat;
                    
                    String query = "SELECT r.*, m.nama as nama_mahasiswa, m.prodi as prodi_mahasiswa " +
                                  "FROM request_pertemuan r " +
                                  "JOIN master_mahasiswa m ON r.nim = m.nim " +
                                  "WHERE r.id_dosen = ? ";
                    
                    // Add filter
                    if (!filter.equals("SEMUA")) {
                        query += "AND r.status = ? ";
                    }
                    
                    query += "ORDER BY r.tanggal DESC, r.jam_mulai DESC";
                    
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, idDosen);
                    
                    if (!filter.equals("SEMUA")) {
                        stmt.setString(2, filter);
                    }
                    
                    ResultSet rs = stmt.executeQuery();
                    
                    while (rs.next()) {
                        Map<String, String> req = new HashMap<>();
                        req.put("id", String.valueOf(rs.getInt("id")));
                        req.put("nim", rs.getString("nim"));
                        req.put("nama_mahasiswa", rs.getString("nama_mahasiswa"));
                        req.put("prodi_mahasiswa", rs.getString("prodi_mahasiswa"));
                        req.put("tanggal", rs.getString("tanggal"));
                        req.put("jam_mulai", rs.getString("jam_mulai"));
                        req.put("jam_selesai", rs.getString("jam_selesai"));
                        req.put("keperluan", rs.getString("keperluan"));
                        req.put("status", rs.getString("status"));
                        req.put("alasan_penolakan", rs.getString("alasan_penolakan") != null ? rs.getString("alasan_penolakan") : "");
                        req.put("keterangan_dosen", rs.getString("keterangan_dosen") != null ? rs.getString("keterangan_dosen") : "");
                        riwayat.add(req);
                    }
                    
                    rs.close();
                    stmt.close();
                    
                    System.out.println("Total riwayat loaded: " + riwayat.size());
                    
                } catch (SQLException e) {
                    System.err.println("Error loading riwayat: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    Database.closeConnection(conn);
                }
                
                return riwayat;
            }
            
            @Override
            protected void done() {
                try {
                    ArrayList<Map<String, String>> result = get();
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    riwayatList = result;
                    
                    // Clear table
                    tableModel.setRowCount(0);
                    
                    if (riwayatList.isEmpty()) {
                        tableModel.addRow(new Object[]{"", "Tidak ada riwayat", "", "", "", "", ""});
                    } else {
                        int no = 1;
                        for (Map<String, String> req : riwayatList) {
                            String nama = req.get("nama_mahasiswa");
                            String nim = req.get("nim");
                            String tanggal = req.get("tanggal");
                            String waktu = req.get("jam_mulai") + " - " + req.get("jam_selesai");
                            String status = req.get("status");
                            
                            // Determine keterangan
                            String keterangan = "";
                            if ("DITERIMA".equals(status)) {
                                keterangan = req.get("keterangan_dosen");
                            } else if ("DITOLAK".equals(status)) {
                                keterangan = req.get("alasan_penolakan");
                            } else if ("N/A".equals(status)) {
                                keterangan = "Permintaan melewati tanggal yang ditentukan";
                            }
                            
                            // Truncate keterangan
                            if (keterangan != null && keterangan.length() > 50) {
                                keterangan = keterangan.substring(0, 47) + "...";
                            }
                            
                            tableModel.addRow(new Object[]{no++, nama, nim, tanggal, waktu, status, keterangan});
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error done: " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(PanelRiwayatDosen.this,
                        "Gagal memuat riwayat: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
    
    private void showDetail() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Pilih riwayat terlebih dahulu!",
                "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (riwayatList == null || riwayatList.isEmpty() || selectedRow >= riwayatList.size()) {
            JOptionPane.showMessageDialog(this,
                "Data tidak ditemukan!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Map<String, String> req = riwayatList.get(selectedRow);
        
        String status = req.get("status");
        String statusIcon = "DITERIMA".equals(status) ? "✓" : "DITOLAK".equals(status) ? "✗" : "⚠️";
        
        StringBuilder detail = new StringBuilder();
        detail.append("=== DETAIL RIWAYAT ===\n\n");
        detail.append("Mahasiswa: ").append(req.get("nama_mahasiswa")).append("\n");
        detail.append("NIM: ").append(req.get("nim")).append("\n");
        detail.append("Prodi: ").append(req.get("prodi_mahasiswa")).append("\n\n");
        detail.append("Tanggal: ").append(req.get("tanggal")).append("\n");
        detail.append("Waktu: ").append(req.get("jam_mulai")).append(" - ").append(req.get("jam_selesai")).append("\n\n");
        detail.append("Status: ").append(statusIcon).append(" ").append(status).append("\n\n");
        detail.append("Keperluan:\n").append(req.get("keperluan")).append("\n\n");
        
        if ("DITERIMA".equals(status)) {
            String ket = req.get("keterangan_dosen");
            if (ket != null && !ket.isEmpty()) {
                detail.append("Keterangan Dosen:\n").append(ket);
            }
        } else if ("DITOLAK".equals(status)) {
            String alasan = req.get("alasan_penolakan");
            if (alasan != null && !alasan.isEmpty()) {
                detail.append("Alasan Penolakan:\n").append(alasan);
            }
        } else if ("N/A".equals(status)) {
            detail.append("CATATAN:\n");
            detail.append("Permintaan ini telah melewati tanggal yang ditentukan.\n");
            detail.append("Tidak ada tindakan yang dilakukan (tidak disetujui maupun ditolak).\n");
            detail.append("Status otomatis diubah menjadi N/A oleh sistem.");
        }
        
        JTextArea textArea = new JTextArea(detail.toString());
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new java.awt.Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 320));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Detail Riwayat", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void refresh() {
        String currentFilter = (String) cmbFilter.getSelectedItem();
        if (currentFilter == null) currentFilter = "SEMUA";
        loadRiwayat(currentFilter);
    }
}
