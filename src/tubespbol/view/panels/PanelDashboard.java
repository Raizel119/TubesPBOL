package tubespbol.view.panels;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import tubespbol.service.JadwalService;
import tubespbol.service.RequestService;

public class PanelDashboard extends JPanel {
    private String nim;
    private Map<String, String> userData;
    private RequestService requestService;
    private JadwalService jadwalService;

    // CONSTRUCTOR LENGKAP (Agar cocok dengan DashboardMahasiswa)
    public PanelDashboard(String nim, Map<String, String> userData, RequestService rs, JadwalService js) {
        this.nim = nim;
        this.userData = userData;
        this.requestService = rs;
        this.jadwalService = js;
        
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 245));

        // Panel utama
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(236, 240, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 25, 25));

        // Header tanpa card
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        // Panel kartu statistik
        JPanel statsPanel = createStatsPanel();
        mainPanel.add(statsPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
        headerPanel.setBackground(new Color(236, 240, 245));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Icon mahasiswa - sama dengan logo di sidebar
        JLabel iconLabel = new JLabel("👨‍🎓");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        iconLabel.setPreferredSize(new Dimension(80, 80));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        iconLabel.setBackground(new Color(41, 128, 185));
        iconLabel.setOpaque(true);

        // Panel informasi
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(236, 240, 245));

        String nama = userData != null ? userData.getOrDefault("nama", "Mahasiswa") : "Mahasiswa";
        String nimText = nim != null ? nim : "-";

        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeLabel = new JLabel("Selamat datang, " + nama);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        welcomeLabel.setForeground(new Color(100, 100, 100));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nimLabel = new JLabel("NIM: " + nimText);
        nimLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        nimLabel.setForeground(new Color(100, 100, 100));
        nimLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(welcomeLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(nimLabel);

        headerPanel.add(iconLabel);
        headerPanel.add(infoPanel);

        return headerPanel;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridBagLayout());
        statsPanel.setBackground(new Color(236, 240, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 25, 0);

        // Hitung statistik
        int totalPermintaan = 0;
        int totalDisetujui = 0;
        int totalPending = 0;
        int totalDitolak = 0;
        
        if (requestService != null) {
            try {
                java.util.List<Map<String, String>> reqs = requestService.getRequestMahasiswa(nim);
                totalPermintaan = reqs.size();
                for (Map<String, String> r : reqs) {
                    String status = r.get("status");
                    if ("DITERIMA".equalsIgnoreCase(status)) {
                        totalDisetujui++;
                    } else if ("PENDING".equalsIgnoreCase(status)) {
                        totalPending++;
                    } else if ("DITOLAK".equalsIgnoreCase(status)) {
                        totalDitolak++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Card Total Permintaan (panjang dan ramping di atas)
        JPanel totalCard = createLongCard("Total Permintaan", String.valueOf(totalPermintaan), 
                new Color(193, 220, 230), new Color(68, 130, 152));
        statsPanel.add(totalCard, gbc);

        // Panel untuk 3 card di bawah
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        
        JPanel bottomCards = new JPanel(new GridLayout(1, 3, 25, 0));
        bottomCards.setBackground(new Color(236, 240, 245));

        bottomCards.add(createCard("Disetujui", String.valueOf(totalDisetujui), 
                new Color(190, 230, 201), new Color(69, 130, 75)));
        bottomCards.add(createCard("Ditolak", String.valueOf(totalDitolak), 
                new Color(255, 200, 200), new Color(180, 50, 50)));
        bottomCards.add(createCard("Pending", String.valueOf(totalPending), 
                new Color(220, 220, 220), new Color(100, 100, 100)));

        statsPanel.add(bottomCards, gbc);

        return statsPanel;
    }

    private JPanel createLongCard(String title, String value, Color bg, Color textColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow effect
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 15, 15);
                
                // Background
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 15, 15);
                
                g2.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.height = Math.max(140, size.height);
                return size;
            }
        };
        
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(30, 45, 30, 45));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(textColor);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setHorizontalAlignment(SwingConstants.RIGHT);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 56));
        lblValue.setForeground(textColor);
        
        card.add(lblTitle, BorderLayout.WEST);
        card.add(lblValue, BorderLayout.EAST);
        
        return card;
    }

    private JPanel createCard(String title, String value, Color bg, Color textColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow effect
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 12, 12);
                
                // Background
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 12, 12);
                
                g2.dispose();
            }
            
            @Override
            public Dimension getMinimumSize() {
                return new Dimension(150, 200);
            }
            
            @Override
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.height = Math.max(200, size.height);
                return size;
            }
        };
        
        card.setLayout(new GridBagLayout());
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.3;
        gbc.anchor = GridBagConstraints.NORTH;
        card.add(lblTitle, gbc);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 56));
        lblValue.setForeground(textColor);
        gbc.gridy = 1;
        gbc.weighty = 0.7;
        gbc.anchor = GridBagConstraints.CENTER;
        card.add(lblValue, gbc);
        
        return card;
    }
}