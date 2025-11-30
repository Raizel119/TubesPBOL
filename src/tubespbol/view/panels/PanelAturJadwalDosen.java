package tubespbol.view.panels;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalDate;
import java.time.ZoneId;
import tubespbol.model.Database;
import tubespbol.service.JadwalService;

public class PanelAturJadwalDosen extends JPanel {
    private JButton btnTanggal;
    private JSpinner spinnerJamMulai;
    private JSpinner spinnerJamSelesai;
    private JTextField textFieldJudul;
    private JTextArea textAreaKeterangan;
    private JButton btnSimpan, btnHapus, btnReset;
    private JTable tableJadwal;
    private DefaultTableModel tableModel;
    
    private JadwalService jadwalService;
    private String dosenId;
    private java.util.Date selectedDate;
    private java.util.Date minDate;
    private java.util.Date maxDate;
    
    public PanelAturJadwalDosen(String dosenId, Map<String, String> userData, JadwalService jadwalService) {
        this.dosenId = dosenId;
        this.jadwalService = jadwalService;
        this.selectedDate = new java.util.Date();
        
        // Set range tanggal: hari ini sampai 30 hari ke depan
        Calendar calToday = Calendar.getInstance();
        this.minDate = calToday.getTime();
        
        Calendar calMax = Calendar.getInstance();
        calMax.add(Calendar.DAY_OF_MONTH, 30);
        this.maxDate = calMax.getTime();
        
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Panel Judul
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
        splitPane.setDividerLocation(380);
        splitPane.setResizeWeight(0.35);
        
        // Layout
        add(panelJudul, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(panelTombol, BorderLayout.SOUTH);
        
        loadDataJadwal();
    }
    
    private JPanel createPanelJudul() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(41, 128, 185));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel labelJudul = new JLabel("KELOLA AGENDA PRIBADI");
        labelJudul.setFont(new Font("Arial", Font.BOLD, 24));
        labelJudul.setForeground(Color.WHITE);
        labelJudul.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel labelDeskripsi = new JLabel("Kelola agenda pribadi Anda agar tidak bisa dipilih mahasiswa.");
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
        
        // ============ TANGGAL DENGAN BUTTON KALENDER ============
        JLabel labelTanggal = new JLabel("Tanggal:");
        labelTanggal.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(labelTanggal, gbc);
        
        // Panel untuk tanggal dengan button kalender
        JPanel panelTanggal = new JPanel();
        panelTanggal.setBackground(Color.WHITE);
        panelTanggal.setLayout(new BorderLayout(5, 5));
        
