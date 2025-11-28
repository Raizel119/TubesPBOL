package tubespbol.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import tubespbol.model.Database;
import tubespbol.service.JadwalService;

public class FormKelolaJadwalDosen extends JFrame {
    private JPanel panelUtama;
    private JSpinner spinnerTanggal;
    private JSpinner spinnerJamMulai;
    private JSpinner spinnerJamSelesai;
    private JTextField textFieldJudul;
    private JTextArea textAreaKeterangan;
    private JButton btnSimpan, btnHapus, btnReset;
    private JTable tableJadwal;
    private DefaultTableModel tableModel;
    
    private JadwalService jadwalService;
    private String dosenId;
    
    public FormKelolaJadwalDosen(String dosenId) {
        this.dosenId = dosenId;
        this.jadwalService = new JadwalService();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Atur Jadwal Ketersediaan");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Main Panel
        panelUtama = new JPanel();
        panelUtama.setLayout(new BorderLayout(15, 15));
        panelUtama.setBackground(new Color(245, 245, 245));
        panelUtama.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Panel Judul - Responsif
        JPanel panelJudul = createPanelJudul();
        
        // Panel Input Form
        JPanel panelForm = createPanelForm();
        
        // Panel Tabel
        JPanel panelTabel = createPanelTabel();
        
        // Panel Tombol
        JPanel panelTombol = createPanelTombol();
        
        // Layout dengan split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(panelForm);
        splitPane.setRightComponent(panelTabel);
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.35);
        
        // Layout
        panelUtama.add(panelJudul, BorderLayout.NORTH);
        panelUtama.add(splitPane, BorderLayout.CENTER);
        panelUtama.add(panelTombol, BorderLayout.SOUTH);
        
        add(panelUtama);
        setVisible(true);
        
