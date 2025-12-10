package tubespbol.view;

import java.awt.*;
import java.util.Map;
import javax.swing.*;

import tubespbol.view.components.SidebarButton;
import tubespbol.view.panels.*;
import tubespbol.service.*;
import tubespbol.controller.PermintaanMasukController;
import tubespbol.controller.LoginController;

public class DashboardDosen extends JFrame {

    // Panel utama tempat CardLayout menyimpan halaman-halaman
    JPanel contentPanel;
    
    // Data user yang login
    private String idDosen;
    private Map<String, String> userData;
    
    // Service untuk koneksi & logic aplikasi
    private UserService userService;
    private JadwalService jadwalService;
    private RequestService requestService;

    // CONSTRUCTOR BARU: dipakai saat login sukses
    public DashboardDosen(String idDosen, Map<String, String> userData) {
        this.idDosen = idDosen;          // ambil ID dosen
        this.userData = userData;        // ambil data user dalam bentuk Map
        
        // Buat instance service
        this.userService = new UserService();
        this.jadwalService = new JadwalService();
        this.requestService = new RequestService();
        
        // Panggil method untuk membangun UI
        initComponents();
    }
    
    // CONSTRUCTOR LAMA: fallback jika tidak ada data login
    public DashboardDosen() {
        this.idDosen = "GUEST";     // default
        this.userData = new java.util.HashMap<>();
        userData.put("nama", "Guest");
        userData.put("prodi", "Unknown");
        
        // Buat service
        this.userService = new UserService();
        this.jadwalService = new JadwalService();
        this.requestService = new RequestService();
        
        initComponents();
    }
    
    // METHOD PEMBANGUN UI DASHBOARD
    private void initComponents() {

        // Set basic window
        setTitle("Dashboard Dosen");
        setSize(1040, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // SIDEBAR

        // Panel sidebar dengan gradient
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Memperhalus rendering
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Warna gradient biru → ungu
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(41, 128, 185),
                    0, getHeight(), new Color(142, 68, 173)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        sidebar.setPreferredSize(new Dimension(240, 600));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

        // --------------------------- Profile section ---------------------------
        JPanel profilePanel = new JPanel();
        profilePanel.setOpaque(false);   // transparan agar gradient tetap terlihat
        profilePanel.setPreferredSize(new Dimension(220, 100));
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));

        JLabel avatarLabel = new JLabel("👨‍🏫");  // icon dosen
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(userData.get("nama"));
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel idLabel = new JLabel("ID: " + idDosen);
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        idLabel.setForeground(new Color(230, 240, 250));
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tambahkan elemen ke profile panel
        profilePanel.add(avatarLabel);
        profilePanel.add(Box.createVerticalStrut(5));
        profilePanel.add(nameLabel);
        profilePanel.add(Box.createVerticalStrut(3));
        profilePanel.add(idLabel);

        sidebar.add(profilePanel);

        // Tombol-tombol sidebar
        SidebarButton btnDashboard = new SidebarButton("Dashboard", "/tubespbol/resources/icons/dashboard.png");
        SidebarButton btnAturJadwal = new SidebarButton("Atur Jadwal", "/tubespbol/resources/icons/schedule.png");
        SidebarButton btnPermintaanMasuk = new SidebarButton("Permintaan Masuk", "/tubespbol/resources/icons/request.png");
        SidebarButton btnRiwayat = new SidebarButton("Riwayat", "/tubespbol/resources/icons/history.png");
        SidebarButton btnLogout = new SidebarButton("Logout", "/tubespbol/resources/icons/logout.png");

        // Masukkan tombol ke sidebar
        sidebar.add(btnDashboard);
        sidebar.add(btnAturJadwal);
        sidebar.add(btnPermintaanMasuk);
        sidebar.add(btnRiwayat);
        sidebar.add(btnLogout);

        // Tambahkan sidebar ke frame
        add(sidebar, BorderLayout.WEST);

        // CONTENT AREA

        // Panel utama dengan CardLayout
        contentPanel = new JPanel(new CardLayout());
        add(contentPanel, BorderLayout.CENTER);

        // 1. Dashboard
        contentPanel.add(
            new PanelDashboardDosen(idDosen, userData, jadwalService, requestService),
            "dashboard"
        );

        // 2. Panel Ketersediaan
        contentPanel.add(
            new PanelJadwalDosen(idDosen, userData, jadwalService),
            "ketersediaan"
        );

        // 3. Atur Jadwal
        contentPanel.add(
            new PanelAturJadwalDosen(idDosen, userData, jadwalService),
            "aturjadwal"
        );

        // 4. PERMINTAAN MASUK — Implementasi MVC Murni

        // Buat view kosong
        PanelPermintaanMasuk viewPermintaan = new PanelPermintaanMasuk();

        // Controller yang menghubungkan view dan service
        new PermintaanMasukController(viewPermintaan, requestService, idDosen);

        // Masukkan view ke CardLayout
        contentPanel.add(viewPermintaan, "permintaanMasuk");

        // 5. Riwayat permintaan
        contentPanel.add(
            new PanelRiwayatDosen(idDosen, userData, requestService),
            "riwayat"
        );

        // LISTENER BUTTON

        // Tiap tombol memanggil showPanel() untuk memindah CardLayout
        btnDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { 
                showPanel("dashboard"); 
            }
        });

        btnAturJadwal.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { 
                showPanel("aturjadwal"); 
            }
        });

        btnPermintaanMasuk.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { 
                showPanel("permintaanMasuk"); 
            }
        });

        btnRiwayat.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { 
                showPanel("riwayat"); 
            }
        });

        // Tombol logout → kembali ke LoginForm
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int confirm = JOptionPane.showConfirmDialog(
                    DashboardDosen.this,
                    "Apakah Anda yakin ingin logout?",
                    "Konfirmasi Logout",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {

                    // Tutup dashboard
                    dispose();

                    // Buka login form lagi
                    LoginForm loginForm = new LoginForm();
                    new LoginController(loginForm);
                    loginForm.setVisible(true);
                }
            }
        });

        // Terakhir tampilkan frame
        setVisible(true);
    }

    // Method memindah halaman CardLayout berdasarkan nama
    private void showPanel(String name) {
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, name);
    }
}
