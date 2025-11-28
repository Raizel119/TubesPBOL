package tubespbol.view.panels;

import tubespbol.view.components.DatePicker;
import tubespbol.view.components.TimePicker;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import tubespbol.service.JadwalService;
import tubespbol.service.RequestService;
import tubespbol.service.UserService;
import tubespbol.view.components.DatePicker; // Import komponen baru
import tubespbol.view.components.TimePicker; // Import komponen baru

public class PanelPermintaan extends JPanel {

    private String nim;
    private UserService userService;
    private RequestService requestService;
    
    // Komponen
    private DatePicker datePicker;
    private TimePicker timeStart;
    private TimePicker timeEnd;

    // CONSTRUCTOR LENGKAP (Agar cocok dengan DashboardMahasiswa)
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
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Pilih Dosen:"), gbc);
        
        gbc.gridx = 1;
        JComboBox<String> cbDosen = new JComboBox<>();
        try {
            java.util.List<Map<String, String>> listDosen = userService.getAllDosen();
            for(Map<String, String> d : listDosen) {
                cbDosen.addItem(d.get("id_dosen") + " - " + d.get("nama"));
            }
        } catch(Exception e) {}
        formPanel.add(cbDosen, gbc);

        // Tanggal (Pakai DatePicker)
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Tanggal:"), gbc);
        
        gbc.gridx = 1;
        datePicker = new DatePicker();
        formPanel.add(datePicker, gbc);

        // Waktu Mulai (Pakai TimePicker)
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Jam Mulai:"), gbc);
        
        gbc.gridx = 1;
        timeStart = new TimePicker();
        timeStart.setTime(9, 0); // Default 09:00
        formPanel.add(timeStart, gbc);

        // Waktu Selesai (Pakai TimePicker)
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Jam Selesai:"), gbc);
        
        gbc.gridx = 1;
        timeEnd = new TimePicker();
        timeEnd.setTime(10, 0); // Default 10:00
        formPanel.add(timeEnd, gbc);

        // Keperluan
        gbc.gridx = 0; gbc.gridy++; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Keperluan:"), gbc);
        
        gbc.gridx = 1;
        JTextArea txtDesc = new JTextArea(4, 20);
        txtDesc.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        formPanel.add(new JScrollPane(txtDesc), gbc);

        // Tombol
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton btnSubmit = new JButton("Kirim Permintaan");
        btnSubmit.setBackground(new Color(46, 134, 193));
        btnSubmit.setForeground(Color.WHITE);
        formPanel.add(btnSubmit, gbc);
        
        // Action
        btnSubmit.addActionListener(e -> {
            String selectedDosen = cbDosen.getSelectedItem().toString();
            String idDosen = selectedDosen.split(" - ")[0];
            
            // Konversi Date ke String
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String tanggal = sdf.format(datePicker.getDate());
            
            // TimePicker return "09:00", tambah ":00" utk SQL
            String mulai = timeStart.getTime() + ":00"; 
            String selesai = timeEnd.getTime() + ":00";
            
            String ket = txtDesc.getText();
            
            String result = requestService.ajukanRequest(nim, idDosen, tanggal, mulai, selesai, ket);
            
            if(result != null) {
                JOptionPane.showMessageDialog(this, "Berhasil! ID Request: " + result);
                txtDesc.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengirim permintaan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
