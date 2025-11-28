package tubespbol.view.panels;

import tubespbol.service.RequestService;
import tubespbol.service.JadwalService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

        // ========== HEADER ==========
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

        // ========== TABLE ==========
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
        
        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(40);  // No
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Nama
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // NIM
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Tanggal
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Waktu
        table.getColumnModel().getColumn(5).setPreferredWidth(250); // Keperluan

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 25, 15, 25));
        add(scrollPane, BorderLayout.CENTER);

        // ========== ACTION PANEL ==========
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
    
    // ========== HELPER METHOD ==========
    
    /**
     * Cek apakah tanggal dan waktu sudah lewat
     */
    private boolean isTanggalLewat(String tanggal, String jamSelesai) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            
            Date tanggalDate = dateFormat.parse(tanggal);
            Date jamSelesaiDate = timeFormat.parse(jamSelesai);
            
            // Gabung tanggal dan jam
            Calendar calTanggal = Calendar.getInstance();
            calTanggal.setTime(tanggalDate);
            
            Calendar calJam = Calendar.getInstance();
            calJam.setTime(jamSelesaiDate);
            
            calTanggal.set(Calendar.HOUR_OF_DAY, calJam.get(Calendar.HOUR_OF_DAY));
            calTanggal.set(Calendar.MINUTE, calJam.get(Calendar.MINUTE));
            calTanggal.set(Calendar.SECOND, 0);
            
            Date tanggalWaktuSelesai = calTanggal.getTime();
            Date sekarang = new Date();
            
            return sekarang.after(tanggalWaktuSelesai);
        } catch (Exception e) {
            System.out.println("Error cek tanggal: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Filter hanya permintaan yang tanggalnya belum lewat
     */
    private ArrayList<Map<String, String>> filterPermintaanAktif(ArrayList<Map<String, String>> allRequests) {
        ArrayList<Map<String, String>> activeRequests = new ArrayList<>();
        
        if (allRequests == null || allRequests.isEmpty()) {
            return activeRequests;
        }
        
        for (Map<String, String> req : allRequests) {
            String tanggal = req.get("tanggal");
            String jamSelesai = req.get("jam_selesai");
            String status = req.get("status");
            String idReq = req.get("id");
            
            // Validasi data ada
            if (tanggal == null || jamSelesai == null || status == null || idReq == null) {
                System.out.println("Data permintaan tidak lengkap, skip.");
                continue;
            }
            
            // Tampilkan hanya yang status "PENDING" dan tanggal belum lewat
            if (status.equalsIgnoreCase("PENDING")) {
                if (!isTanggalLewat(tanggal, jamSelesai)) {
                    // Tanggal belum lewat, masukkan ke list aktif
                    activeRequests.add(req);
                } else {
                    // Tanggal sudah lewat, update status ke NA di background
                    updateToHistoryWithNA(idReq);
                }
            }
        }
        
        return activeRequests;
    }
    
    /**
     * Update permintaan yang lewat tanggal ke riwayat dengan status N/A
     */
    private void updateToHistoryWithNA(String requestId) {
        if (requestService == null) return;
        
        new Thread(() -> {
            try {
                requestService.updateStatusToNA(requestId);
                System.out.println("Status updated to NA for request: " + requestId);
            } catch (Exception e) {
                System.out.println("Error update status NA: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
    
    // ========== DATABASE OPERATIONS ==========
    
    private void loadRequests() {
        if (requestService == null || idDosen == null) {
            System.out.println("RequestService atau idDosen null!");
            return;
        }
        
        new SwingWorker<ArrayList<Map<String, String>>, Void>() {
            @Override
            protected ArrayList<Map<String, String>> doInBackground() {
                try {
                    Object result = requestService.getRequestDosen(idDosen);
                    if (result instanceof ArrayList) {
                        return (ArrayList<Map<String, String>>) result;
                    }
                    return new ArrayList<>();
                } catch (Exception e) {
                    System.out.println("Error load requests: " + e.getMessage());
                    e.printStackTrace();
                    return new ArrayList<>();
                }
            }
            
            @Override
            protected void done() {
                try {
                    ArrayList<Map<String, String>> allRequests = get();
                    if (allRequests == null) {
                        allRequests = new ArrayList<>();
                    }
                    
                    // Filter hanya permintaan aktif (belum lewat tanggal)
                    requestList = filterPermintaanAktif(allRequests);
                    
                    // Clear table
                    tableModel.setRowCount(0);
                    
                    if (requestList == null || requestList.isEmpty()) {
                        tableModel.addRow(new Object[]{"", "Tidak ada permintaan masuk", "", "", "", ""});
                    } else {
                        int no = 1;
                        for (Map<String, String> req : requestList) {
                            String nama = req.get("nama_mahasiswa");
                            String nim = req.get("nim");
                            String tanggal = req.get("tanggal");
                            String jamMulai = req.get("jam_mulai");
                            String jamSelesai = req.get("jam_selesai");
                            String keperluan = req.get("keperluan");
                            
                            // Validasi data
                            if (nama == null) nama = "-";
                            if (nim == null) nim = "-";
                            if (tanggal == null) tanggal = "-";
                            if (jamMulai == null) jamMulai = "-";
                            if (jamSelesai == null) jamSelesai = "-";
                            if (keperluan == null) keperluan = "-";
                            
                            String waktu = jamMulai + " - " + jamSelesai;
                            
                            // Truncate keperluan jika terlalu panjang
                            if (keperluan.length() > 50) {
                                keperluan = keperluan.substring(0, 47) + "...";
                            }
                            
                            tableModel.addRow(new Object[]{no++, nama, nim, tanggal, waktu, keperluan});
                        }
                    }
                    
                    System.out.println("Total permintaan aktif: " + (requestList != null ? requestList.size() : 0));
                    
                } catch (Exception e) {
                    System.out.println("Error done loadRequests: " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(PanelPermintaanMasuk.this,
                        "Gagal memuat permintaan: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
    
    private void showDetail() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Pilih permintaan terlebih dahulu!",
                "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (requestList == null || requestList.isEmpty() || selectedRow >= requestList.size()) {
            JOptionPane.showMessageDialog(this,
                "Data tidak ditemukan!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Map<String, String> req = requestList.get(selectedRow);
        
        String namaMahasiswa = req.getOrDefault("nama_mahasiswa", "-");
        String nim = req.getOrDefault("nim", "-");
        String prodi = req.getOrDefault("prodi_mahasiswa", "-");
        String tanggal = req.getOrDefault("tanggal", "-");
        String jamMulai = req.getOrDefault("jam_mulai", "-");
        String jamSelesai = req.getOrDefault("jam_selesai", "-");
        String keperluan = req.getOrDefault("keperluan", "-");
        
        String detail = "=== DETAIL PERMINTAAN ===\n\n" +
                       "Mahasiswa: " + namaMahasiswa + "\n" +
                       "NIM: " + nim + "\n" +
                       "Prodi: " + prodi + "\n\n" +
                       "Tanggal: " + tanggal + "\n" +
                       "Waktu: " + jamMulai + " - " + jamSelesai + "\n\n" +
                       "Keperluan:\n" + keperluan;
        
        JTextArea textArea = new JTextArea(detail);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 250));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Detail Permintaan", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void setujuiRequest() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Pilih permintaan terlebih dahulu!",
                "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (requestList == null || requestList.isEmpty() || selectedRow >= requestList.size()) {
            JOptionPane.showMessageDialog(this,
                "Data tidak ditemukan!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Map<String, String> req = requestList.get(selectedRow);
        String namaMahasiswa = req.getOrDefault("nama_mahasiswa", "-");
        String tanggal = req.getOrDefault("tanggal", "-");
        String jamMulai = req.getOrDefault("jam_mulai", "-");
        
        // Input keterangan dari dosen
        String keterangan = JOptionPane.showInputDialog(this,
            "Masukkan keterangan tambahan (lokasi/catatan):\n" +
            "Mahasiswa: " + namaMahasiswa + "\n" +
            "Waktu: " + tanggal + " " + jamMulai,
            "Keterangan Dosen",
            JOptionPane.QUESTION_MESSAGE);
        
        if (keterangan == null) return; // Cancelled
        
        if (keterangan.trim().isEmpty()) {
            keterangan = "Pertemuan disetujui";
        }
        
        String idReq = req.get("id");
        if (idReq == null) {
            JOptionPane.showMessageDialog(this,
                "ID Permintaan tidak ditemukan!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String finalKeterangan = keterangan;
        
        // Save to database
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                try {
                    return requestService.terimaRequest(idReq, finalKeterangan);
                } catch (Exception e) {
                    System.out.println("Error terima request: " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }
            
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        JOptionPane.showMessageDialog(PanelPermintaanMasuk.this,
                            "✓ Permintaan berhasil disetujui!\n" +
                            "Mahasiswa akan menerima notifikasi.\n\n" +
                            "⚠️ Jadwal otomatis terblok di sistem.",
                            "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        loadRequests(); // Reload
                    } else {
                        JOptionPane.showMessageDialog(PanelPermintaanMasuk.this,
                            "Gagal menyetujui permintaan!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    System.out.println("Error done setujui: " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(PanelPermintaanMasuk.this,
                        "Error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
    
    private void tolakRequest() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Pilih permintaan terlebih dahulu!",
                "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (requestList == null || requestList.isEmpty() || selectedRow >= requestList.size()) {
            JOptionPane.showMessageDialog(this,
                "Data tidak ditemukan!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Map<String, String> req = requestList.get(selectedRow);
        String namaMahasiswa = req.getOrDefault("nama_mahasiswa", "-");
        String tanggal = req.getOrDefault("tanggal", "-");
        String jamMulai = req.getOrDefault("jam_mulai", "-");
        
        // Input alasan penolakan
        String alasan = JOptionPane.showInputDialog(this,
            "Masukkan alasan penolakan:\n" +
            "Mahasiswa: " + namaMahasiswa + "\n" +
            "Waktu: " + tanggal + " " + jamMulai,
            "Alasan Penolakan",
            JOptionPane.QUESTION_MESSAGE);
        
        if (alasan == null) return; // Cancelled
        
        if (alasan.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Alasan penolakan tidak boleh kosong!",
                "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idReq = req.get("id");
        if (idReq == null) {
            JOptionPane.showMessageDialog(this,
                "ID Permintaan tidak ditemukan!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Confirm
        int confirm = JOptionPane.showConfirmDialog(this,
            "Tolak permintaan dari " + namaMahasiswa + "?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) return;
        
        // Save to database
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                try {
                    return requestService.tolakRequest(idReq, alasan);
                } catch (Exception e) {
                    System.out.println("Error tolak request: " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }
            
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        JOptionPane.showMessageDialog(PanelPermintaanMasuk.this,
                            "✓ Permintaan berhasil ditolak.\nMahasiswa akan menerima notifikasi dengan alasan penolakan.",
                            "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        loadRequests(); // Reload
                    } else {
                        JOptionPane.showMessageDialog(PanelPermintaanMasuk.this,
                            "Gagal menolak permintaan!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    System.out.println("Error done tolak: " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(PanelPermintaanMasuk.this,
                        "Error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
    
    public void refresh() {
        loadRequests();
    }
}