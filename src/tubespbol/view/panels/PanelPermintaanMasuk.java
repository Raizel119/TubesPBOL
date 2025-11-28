package tubespbol.view.panels;

import tubespbol.service.RequestService;
import tubespbol.service.JadwalService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class PanelPermintaanMasuk extends JPanel {
    
    private String idDosen;
    private Map<String, String> userData;
    private RequestService requestService;
    private JadwalService jadwalService;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private ArrayList<Map<String, String>> requestList;
    
    public PanelPermintaanMasuk() {
        this(null, null, null, null);
    }
    
    public PanelPermintaanMasuk(String idDosen, Map<String, String> userData,
                                RequestService requestService, JadwalService jadwalService) {
        this.idDosen = idDosen;
        this.userData = userData;
        this.requestService = requestService;
        this.jadwalService = jadwalService;
        
        initComponents();
        
        if (requestService != null && idDosen != null) {
            loadRequests();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 245));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(236, 240, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 20));
        
        JLabel title = new JLabel("Permintaan Masuk");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(41, 128, 185));
        
        JButton btnRefresh = new JButton("🔄 Refresh");
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadRequests());
        
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        String[] cols = {"No", "Mahasiswa", "NIM", "Tanggal", "Waktu", "Keperluan"};
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
        
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(250);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 25, 15, 25));
        add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(new Color(236, 240, 245));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));
        actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        
        JButton btnSetujui = createStyledButton("✓ Setujui", new Color(39, 174, 96));
        btnSetujui.addActionListener(e -> setujuiRequest());
        
        JButton btnTolak = createStyledButton("✗ Tolak", new Color(231, 76, 60));
        btnTolak.addActionListener(e -> tolakRequest());
        
        JButton btnDetail = createStyledButton("ℹ️ Detail", new Color(52, 152, 219));
        btnDetail.addActionListener(e -> showDetail());

        actionPanel.add(btnSetujui);
        actionPanel.add(Box.createHorizontalStrut(15));
        actionPanel.add(btnTolak);
        actionPanel.add(btnDetail);

        add(actionPanel, BorderLayout.SOUTH);
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(120, 36));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(color);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }
    
    private void loadRequests() {
        if (requestService == null || idDosen == null) {
            System.out.println("RequestService atau idDosen null!");
            return;
        }
        
        // 1. BERSIHKAN DATA KADALUWARSA DULU
        new Thread(() -> {
            requestService.autoUpdateExpired(idDosen);
            
            // 2. SETELAH BERSIH, LOAD DATA
            SwingUtilities.invokeLater(() -> executeLoadWorker());
        }).start();
    }

    private void executeLoadWorker() {
        new SwingWorker<ArrayList<Map<String, String>>, Void>() {
            @Override
            protected ArrayList<Map<String, String>> doInBackground() {
                try {
                    // Ambil request yang tersisa (Hanya yang PENDING dan Belum Lewat)
                    // Fungsi getRequestDosen hanya akan mengembalikan yang statusnya belum diubah jadi N/A
                    Object result = requestService.getRequestDosen(idDosen);
                    if (result instanceof ArrayList) {
                        ArrayList<Map<String, String>> all = (ArrayList<Map<String, String>>) result;
                        ArrayList<Map<String, String>> pendingOnly = new ArrayList<>();
                        
                        // Filter manual untuk memastikan hanya PENDING yang muncul
                        for(Map<String, String> req : all) {
                            if("PENDING".equalsIgnoreCase(req.get("status"))) {
                                pendingOnly.add(req);
                            }
                        }
                        return pendingOnly;
                    }
                    return new ArrayList<>();
                } catch (Exception e) {
                    return new ArrayList<>();
                }
            }
            
            @Override
            protected void done() {
                try {
                    ArrayList<Map<String, String>> result = get();
                    requestList = (result != null) ? result : new ArrayList<>();
                    
                    tableModel.setRowCount(0);
                    
                    if (requestList.isEmpty()) {
                        tableModel.addRow(new Object[]{"", "Tidak ada permintaan masuk", "", "", "", ""});
                    } else {
                        int no = 1;
                        for (Map<String, String> req : requestList) {
                            String nama = req.getOrDefault("nama_mahasiswa", "-");
                            String nim = req.getOrDefault("nim", "-");
                            String tanggal = req.getOrDefault("tanggal", "-");
                            String jamMulai = req.getOrDefault("jam_mulai", "-");
                            String jamSelesai = req.getOrDefault("jam_selesai", "-");
                            String keperluan = req.getOrDefault("keperluan", "-");
                            
                            if (keperluan.length() > 50) {
                                keperluan = keperluan.substring(0, 47) + "...";
                            }
                            
                            tableModel.addRow(new Object[]{no++, nama, nim, tanggal, jamMulai + " - " + jamSelesai, keperluan});
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    
    private void showDetail() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0 || requestList == null || selectedRow >= requestList.size()) {
            JOptionPane.showMessageDialog(this, "Pilih permintaan terlebih dahulu!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Map<String, String> req = requestList.get(selectedRow);
        
        String detail = "=== DETAIL PERMINTAAN ===\n\n" +
                       "Mahasiswa: " + req.getOrDefault("nama_mahasiswa", "-") + "\n" +
                       "NIM: " + req.getOrDefault("nim", "-") + "\n" +
                       "Prodi: " + req.getOrDefault("prodi_mahasiswa", "-") + "\n\n" +
                       "Tanggal: " + req.getOrDefault("tanggal", "-") + "\n" +
                       "Waktu: " + req.getOrDefault("jam_mulai", "-") + " - " + req.getOrDefault("jam_selesai", "-") + "\n\n" +
                       "Keperluan:\n" + req.getOrDefault("keperluan", "-");
        
        JTextArea textArea = new JTextArea(detail);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Detail Permintaan", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void setujuiRequest() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0 || requestList == null || selectedRow >= requestList.size()) {
            JOptionPane.showMessageDialog(this, "Pilih permintaan!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Map<String, String> req = requestList.get(selectedRow);
        String keterangan = JOptionPane.showInputDialog(this, "Masukkan keterangan tambahan:", "Keterangan Dosen", JOptionPane.QUESTION_MESSAGE);
        if (keterangan == null) return;
        if (keterangan.trim().isEmpty()) keterangan = "Pertemuan disetujui";
        
        String idReq = req.get("id");
        String finalKeterangan = keterangan;
        
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return requestService.terimaRequest(idReq, finalKeterangan);
            }
            @Override
            protected void done() {
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(PanelPermintaanMasuk.this, "Permintaan disetujui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        loadRequests();
                    } else {
                        JOptionPane.showMessageDialog(PanelPermintaanMasuk.this, "Gagal menyetujui!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();
    }
    
    private void tolakRequest() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0 || requestList == null || selectedRow >= requestList.size()) {
            JOptionPane.showMessageDialog(this, "Pilih permintaan!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Map<String, String> req = requestList.get(selectedRow);
        String alasan = JOptionPane.showInputDialog(this, "Masukkan alasan penolakan:", "Alasan Penolakan", JOptionPane.QUESTION_MESSAGE);
        if (alasan == null) return;
        if (alasan.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Alasan tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idReq = req.get("id");
        
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return requestService.tolakRequest(idReq, alasan);
            }
            @Override
            protected void done() {
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(PanelPermintaanMasuk.this, "Permintaan ditolak.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        loadRequests();
                    } else {
                        JOptionPane.showMessageDialog(PanelPermintaanMasuk.this, "Gagal menolak!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();
    }
    
    public void refresh() {
        loadRequests();
    }
}