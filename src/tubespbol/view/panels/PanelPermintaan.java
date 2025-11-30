package tubespbol.view.panels;

import tubespbol.view.components.DatePicker;
import tubespbol.view.components.TimePicker;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Date;
import tubespbol.service.JadwalService;
import tubespbol.service.RequestService;
import tubespbol.service.UserService;

public class PanelPermintaan extends JPanel {

    private String nim;
    private UserService userService;
    private RequestService requestService;
    
    // Komponen
    private DatePicker datePicker;
    private TimePicker timeStart;
    private TimePicker timeEnd;

    public PanelPermintaan(String nim, Map<String, String> userData, UserService us, JadwalService js, RequestService rs) {
        this.nim = nim;
        this.userService = us;
        this.requestService = rs;
        
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 245));

        JLabel title = new JLabel("Ajukan Permintaan Pertemuan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(41, 128, 185));
        title.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 20));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Pilih Dosen
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Pilih Dosen:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        JComboBox<String> cbDosen = new JComboBox<>();
        cbDosen.addItem("- Pilih Dosen -"); // Item default
        try {
            java.util.List<Map<String, String>> listDosen = userService.getAllDosen();
            for(Map<String, String> d : listDosen) {
                cbDosen.addItem(d.get("id_dosen") + " - " + d.get("nama"));
            }
        } catch(Exception e) {}
        formPanel.add(cbDosen, gbc);

        // Tanggal
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Tanggal:"), gbc);
        
        gbc.gridx = 1;
        datePicker = new DatePicker();
        formPanel.add(datePicker, gbc);

        // Waktu Mulai
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Jam Mulai:"), gbc);
        
        gbc.gridx = 1;
        timeStart = new TimePicker();
        timeStart.setTime(9, 0); 
        formPanel.add(timeStart, gbc);

        // Waktu Selesai
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Jam Selesai:"), gbc);
        
        gbc.gridx = 1;
        timeEnd = new TimePicker();
        timeEnd.setTime(10, 0);
        formPanel.add(timeEnd, gbc);

        // Keperluan
        gbc.gridx = 0; gbc.gridy++; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Keperluan:"), gbc);
        
        gbc.gridx = 1;
        JTextArea txtDesc = new JTextArea(4, 20);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        formPanel.add(new JScrollPane(txtDesc), gbc);

        // Tombol
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton btnSubmit = new JButton("Kirim Permintaan");
        btnSubmit.setBackground(new Color(46, 134, 193));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.setPreferredSize(new Dimension(180, 40));
        btnSubmit.setFocusPainted(false);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(btnSubmit, gbc);
        
        // --- ACTION LISTENER DENGAN VALIDASI ---
        btnSubmit.addActionListener(e -> {
            // 1. Validasi Dosen
            if (cbDosen.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, 
                    "Mohon pilih Dosen terlebih dahulu!", 
                    "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Validasi Tanggal
            Date selectedDate = datePicker.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, 
                    "Mohon pilih Tanggal pertemuan!", 
                    "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 3. Validasi Keperluan
            String ket = txtDesc.getText().trim();
            if (ket.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Mohon isi Keperluan pertemuan!", 
                    "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 4. Validasi Waktu
            String strMulai = timeStart.getTime();
            String strSelesai = timeEnd.getTime();
            
            if (strMulai.compareTo(strSelesai) >= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Jam Selesai harus lebih besar dari Jam Mulai!\nContoh: 09:00 s/d 10:00", 
                    "Validasi Waktu Salah", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // --- PERSIAPAN DATA ---
            String selectedDosen = cbDosen.getSelectedItem().toString();
            String idDosen = selectedDosen.split(" - ")[0];
            
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String tanggal = sdf.format(selectedDate);
            
            String jamMulaiDB = strMulai + ":00";
            String jamSelesaiDB = strSelesai + ":00";
            
            // Konfirmasi User
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Ajukan pertemuan dengan " + selectedDosen + "\n" +
                "Tanggal: " + tanggal + "\n" +
                "Pukul: " + strMulai + " - " + strSelesai + "?",
                "Konfirmasi Pengajuan", JOptionPane.YES_NO_OPTION);
                
            if (confirm != JOptionPane.YES_OPTION) return;

            // --- EKSEKUSI KE DATABASE ---
            try {
                // Panggil Service (Method ini sekarang melempar SQLException jika bentrok)
                String result = requestService.ajukanRequest(nim, idDosen, tanggal, jamMulaiDB, jamSelesaiDB, ket);
                
                if(result != null) {
                    JOptionPane.showMessageDialog(this, 
                        "✅ Permintaan berhasil dikirim!\nID Request: " + result + "\nSilakan cek status di menu Riwayat.", 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Reset Form jika sukses
                    cbDosen.setSelectedIndex(0);
                    txtDesc.setText("");
                }
            } catch (java.sql.SQLException ex) {
                // TANGKAP ERROR DARI MYSQL TRIGGER (BENTROK JADWAL)
                String pesanError = ex.getMessage();
                
                // Jika error generic, beri pesan default
                if (pesanError == null || pesanError.isEmpty()) {
                    pesanError = "Terjadi kesalahan database.";
                }
                
                JOptionPane.showMessageDialog(this, 
                    "❌ Gagal mengajukan permintaan:\n" + pesanError, 
                    "Jadwal Bentrok / Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                // Tangkap error lain yang tidak terduga
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "❌ Terjadi kesalahan sistem: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}