        loadDataJadwal();
    }
    
    private JPanel createPanelJudul() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(41, 128, 185));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel labelJudul = new JLabel("ATUR JADWAL KETERSEDIAAN");
        labelJudul.setFont(new Font("Arial", Font.BOLD, 24));
        labelJudul.setForeground(Color.WHITE);
        labelJudul.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel labelDeskripsi = new JLabel("Kelola jadwal dan agenda ketersediaan Anda untuk pertemuan dengan mahasiswa");
        labelDeskripsi.setFont(new Font("Arial", Font.PLAIN, 12));
        labelDeskripsi.setForeground(new Color(230, 230, 230));
        labelDeskripsi.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(labelJudul);
        panel.add(Box.createVerticalStrut(5));
        panel.add(labelDeskripsi);
        
        return panel;
    }
    
    private JPanel createPanelForm() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        panel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Label dan Input Tanggal
        JLabel labelTanggal = new JLabel("Tanggal:");
        labelTanggal.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(labelTanggal, gbc);
        
        // Spinner Tanggal - Minimum date adalah hari ini
        java.util.Date hariIni = new java.util.Date();
        SpinnerDateModel modelTanggal = new SpinnerDateModel(hariIni, hariIni, null, Calendar.DAY_OF_MONTH);
        spinnerTanggal = new JSpinner(modelTanggal);
        JSpinner.DateEditor editorTanggal = new JSpinner.DateEditor(spinnerTanggal, "yyyy-MM-dd");
        spinnerTanggal.setEditor(editorTanggal);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        panel.add(spinnerTanggal, gbc);
        gbc.gridwidth = 1;
        
        // Label dan Input Judul
        JLabel labelJudul = new JLabel("Judul/Agenda:");
        labelJudul.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(labelJudul, gbc);
        
        textFieldJudul = new JTextField();
        textFieldJudul.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        panel.add(textFieldJudul, gbc);
        gbc.gridwidth = 1;
        
        // Label dan Input Jam Mulai
        JLabel labelJamMulai = new JLabel("Jam Mulai:");
        labelJamMulai.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(labelJamMulai, gbc);
        
        spinnerJamMulai = createTimeSpinner("08:00");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(spinnerJamMulai, gbc);
        
        // Label dan Input Jam Selesai
        JLabel labelJamSelesai = new JLabel("Jam Selesai:");
        labelJamSelesai.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(labelJamSelesai, gbc);
        
        spinnerJamSelesai = createTimeSpinner("09:00");
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(spinnerJamSelesai, gbc);
        
        // Label dan Input Keterangan - Box Responsif
        JLabel labelKeterangan = new JLabel("Keterangan:");
        labelKeterangan.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(labelKeterangan, gbc);
        
        textAreaKeterangan = new JTextArea(4, 15);
        textAreaKeterangan.setFont(new Font("Arial", Font.PLAIN, 11));
        textAreaKeterangan.setLineWrap(true);
        textAreaKeterangan.setWrapStyleWord(true);
        textAreaKeterangan.setBorder(new LineBorder(new Color(180, 180, 180), 1));
        JScrollPane scrollKeterangan = new JScrollPane(textAreaKeterangan);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 2;
        panel.add(scrollKeterangan, gbc);
        
        return panel;
    }
    
    private JSpinner createTimeSpinner(String initialTime) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            java.util.Date date = format.parse(initialTime);
            JSpinner spinner = new JSpinner(new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY));
            JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "HH:mm");
            spinner.setEditor(editor);
            return spinner;
        } catch (Exception e) {
            return new JSpinner();
        }
    }
    
    private JPanel createPanelTabel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        
        String[] kolom = {"ID", "Tanggal", "Judul", "Jam Mulai", "Jam Selesai", "Keterangan"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableJadwal = new JTable(tableModel);
        tableJadwal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableJadwal.setRowHeight(25);
        tableJadwal.getColumnModel().getColumn(0).setMaxWidth(40);
        tableJadwal.getColumnModel().getColumn(1).setMaxWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(tableJadwal);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createPanelTombol() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(new Color(245, 245, 245));
        
        btnSimpan = new JButton("Simpan");
        btnSimpan.setBackground(new Color(46, 204, 113));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFont(new Font("Arial", Font.BOLD, 12));
        btnSimpan.setPreferredSize(new Dimension(110, 40));
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSimpan.addActionListener(e -> simpanAgenda());
        
        btnHapus = new JButton("Hapus");
        btnHapus.setBackground(new Color(231, 76, 60));
        btnHapus.setForeground(Color.WHITE);
        btnHapus.setFont(new Font("Arial", Font.BOLD, 12));
        btnHapus.setPreferredSize(new Dimension(110, 40));
        btnHapus.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHapus.addActionListener(e -> hapusAgenda());
        
        btnReset = new JButton("Reset");
        btnReset.setBackground(new Color(149, 165, 166));
        btnReset.setForeground(Color.WHITE);
        btnReset.setFont(new Font("Arial", Font.BOLD, 12));
        btnReset.setPreferredSize(new Dimension(110, 40));
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.addActionListener(e -> resetForm());
        
        panel.add(btnSimpan);
        panel.add(btnHapus);
        panel.add(btnReset);
        
        return panel;
    }
    
    private void simpanAgenda() {
        try {
            java.util.Date selectedDate = (java.util.Date) spinnerTanggal.getValue();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Pilih tanggal terlebih dahulu!", 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String judul = textFieldJudul.getText().trim();
            if (judul.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Masukkan judul agenda!", 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String tanggal = dateFormat.format(selectedDate);
            
            String jamMulai = getTimeFromSpinner(spinnerJamMulai);
            String jamSelesai = getTimeFromSpinner(spinnerJamSelesai);
            
            if (jamMulai.compareTo(jamSelesai) >= 0) {
                JOptionPane.showMessageDialog(this, "Jam mulai harus sebelum jam selesai!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String keterangan = textAreaKeterangan.getText().trim();
            
            boolean berhasil = jadwalService.tambahAgenda(dosenId, tanggal, 
                jamMulai + ":00", jamSelesai + ":00", judul, keterangan);
            
            if (berhasil) {
                JOptionPane.showMessageDialog(this, "Agenda berhasil disimpan!", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadDataJadwal();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan agenda!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void hapusAgenda() {
        int selectedRow = tableJadwal.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Pilih agenda yang ingin dihapus!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin ingin menghapus agenda ini?", 
            "Konfirmasi", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int agendaId = (Integer) tableModel.getValueAt(selectedRow, 0);
            if (jadwalService.hapusAgenda(agendaId)) {
                JOptionPane.showMessageDialog(this, "Agenda berhasil dihapus!", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadDataJadwal();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus agenda!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void resetForm() {
        java.util.Date hariIni = new java.util.Date();
        spinnerTanggal.setValue(hariIni);
        textFieldJudul.setText("");
        spinnerJamMulai.setValue(parseTime("08:00"));
        spinnerJamSelesai.setValue(parseTime("09:00"));
        textAreaKeterangan.setText("");
        tableJadwal.clearSelection();
    }
    
    private void loadDataJadwal() {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Koneksi database gagal!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String query = "SELECT id, tanggal, judul, jam_mulai, jam_selesai, keterangan " +
                          "FROM agenda_dosen WHERE id_dosen = ? ORDER BY tanggal DESC, jam_mulai ASC";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, dosenId);
            ResultSet rs = pstmt.executeQuery();
            
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getDate("tanggal"),
                    rs.getString("judul"),
                    rs.getTime("jam_mulai"),
                    rs.getTime("jam_selesai"),
                    rs.getString("keterangan")
                });
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private String getTimeFromSpinner(JSpinner spinner) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format((java.util.Date) spinner.getValue());
    }
    
    private java.util.Date parseTime(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            return format.parse(time);
        } catch (Exception e) {
            return new java.util.Date();
        }
    }
}