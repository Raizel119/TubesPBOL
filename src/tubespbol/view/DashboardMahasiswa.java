package tubespbol.view;

import tubespbol.view.components.SidebarButton;
import tubespbol.view.panels.*;

import javax.swing.*;
import java.awt.*;

public class DashboardMahasiswa extends JFrame {

    JPanel contentPanel;

    public DashboardMahasiswa() {
        setTitle("Dashboard Mahasiswa");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // ================= SIDEBAR =================
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(31, 78, 121));
        sidebar.setPreferredSize(new Dimension(200, 600));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

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
