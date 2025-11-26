package tubespbol.view;

import tubespbol.view.components.SidebarButton;
import tubespbol.view.panels.*;

import javax.swing.*;
import java.awt.*;

public class DashboardMahasiswa extends JFrame {

    JPanel contentPanel;

    public DashboardMahasiswa() {
        setTitle("Dashboard Mahasiswa");
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
                
                // Gradient dari biru ke ungu
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
        profilePanel.setPreferredSize(new Dimension(220, 80));
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        
        JLabel avatarLabel = new JLabel("👨‍🎓");
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel nameLabel = new JLabel("Mahasiswa");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        profilePanel.add(avatarLabel);
        profilePanel.add(Box.createVerticalStrut(5));
        profilePanel.add(nameLabel);
        
        sidebar.add(profilePanel);

        SidebarButton btnDashboard = new SidebarButton("Dashboard", "/tubespbol/resources/icons/dashboard.png");
        SidebarButton btnJadwal = new SidebarButton("Jadwal", "/tubespbol/resources/icons/schedule.png");
        SidebarButton btnPermintaan = new SidebarButton("Permintaan", "/tubespbol/resources/icons/request.png");
        SidebarButton btnRiwayat = new SidebarButton("Riwayat", "/tubespbol/resources/icons/history.png");
        SidebarButton btnLogout = new SidebarButton("Logout", "/tubespbol/resources/icons/logout.png");

        sidebar.add(btnDashboard);
        sidebar.add(btnJadwal);
        sidebar.add(btnPermintaan);
        sidebar.add(btnRiwayat);
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // ================= CONTENT AREA =================
        contentPanel = new JPanel(new CardLayout());
        add(contentPanel, BorderLayout.CENTER);

        // Add Panels
        contentPanel.add(new PanelDashboard(), "dashboard");
        contentPanel.add(new PanelJadwal(), "jadwal");
        contentPanel.add(new PanelPermintaan(), "permintaan");
        contentPanel.add(new PanelRiwayat(), "riwayat");

        // ================= LISTENERS =================
        btnDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showPanel("dashboard");
            }
        });
        btnJadwal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showPanel("jadwal");
            }
        });
        btnPermintaan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showPanel("permintaan");
            }
        });
        btnRiwayat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showPanel("riwayat");
            }
        });
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                new LoginForm();
            }
        });

        setVisible(true);
    }

    private void showPanel(String name) {
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, name);
    }
}