        // Button Kalender
        btnTanggal = new JButton("📅 " + formatDate(selectedDate));
        btnTanggal.setPreferredSize(new Dimension(200, 35));
        btnTanggal.setFont(new Font("Arial", Font.PLAIN, 12));
        btnTanggal.setBackground(new Color(52, 152, 219));
        btnTanggal.setForeground(Color.WHITE);
        btnTanggal.setBorderPainted(false);
        btnTanggal.setFocusPainted(false);
        btnTanggal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTanggal.addActionListener(e -> openDatePicker());
        panelTanggal.add(btnTanggal, BorderLayout.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        panel.add(panelTanggal, gbc);
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
        
        // Label dan Input Keterangan
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

    private void openDatePicker() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Pilih Tanggal (Maksimal 30 Hari dari Hari Ini)");
        dialog.setModal(true);
        dialog.setSize(420, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        
        JPanel panelDialog = new JPanel(new BorderLayout(10, 10));
        panelDialog.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelDialog.setBackground(Color.WHITE);
        
        // ============ HITUNG RANGE TANGGAL ============
        Calendar calToday = Calendar.getInstance();
        java.util.Date minDate = calToday.getTime();
        
        Calendar calMax = Calendar.getInstance();
        calMax.add(Calendar.DAY_OF_MONTH, 30);
        java.util.Date maxDate = calMax.getTime();
        
        // PERBAIKAN: Gunakan array untuk menyimpan referensi yang bisa diubah
        java.util.Date[] selectedDateWrapper = new java.util.Date[1];
        selectedDateWrapper[0] = selectedDate;
        
        if (selectedDateWrapper[0].before(minDate)) {
            selectedDateWrapper[0] = minDate;
        }
        if (selectedDateWrapper[0].after(maxDate)) {
            selectedDateWrapper[0] = maxDate;
        }
        
        // ============ LABEL INFO ============
        JLabel labelInfo = new JLabel("Pilih tanggal antara " + formatDate(minDate) + 
                                       " hingga " + formatDate(maxDate));
        labelInfo.setFont(new Font("Arial", Font.ITALIC, 11));
        labelInfo.setForeground(new Color(100, 100, 100));
        
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(Color.WHITE);
        panelTop.add(new JLabel("Pilih Tanggal:"), BorderLayout.NORTH);
        panelTop.add(labelInfo, BorderLayout.SOUTH);
        
        panelDialog.add(panelTop, BorderLayout.NORTH);
        
        // ============ CONTAINER KALENDER ============
        JPanel panelKalenderContainer = new JPanel(new BorderLayout());
        panelKalenderContainer.setBackground(Color.WHITE);
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(selectedDateWrapper[0]);
        
        JPanel panelKalender = createCalendarPanel(cal, selectedDateWrapper, minDate, maxDate, panelKalenderContainer);
        panelKalenderContainer.add(panelKalender, BorderLayout.CENTER);
        
        panelDialog.add(panelKalenderContainer, BorderLayout.CENTER);
        
        // Tombol OK dan BATAL
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelButtons.setBackground(Color.WHITE);
        
        JButton btnOK = new JButton("OK");
        btnOK.setPreferredSize(new Dimension(100, 35));
        btnOK.setBackground(new Color(46, 204, 113));
        btnOK.setForeground(Color.WHITE);
        btnOK.setBorderPainted(false);
        btnOK.setFont(new Font("Arial", Font.BOLD, 12));
        btnOK.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOK.addActionListener(e -> {
            selectedDate = selectedDateWrapper[0];
            btnTanggal.setText("📅 " + formatDate(selectedDate));
            loadDataJadwal();
            dialog.dispose();
        });
        
        JButton btnBatal = new JButton("Batal");
        btnBatal.setPreferredSize(new Dimension(100, 35));
        btnBatal.setBackground(new Color(231, 76, 60));
        btnBatal.setForeground(Color.WHITE);
        btnBatal.setBorderPainted(false);
        btnBatal.setFont(new Font("Arial", Font.BOLD, 12));
        btnBatal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBatal.addActionListener(e -> dialog.dispose());
        
        panelButtons.add(btnOK);
        panelButtons.add(btnBatal);
        
        panelDialog.add(panelButtons, BorderLayout.SOUTH);
        
        dialog.add(panelDialog);
        dialog.setVisible(true);
    }
    
    // ============ MEMBUAT KALENDER VISUAL ============
    private JPanel createCalendarPanel(Calendar cal, java.util.Date[] selectedDateWrapper, 
                                      java.util.Date minDate, java.util.Date maxDate,
                                      JPanel containerPanel) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // ============ PANEL NAVIGASI BULAN/TAHUN ============
        JPanel panelNav = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelNav.setBackground(Color.WHITE);
        panelNav.setMaximumSize(new Dimension(400, 50));
        
        JButton btnPrev = new JButton("◄");
        btnPrev.setBackground(new Color(52, 152, 219));
        btnPrev.setForeground(Color.WHITE);
        btnPrev.setBorderPainted(false);
        btnPrev.setFont(new Font("Arial", Font.BOLD, 16));
        btnPrev.setPreferredSize(new Dimension(45, 40));
        btnPrev.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPrev.setToolTipText("Bulan Sebelumnya");
        
        JLabel labelBulan = new JLabel(getMonthYear(cal));
        labelBulan.setFont(new Font("Arial", Font.BOLD, 16));
        labelBulan.setForeground(new Color(41, 128, 185));
        labelBulan.setPreferredSize(new Dimension(180, 40));
        labelBulan.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton btnNext = new JButton("►");
        btnNext.setBackground(new Color(52, 152, 219));
        btnNext.setForeground(Color.WHITE);
        btnNext.setBorderPainted(false);
        btnNext.setFont(new Font("Arial", Font.BOLD, 16));
        btnNext.setPreferredSize(new Dimension(45, 40));
        btnNext.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNext.setToolTipText("Bulan Berikutnya");
        
        panelNav.add(btnPrev);
        panelNav.add(labelBulan);
        panelNav.add(btnNext);
        
        panel.add(panelNav);
        panel.add(Box.createVerticalStrut(10));
        
        // ============ PANEL GRID TANGGAL ============
        JPanel panelTanggal = new JPanel(new GridLayout(0, 7, 4, 4));
        panelTanggal.setBackground(Color.WHITE);
        panelTanggal.setMaximumSize(new Dimension(380, 250));
        
        // Header hari
        String[] hariNama = {"Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab"};
        for (String hari : hariNama) {
            JLabel labelHari = new JLabel(hari);
            labelHari.setFont(new Font("Arial", Font.BOLD, 12));
            labelHari.setForeground(new Color(52, 152, 219));
            labelHari.setHorizontalAlignment(SwingConstants.CENTER);
            panelTanggal.add(labelHari);
        }
        
        // Tanggal-tanggal
        int firstDay = cal.get(Calendar.DAY_OF_WEEK) - 1; // Sunday = 0
        int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        // Tambah empty cells di awal
        for (int i = 0; i < firstDay; i++) {
            panelTanggal.add(new JLabel(""));
        }
        
        // Tanggal button
        for (int day = 1; day <= maxDays; day++) {
            Calendar dayCalendar = Calendar.getInstance();
            dayCalendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), day);
            dayCalendar.set(Calendar.HOUR_OF_DAY, 0);
            dayCalendar.set(Calendar.MINUTE, 0);
            dayCalendar.set(Calendar.SECOND, 0);
            java.util.Date dayDate = dayCalendar.getTime();
            
