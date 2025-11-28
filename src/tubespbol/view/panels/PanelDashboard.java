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

        JLabel title = new JLabel("Dashboard Mahasiswa");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(41, 128, 185));
        title.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 20));

        add(title, BorderLayout.NORTH);

        JPanel cardArea = new JPanel();
        cardArea.setBackground(new Color(236, 240, 245));
        cardArea.setLayout(new GridLayout(1, 3, 20, 0));
        cardArea.setBorder(BorderFactory.createEmptyBorder(15, 25, 25, 25));

        // Hitung statistik sederhana
        int totalReq = 0;
        int totalDisetujui = 0;
        
        if (requestService != null) {
            try {
                java.util.List<Map<String, String>> reqs = requestService.getRequestMahasiswa(nim);
                totalReq = reqs.size();
                for (Map<String, String> r : reqs) {
                    if ("DITERIMA".equalsIgnoreCase(r.get("status"))) {
                        totalDisetujui++;
                    }
                }
            } catch (Exception e) {}
        }

        cardArea.add(createCard("Total Request", String.valueOf(totalReq), new Color(193, 220, 230), new Color(68, 130, 152)));
        cardArea.add(createCard("Disetujui", String.valueOf(totalDisetujui), new Color(190, 230, 201), new Color(69, 130, 75)));
        cardArea.add(createCard("Status", "Aktif", new Color(247, 207, 175), new Color(169, 110, 71)));

        add(cardArea, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String value, Color bg, Color textColor) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow effect
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 15, 15);
            }
        };
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(textColor);

        JLabel lblValue = new JLabel(value);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValue.setForeground(textColor);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }
}
