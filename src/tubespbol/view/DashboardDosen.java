package tubespbol.view;

import java.awt.*;
import java.util.Map;
import javax.swing.*;
import tubespbol.view.components.SidebarButton;
import tubespbol.view.panels.*;
import tubespbol.service.*;
import tubespbol.controller.PermintaanMasukController;

public class DashboardDosen extends JFrame {

    JPanel contentPanel;
    
    // User data
    private String idDosen;
    private Map<String, String> userData;
    
    // Services
    private UserService userService;
    private JadwalService jadwalService;
    private RequestService requestService;

    // Constructor BARU dengan parameter
    public DashboardDosen(String idDosen, Map<String, String> userData) {
        this.idDosen = idDosen;
        this.userData = userData;
        
        // Initialize services
        this.userService = new UserService();
        this.jadwalService = new JadwalService();
        this.requestService = new RequestService();
        
        initComponents();
    }
    
    // Constructor LAMA
    public DashboardDosen() {
        this.idDosen = "GUEST";
        this.userData = new java.util.HashMap<>();
        userData.put("nama", "Guest");
        userData.put("prodi", "Unknown");
        
        this.userService = new UserService();
        this.jadwalService = new JadwalService();
        this.requestService = new RequestService();
        
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Dashboard Dosen");
        setSize(1040, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // ================= SIDEBAR =================
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
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
        
        // Header Profile
        JPanel profilePanel = new JPanel();
        profilePanel.setOpaque(false);
        profilePanel.setPreferredSize(new Dimension(220, 100));
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        
        JLabel avatarLabel = new JLabel("👨‍🏫");
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
        
        profilePanel.add(avatarLabel);
        profilePanel.add(Box.createVerticalStrut(5));
        profilePanel.add(nameLabel);
        profilePanel.add(Box.createVerticalStrut(3));
        profilePanel.add(idLabel);
        
        sidebar.add(profilePanel);

        SidebarButton btnDashboard = new SidebarButton("Dashboard", "/tubespbol/resources/icons/dashboard.png");
        SidebarButton btnAturJadwal = new SidebarButton("Atur Jadwal", "/tubespbol/resources/icons/schedule.png");
        SidebarButton btnPermintaanMasuk = new SidebarButton("Permintaan Masuk", "/tubespbol/resources/icons/request.png");
        SidebarButton btnRiwayat = new SidebarButton("Riwayat", "/tubespbol/resources/icons/history.png");
        SidebarButton btnLogout = new SidebarButton("Logout", "/tubespbol/resources/icons/logout.png");

        sidebar.add(btnDashboard);
        sidebar.add(btnAturJadwal);
        sidebar.add(btnPermintaanMasuk);
        sidebar.add(btnRiwayat);
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // ================= CONTENT AREA =================
        contentPanel = new JPanel(new CardLayout());
        add(contentPanel, BorderLayout.CENTER);

        // 1. Dashboard (Masih Style Lama)
        contentPanel.add(new PanelDashboardDosen(idDosen, userData, jadwalService, requestService), "dashboard");
        
        // 2. Ketersediaan (Masih Style Lama)
        contentPanel.add(new PanelJadwalDosen(idDosen, userData, jadwalService), "ketersediaan");
        
        // 3. Atur Jadwal (Masih Style Lama)
        contentPanel.add(new PanelAturJadwalDosen(idDosen, userData, jadwalService), "aturjadwal");
        
        // ============================================================
        // 4. PERMINTAAN MASUK (IMPLEMENTASI MVC MURNI DI SINI) 🚀
        // ============================================================
        PanelPermintaanMasuk viewPermintaan = new PanelPermintaanMasuk(); // Buat View Kosong
        new PermintaanMasukController(viewPermintaan, requestService, idDosen); // Sambungkan dengan Controller
        contentPanel.add(viewPermintaan, "permintaanMasuk"); // Masukkan ke Layar
        // ============================================================

        // 5. Riwayat (Masih Style Lama)
        contentPanel.add(new PanelRiwayatDosen(idDosen, userData, requestService), "riwayat");

        // ================= LISTENERS =================
        btnDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { showPanel("dashboard"); }
        });

        btnAturJadwal.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { showPanel("aturjadwal"); }
        });

        btnPermintaanMasuk.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { showPanel("permintaanMasuk"); }
        });

        btnRiwayat.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) { showPanel("riwayat"); }
        });

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
                    dispose();
                    new LoginForm();
                }
            }
        });

        setVisible(true);
    }

    private void showPanel(String name) {
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, name);
    }
}