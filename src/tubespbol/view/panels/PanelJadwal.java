package tubespbol.view.panels;

import tubespbol.view.components.DatePicker;
import tubespbol.service.JadwalService;
import tubespbol.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Date;

public class PanelJadwal extends JPanel {

    private String nim;
    private UserService userService;
    private JadwalService jadwalService;
    
    private DatePicker datePicker;

    public PanelJadwal(String nim, Map<String, String> userData, UserService us, JadwalService js) {
        this.nim = nim;
        this.userService = us;
        this.jadwalService = js;
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 245));

        JLabel title = new JLabel("Cek Jadwal Dosen");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(41, 128, 185));
        title.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 20));
        add(title, BorderLayout.NORTH);

        // === FILTER PANEL DENGAN GRIDBAGLAYOUT (LEBIH RAPI) ===
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(new EmptyBorder(15, 25, 15, 25)); // Padding lebih lega

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Jarak antar komponen
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // --- BARIS 1: DOSEN ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        JLabel lblDosen = new JLabel("Pilih Dosen:");
        lblDosen.setFont(new Font("Segoe UI", Font.BOLD, 13));
        filterPanel.add(lblDosen, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.gridwidth = 2; // Span 2 kolom
        JComboBox<String> cmbDosen = new JComboBox<>();
        cmbDosen.addItem("- Pilih Dosen -");
        cmbDosen.setPreferredSize(new Dimension(200, 35));
        try {
            java.util.List<Map<String, String>> listDosen = userService.getAllDosen();
            for(Map<String, String> d : listDosen) {
                cmbDosen.addItem(d.get("id_dosen") + " - " + d.get("nama"));
            }
        } catch(Exception e) {}
        filterPanel.add(cmbDosen, gbc);

        // --- BARIS 2: TANGGAL & TOMBOL ---
        // Label Tanggal
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0; gbc.gridwidth = 1;
        JLabel lblTanggal = new JLabel("Tanggal:");
        lblTanggal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        filterPanel.add(lblTanggal, gbc);

        // Date Picker
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        datePicker = new DatePicker();
        filterPanel.add(datePicker, gbc);

        // Tombol Cek (Di sebelah kanan DatePicker)
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.0;
        JButton btnCek = new JButton("Cari Jadwal");
        btnCek.setBackground(new Color(52, 152, 219));
        btnCek.setForeground(Color.WHITE);
        btnCek.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCek.setPreferredSize(new Dimension(110, 32)); // Tinggi disamakan dengan DatePicker
        btnCek.setFocusPainted(false);
        btnCek.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterPanel.add(btnCek, gbc);

        // --- TABLE ---
        String[] columns = {"Jam Mulai", "Jam Selesai", "Kegiatan", "Keterangan"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Return false agar tidak bisa diedit
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(35); // Baris lebih tinggi biar enak dilihat
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(Color.BLACK);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(filterPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        
        add(tablePanel, BorderLayout.CENTER);

        // --- ACTION LISTENER ---
        btnCek.addActionListener(e -> {
            if(cmbDosen.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Silakan pilih dosen terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Date selectedDate = datePicker.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Silakan pilih tanggal!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String selectedItem = cmbDosen.getSelectedItem().toString();
            String idDosen = selectedItem.split(" - ")[0];
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tanggal = sdf.format(selectedDate);
            
            // Loading cursor
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            // SwingWorker agar tidak freeze
            new SwingWorker<java.util.List<Map<String, String>>, Void>() {
                @Override
                protected java.util.List<Map<String, String>> doInBackground() throws Exception {
                    return jadwalService.getJadwalLengkap(idDosen, tanggal);
                }

                @Override
                protected void done() {
                    setCursor(Cursor.getDefaultCursor());
                    try {
                        model.setRowCount(0);
                        java.util.List<Map<String, String>> jadwal = get();
                        
                        if(jadwal.isEmpty()) {
                            model.addRow(new Object[]{"", "", "Tidak ada jadwal", "Dosen available"});
                        } else {
                            for(Map<String, String> j : jadwal) {
                                model.addRow(new Object[]{
                                    j.get("jam_mulai"),
                                    j.get("jam_selesai"),
                                    j.get("tipe") + ": " + j.get("judul"),
                                    j.get("keterangan")
                                });
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }.execute();
        });
    }
}