            JButton btnDay = new JButton(String.valueOf(day));
            btnDay.setFont(new Font("Arial", Font.PLAIN, 12));
            btnDay.setFocusPainted(false);
            btnDay.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnDay.setPreferredSize(new Dimension(45, 45));
            
            // CEK RANGE TANGGAL
            boolean isInRange = !dayDate.before(minDate) && !dayDate.after(maxDate);
            
            if (!isInRange) {
                btnDay.setBackground(new Color(220, 220, 220));
                btnDay.setForeground(new Color(150, 150, 150));
                btnDay.setEnabled(false);
                btnDay.setBorderPainted(false);
            } else {
                btnDay.setBackground(Color.WHITE);
                btnDay.setForeground(Color.BLACK);
                btnDay.setBorderPainted(true);
                btnDay.setBorder(new LineBorder(new Color(200, 200, 200), 1));
                
                // Highlight tanggal yang dipilih
                if (isSameDay(dayDate, selectedDateWrapper[0])) {
                    btnDay.setBackground(new Color(52, 152, 219));
                    btnDay.setForeground(Color.WHITE);
                    btnDay.setFont(new Font("Arial", Font.BOLD, 12));
                }
                
                // Click handler untuk memilih tanggal
                final java.util.Date finalDayDate = dayDate;
                btnDay.addActionListener(e -> {
                    selectedDateWrapper[0] = finalDayDate;
                    
                    // Refresh kalender
                    containerPanel.removeAll();
                    JPanel newPanel = createCalendarPanel(cal, selectedDateWrapper, minDate, maxDate, containerPanel);
                    containerPanel.add(newPanel, BorderLayout.CENTER);
                    containerPanel.revalidate();
                    containerPanel.repaint();
                });
            }
            
            panelTanggal.add(btnDay);
        }
        
        panel.add(panelTanggal);
        
        // ============ BUTTON HANDLERS NAVIGASI BULAN ============
        btnPrev.addActionListener(e -> {
            Calendar prevCal = (Calendar) cal.clone();
            prevCal.add(Calendar.MONTH, -1);
            
            if (!prevCal.getTime().before(minDate) || isSameMonth(prevCal.getTime(), minDate)) {
                cal.add(Calendar.MONTH, -1);
                labelBulan.setText(getMonthYear(cal));
                
                // Refresh kalender
                containerPanel.removeAll();
                JPanel newPanel = createCalendarPanel(cal, selectedDateWrapper, minDate, maxDate, containerPanel);
                containerPanel.add(newPanel, BorderLayout.CENTER);
                containerPanel.revalidate();
                containerPanel.repaint();
            }
        });
        
        btnNext.addActionListener(e -> {
            Calendar nextCal = (Calendar) cal.clone();
            nextCal.add(Calendar.MONTH, 1);
            
            if (!nextCal.getTime().after(maxDate) || isSameMonth(nextCal.getTime(), maxDate)) {
                cal.add(Calendar.MONTH, 1);
                labelBulan.setText(getMonthYear(cal));
                
                // Refresh kalender
                containerPanel.removeAll();
                JPanel newPanel = createCalendarPanel(cal, selectedDateWrapper, minDate, maxDate, containerPanel);
                containerPanel.add(newPanel, BorderLayout.CENTER);
                containerPanel.revalidate();
                containerPanel.repaint();
            }
        });
        
        return panel;
    }
    
    private String getMonthYear(Calendar cal) {
        SimpleDateFormat fmt = new SimpleDateFormat("MMMM yyyy", new Locale("id", "ID"));
        return fmt.format(cal.getTime());
    }
    
    private boolean isSameDay(java.util.Date date1, java.util.Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(date1).equals(fmt.format(date2));
    }
    
    private boolean isSameMonth(java.util.Date date1, java.util.Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }
        
    private String formatDate(java.util.Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(date);
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
        selectedDate = new java.util.Date();
        btnTanggal.setText("📅 " + formatDate(selectedDate));
        textFieldJudul.setText("");
        spinnerJamMulai.setValue(parseTime("08:00"));
        spinnerJamSelesai.setValue(parseTime("09:00"));
        textAreaKeterangan.setText("");
        tableJadwal.clearSelection();
        loadDataJadwal();
    }
    
    private void loadDataJadwal() {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) {
                System.out.println("ERROR: Database connection is null!");
                return;
            }
            
            System.out.println("Loading jadwal for dosenId: " + dosenId);
            
            String query = "SELECT id, tanggal, judul, jam_mulai, jam_selesai, keterangan " +
                          "FROM agenda_dosen " +
                          "WHERE id_dosen = ? AND tanggal >= CURDATE() " +
                          "ORDER BY tanggal ASC, jam_mulai ASC";
            
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, dosenId);
            
            ResultSet rs = pstmt.executeQuery();
            
            tableModel.setRowCount(0);
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getDate("tanggal"),
                    rs.getString("judul"),
                    rs.getTime("jam_mulai"),
                    rs.getTime("jam_selesai"),
                    rs.getString("keterangan")
                });
            }
            
            System.out.println("Total rows loaded: " + rowCount);
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("SQL Error loading data: " + e.getMessage());
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