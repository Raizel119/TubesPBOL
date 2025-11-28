package tubespbol.view.panels;

import tubespbol.service.JadwalService;
import tubespbol.view.components.DatePicker;
import tubespbol.view.components.TimePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class PanelJadwalDosen extends JPanel {
    
    private String idDosen;
    private Map<String, String> userData;
    private JadwalService jadwalService;
    
    private JTable tableJadwal;
    private DefaultTableModel tableModel;
    private DatePicker datePicker;
    
    // UPDATE: Gunakan 2 TimePicker untuk Mulai dan Selesai
    private TimePicker timePickerMulai;
    private TimePicker timePickerSelesai;
    
    private JTextArea txtCatatan;
    private JTextField txtJudul;
    
    public PanelJadwalDosen() {
        this(null, null, null);
    }
    
    public PanelJadwalDosen(String idDosen, Map<String, String> userData, JadwalService jadwalService) {
        this.idDosen = idDosen;
        this.userData = userData;
        this.jadwalService = jadwalService;
        
        initComponents();
        
        if (jadwalService != null && idDosen != null) {
            loadJadwalToday();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(236, 240, 245));

        // ========== HEADER ==========
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(236, 240, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 20));
        
        JLabel title = new JLabel("Atur Jadwal Ketersediaan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(41, 128, 185));
        
        JButton btnRefresh = new JButton("🔄 Refresh Jadwal");
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadJadwalToday());
        
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        // ========== MAIN CONTENT (SPLIT) ==========
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 15, 0));
        mainContent.setBackground(new Color(236, 240, 245));
        mainContent.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        
        // LEFT: Form Blok Waktu
        mainContent.add(createFormPanel());
        
        // RIGHT: Jadwal Hari Ini
        mainContent.add(createJadwalPanel());
        
        add(mainContent, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Blok Waktu (Tambah Agenda)",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(41, 128, 185)
            ),
            BorderFactory.createEmptyBorder(10, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Tanggal
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        JLabel lblTanggal = new JLabel("Tanggal:");
        lblTanggal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblTanggal, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        datePicker = new DatePicker();
        formPanel.add(datePicker, gbc);

        // Judul Agenda
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0.3;
        JLabel lblJudul = new JLabel("Judul:");
        lblJudul.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblJudul, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtJudul = new JTextField();
        txtJudul.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtJudul.setPreferredSize(new Dimension(0, 30));
        formPanel.add(txtJudul, gbc);

        // Jam Mulai (UPDATE: Pakai TimePicker 1)
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0.3;
        JLabel lblMulai = new JLabel("Jam Mulai:");
        lblMulai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblMulai, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        timePickerMulai = new TimePicker();
        // Set default jam 08:00
        timePickerMulai.setTime(8, 0); 
        formPanel.add(timePickerMulai, gbc);

        // Jam Selesai (UPDATE: Pakai TimePicker 2)
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0.3;
        JLabel lblSelesai = new JLabel("Jam Selesai:");
        lblSelesai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblSelesai, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        timePickerSelesai = new TimePicker();
        // Set default jam 09:00
        timePickerSelesai.setTime(9, 0);
        formPanel.add(timePickerSelesai, gbc);

        // Catatan
        gbc.gridx = 0; gbc.gridy++; gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblCatatan = new JLabel("Keterangan:");
        lblCatatan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblCatatan, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.WEST;
        txtCatatan = new JTextArea(3, 20);
        txtCatatan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCatatan.setLineWrap(true);
        txtCatatan.setWrapStyleWord(true);
        JScrollPane scrollCatatan = new JScrollPane(txtCatatan);
        scrollCatatan.setPreferredSize(new Dimension(200, 70));
        formPanel.add(scrollCatatan, gbc);

        // Button Simpan
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 10, 10, 10);
        JButton btnSimpan = createStyledButton("💾 Simpan Agenda", new Color(52, 152, 219));
        btnSimpan.addActionListener(e -> simpanAgenda());
        formPanel.add(btnSimpan, gbc);
        
        return formPanel;
    }
    
    private JPanel createJadwalPanel() {
        JPanel jadwalPanel = new JPanel(new BorderLayout(0, 10));
        jadwalPanel.setBackground(Color.WHITE);
        jadwalPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Jadwal Hari Ini (Tidak Tersedia untuk Mahasiswa)",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(41, 128, 185)
            ),
            BorderFactory.createEmptyBorder(10, 15, 15, 15)
        ));
        
        // Table
        String[] cols = {"Tipe", "Jam", "Keterangan"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableJadwal = new JTable(tableModel);
        tableJadwal.setRowHeight(28);
        tableJadwal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableJadwal.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableJadwal.getTableHeader().setBackground(new Color(41, 128, 185));
        tableJadwal.getTableHeader().setForeground(Color.WHITE);
        tableJadwal.setSelectionBackground(new Color(174, 214, 241));
        tableJadwal.setSelectionForeground(Color.BLACK);
        tableJadwal.setGridColor(new Color(220, 220, 220));
        
        // Column widths
        tableJadwal.getColumnModel().getColumn(0).setPreferredWidth(80);
        tableJadwal.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableJadwal.getColumnModel().getColumn(2).setPreferredWidth(200);
        
        JScrollPane scrollPane = new JScrollPane(tableJadwal);
        jadwalPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Info label
        JLabel lblInfo = new JLabel("<html><small>💡 Jadwal yang ditampilkan adalah waktu TIDAK TERSEDIA</small></html>");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInfo.setForeground(new Color(120, 120, 120));
        lblInfo.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        jadwalPanel.add(lblInfo, BorderLayout.SOUTH);
        
        return jadwalPanel;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gp = new GradientPaint(
                    0, 0, color,
                    getWidth(), getHeight(), color.darker()
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(160, 36));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    // ========== DATABASE OPERATIONS ==========
    
    private void loadJadwalToday() {
        if (jadwalService == null || idDosen == null) return;
        
        SwingWorker<List<Map<String, String>>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Map<String, String>> doInBackground() {
                LocalDate today = LocalDate.now();
                String tanggal = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return jadwalService.getJadwalLengkap(idDosen, tanggal);
            }
            
            @Override
            protected void done() {
                try {
                    List<Map<String, String>> jadwal = get();
                    
                    // Clear table
                    tableModel.setRowCount(0);
                    
                    if (jadwal.isEmpty()) {
                        tableModel.addRow(new Object[]{"", "Tidak ada jadwal", "Semua waktu tersedia"});
                    } else {
                        for (Map<String, String> item : jadwal) {
                            String tipe = item.get("tipe");
                            String jam = item.get("jam_mulai") + " - " + item.get("jam_selesai");
                            String keterangan = item.get("keterangan");
                            
                            tableModel.addRow(new Object[]{tipe, jam, keterangan});
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(PanelJadwalDosen.this,
                        "Gagal memuat jadwal: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
    
    private void simpanAgenda() {
        if (jadwalService == null || idDosen == null) {
            JOptionPane.showMessageDialog(this,
                "Service tidak tersedia!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 1. Ambil Tanggal (Konversi Date -> String)
        Date dateObj = datePicker.getDate();
        if (dateObj == null) {
            JOptionPane.showMessageDialog(this,
                "Pilih tanggal terlebih dahulu!",
                "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = sdf.format(dateObj);
        
        // 2. Ambil Input Lain
        String judul = txtJudul.getText().trim();
        String keterangan = txtCatatan.getText().trim();
        
        if (judul.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Masukkan judul agenda!",
                "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 3. Ambil Waktu dari 2 TimePicker yang berbeda
        // TimePicker mengembalikan "08:00", kita tambah ":00" untuk format SQL
        String jamMulai = timePickerMulai.getTime() + ":00";
        String jamSelesai = timePickerSelesai.getTime() + ":00";
        
        // Validasi Jam Mulai < Jam Selesai
        if (jamMulai.compareTo(jamSelesai) >= 0) {
            JOptionPane.showMessageDialog(this,
                "Jam Mulai harus lebih kecil dari Jam Selesai!",
                "Validasi Waktu", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Konfirmasi
        int confirm = JOptionPane.showConfirmDialog(this,
            "Simpan agenda berikut?\n\n" +
            "Tanggal: " + tanggal + "\n" +
            "Judul: " + judul + "\n" +
            "Waktu: " + timePickerMulai.getTime() + " - " + timePickerSelesai.getTime() + "\n" +
            "Keterangan: " + keterangan + "\n\n" +
            "⚠️ Waktu ini akan TERBLOK dan tidak tersedia untuk mahasiswa.",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) return;
        
        // Save to database
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                return jadwalService.tambahAgenda(idDosen, tanggal, jamMulai, jamSelesai, judul, keterangan);
            }
            
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        JOptionPane.showMessageDialog(PanelJadwalDosen.this,
                            "✓ Agenda berhasil disimpan!\nWaktu tersebut sekarang TIDAK TERSEDIA untuk mahasiswa.",
                            "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        
                        // Clear form
                        txtJudul.setText("");
                        txtCatatan.setText("");
                        
                        // Reload jadwal
                        loadJadwalToday();
                    } else {
                        JOptionPane.showMessageDialog(PanelJadwalDosen.this,
                            "Gagal menyimpan agenda!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(PanelJadwalDosen.this,
                        "Error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }
    
    public void refresh() {
        loadJadwalToday();
    }
}