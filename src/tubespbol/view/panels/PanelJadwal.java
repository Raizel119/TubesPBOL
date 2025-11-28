package tubespbol.view.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;
import tubespbol.service.JadwalService;
import tubespbol.service.UserService;

public class PanelJadwal extends JPanel {

    private String nim;
    private UserService userService;
    private JadwalService jadwalService;

    // CONSTRUCTOR LENGKAP (Agar cocok dengan DashboardMahasiswa)
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

        // Panel Filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(Color.WHITE);
        
        JComboBox<String> cmbDosen = new JComboBox<>();
        cmbDosen.addItem("- Pilih Dosen -");
        try {
            java.util.List<Map<String, String>> listDosen = userService.getAllDosen();
            for(Map<String, String> d : listDosen) {
                cmbDosen.addItem(d.get("id_dosen") + " - " + d.get("nama"));
            }
        } catch(Exception e) {}

        JTextField txtTanggal = new JTextField(10);
        txtTanggal.setText(java.time.LocalDate.now().toString());
        
        JButton btnCek = new JButton("Cek");
        btnCek.setBackground(new Color(52, 152, 219));
        btnCek.setForeground(Color.WHITE);

        filterPanel.add(new JLabel("Dosen:"));
        filterPanel.add(cmbDosen);
        filterPanel.add(new JLabel("Tanggal (YYYY-MM-DD):"));
        filterPanel.add(txtTanggal);
        filterPanel.add(btnCek);

        // Tabel
        String[] columns = {"Jam Mulai", "Jam Selesai", "Kegiatan", "Keterangan"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(filterPanel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        
        add(tablePanel, BorderLayout.CENTER);

        // Action Listener
        btnCek.addActionListener(e -> {
            if(cmbDosen.getSelectedIndex() == 0) return;
            
            String selectedItem = cmbDosen.getSelectedItem().toString();
            String idDosen = selectedItem.split(" - ")[0];
            String tanggal = txtTanggal.getText();
            
            model.setRowCount(0);
            java.util.List<Map<String, String>> jadwal = jadwalService.getJadwalLengkap(idDosen, tanggal);
            
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
        });
    }
}
