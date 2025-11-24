package tubespbol.view;

import java.awt.*;
import javax.swing.*;
import tubespbol.view.components.SidebarButton;
import tubespbol.view.panels.*;

public class DashboardDosen extends JFrame {

    JPanel contentPanel;

    public DashboardDosen() {
        setTitle("Dashboard Dosen");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // ================= SIDEBAR =================
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(31, 78, 121)); // Biru tua
        sidebar.setPreferredSize(new Dimension(200, 600));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

        SidebarButton btnDashboard = new SidebarButton("Dashboard", "/tubespbol/resources/icons/dashboard.png");

        // sesuai PDF: Dosen mengatur jadwal
        SidebarButton btnKetersediaan = new SidebarButton("Jadwal Ketersediaan", "/tubespbol/resources/icons/schedule.png");

        // sesuai PDF: Dosen melihat permintaan masuk
        SidebarButton btnPermintaanMasuk = new SidebarButton("Permintaan Masuk", "/tubespbol/resources/icons/request.png");

        SidebarButton btnRiwayat = new SidebarButton("Riwayat", "/tubespbol/resources/icons/history.png");
        SidebarButton btnLogout = new SidebarButton("Logout", "/tubespbol/resources/icons/logout.png");

        sidebar.add(btnDashboard);
        sidebar.add(btnKetersediaan);
        sidebar.add(btnPermintaanMasuk);
        sidebar.add(btnRiwayat);
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // ================= CONTENT AREA =================
        contentPanel = new JPanel(new CardLayout());
        add(contentPanel, BorderLayout.CENTER);

        // Panel-panel untuk Dosen
        contentPanel.add(new PanelDashboardDosen(), "dashboard");
        contentPanel.add(new PanelJadwalDosen(), "ketersediaan");
        contentPanel.add(new PanelPermintaanMasuk(), "permintaanMasuk");
        contentPanel.add(new PanelRiwayatDosen(), "riwayat");

        // ================= LISTENERS =================
        btnDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showPanel("dashboard");
            }
        });

        btnKetersediaan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showPanel("ketersediaan");
            }
        });

        btnPermintaanMasuk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showPanel("permintaanMasuk");
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
