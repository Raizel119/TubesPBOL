package tubespbol.view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

public class PanelPermintaanMasuk extends JPanel {
    
    // Komponen UI (Global agar bisa diakses)
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRefresh, btnSetujui, btnTolak, btnDetail;
    
    // Data (Disimpan sementara untuk keperluan UI seperti Detail)
    private ArrayList<Map<String, String>> currentDataList;

    public PanelPermintaanMasuk() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 245));

        // ========== HEADER ==========
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(236, 240, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 20));
        
        JLabel title = new JLabel("Permintaan Masuk");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(41, 128, 185));
        
        btnRefresh = new JButton("🔄 Refresh");
        styleButton(btnRefresh, new Color(52, 152, 219));
        
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // ========== TABLE ==========
        String[] cols = {"No", "Mahasiswa", "NIM", "Tanggal", "Waktu", "Keperluan"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(Color.BLACK);
        
        // Atur Lebar Kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(250);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 25, 15, 25));
        add(scrollPane, BorderLayout.CENTER);

        // ========== ACTION PANEL ==========
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        actionPanel.setBackground(new Color(236, 240, 245));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));
        
        btnSetujui = new JButton("✓ Setujui");
        styleButton(btnSetujui, new Color(39, 174, 96));
        
        btnTolak = new JButton("✗ Tolak");
        styleButton(btnTolak, new Color(231, 76, 60));
        
        btnDetail = new JButton("ℹ️ Detail");
        styleButton(btnDetail, new Color(52, 152, 219));

        actionPanel.add(btnSetujui);
        actionPanel.add(btnTolak);
        actionPanel.add(btnDetail);
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    private void styleButton(JButton btn, Color color) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 36));
    }

    // ========================================================
    // METODE UNTUK INTERAKSI DENGAN CONTROLLER
    // ========================================================

    // 1. Mendaftarkan Listener (Telinga)
    public void addRefreshListener(ActionListener listener) { btnRefresh.addActionListener(listener); }
    public void addSetujuiListener(ActionListener listener) { btnSetujui.addActionListener(listener); }
    public void addTolakListener(ActionListener listener) { btnTolak.addActionListener(listener); }
    
    // Detail bisa ditangani View sendiri (karena cuma baca data tabel) atau Controller. 
    // Kita biarkan View menangani Detail sederhana ini biar Controller fokus ke Bisnis Logic.
    public void addDetailListener(ActionListener listener) { btnDetail.addActionListener(listener); }

    // 2. Manipulasi Tabel
    public void updateTableData(ArrayList<Map<String, String>> data) {
        this.currentDataList = data;
        tableModel.setRowCount(0); // Bersihkan tabel
        
        if (data == null || data.isEmpty()) {
            tableModel.addRow(new Object[]{"", "Tidak ada permintaan masuk", "", "", "", ""});
        } else {
            int no = 1;
            for (Map<String, String> req : data) {
                String waktu = req.getOrDefault("jam_mulai", "-") + " - " + req.getOrDefault("jam_selesai", "-");
                String keperluan = req.getOrDefault("keperluan", "-");
                if (keperluan.length() > 50) keperluan = keperluan.substring(0, 47) + "...";

                tableModel.addRow(new Object[]{
                    no++, 
                    req.getOrDefault("nama_mahasiswa", "-"),
                    req.getOrDefault("nim", "-"),
                    req.getOrDefault("tanggal", "-"),
                    waktu,
                    keperluan
                });
            }
        }
    }

    // 3. Ambil Data Terpilih
    public Map<String, String> getSelectedData() {
        int row = table.getSelectedRow();
        if (row < 0 || currentDataList == null || row >= currentDataList.size()) {
            return null;
        }
        return currentDataList.get(row);
    }

    // 4. Dialog Helpers (Agar Controller tidak pusing urus UI)
    public String showInputDialog(String title, String message) {
        return JOptionPane.showInputDialog(this, message, title, JOptionPane.QUESTION_MESSAGE);
    }

    public boolean showConfirmDialog(String message) {
        int res = JOptionPane.showConfirmDialog(this, message, "Konfirmasi", JOptionPane.YES_NO_OPTION);
        return res == JOptionPane.YES_OPTION;
    }

    public void showMessage(String message, boolean isError) {
        JOptionPane.showMessageDialog(this, message, isError ? "Error" : "Sukses", 
                isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Fitur Detail (View Only)
    public void showDetailDialog(Map<String, String> req) {
        if (req == null) {
            showMessage("Pilih data terlebih dahulu!", true);
            return;
        }
        String detail = "=== DETAIL PERMINTAAN ===\n\n" +
                       "Mahasiswa: " + req.getOrDefault("nama_mahasiswa", "-") + "\n" +
                       "NIM: " + req.getOrDefault("nim", "-") + "\n" +
                       "Prodi: " + req.getOrDefault("prodi_mahasiswa", "-") + "\n\n" +
                       "Tanggal: " + req.getOrDefault("tanggal", "-") + "\n" +
                       "Waktu: " + req.getOrDefault("jam_mulai", "-") + " - " + req.getOrDefault("jam_selesai", "-") + "\n\n" +
                       "Keperluan:\n" + req.getOrDefault("keperluan", "-");
        
        JTextArea area = new JTextArea(detail);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Detail", JOptionPane.INFORMATION_MESSAGE);
    }
}