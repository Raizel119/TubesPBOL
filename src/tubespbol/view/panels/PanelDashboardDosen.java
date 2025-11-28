package tubespbol.view.panels;

import tubespbol.service.JadwalService;
import tubespbol.service.RequestService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Locale;

public class PanelDashboardDosen extends JPanel {
    
    private String idDosen;
    private Map<String, String> userData;
    private JadwalService jadwalService;
    private RequestService requestService;
    
    // Components
    private JButton[] dayButtons;
    private JPanel jadwalListPanel;
    private LocalDate currentWeekStart;
    private int selectedDayIndex = -1;
    
    // Stats labels
    private JLabel lblTotalPermintaan;
    private JLabel lblDisetujui;
    
    // Constructor LAMA
    public PanelDashboardDosen() {
        this(null, null, null, null);
    }
    
    // Constructor BARU
    public PanelDashboardDosen(String idDosen, Map<String, String> userData, 
                               JadwalService jadwalService, RequestService requestService) {
        this.idDosen = idDosen;
        this.userData = userData;
        this.jadwalService = jadwalService;
        this.requestService = requestService;
        
        // Get current week start (Monday)
        this.currentWeekStart = getWeekStart(LocalDate.now());
        
        initComponents();
        
        if (jadwalService != null && requestService != null && idDosen != null) {
            loadDashboardData();
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 245));

        // ========== SCROLLABLE CONTENT ==========
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(new Color(236, 240, 245));
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(25, 30, 30, 30));
        
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(236, 240, 245));
        
        // Header
        mainContent.add(createHeaderWithProfile());
        mainContent.add(Box.createVerticalStrut(22));
        
        // Stats
        mainContent.add(createStatsPanel());
        mainContent.add(Box.createVerticalStrut(28));
        
        // Jadwal Section
        mainContent.add(createJadwalSection());
        
        contentWrapper.add(mainContent, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(contentWrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderWithProfile() {
        JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
        headerPanel.setBackground(new Color(236, 240, 245));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Left: Profile + Info
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        leftPanel.setBackground(new Color(236, 240, 245));
        
        // Photo - FIXED: Better size and centering
        JPanel photoPanel = new JPanel(new GridBagLayout());
        photoPanel.setBackground(Color.WHITE);
        photoPanel.setPreferredSize(new Dimension(95, 95));
        photoPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));
        
        JLabel lblPhoto = new JLabel("👨‍🏫");
        lblPhoto.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        lblPhoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblPhoto.setVerticalAlignment(SwingConstants.CENTER);
        photoPanel.add(lblPhoto);
        
        // Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(236, 240, 245));
        
        String nama = userData != null ? userData.get("nama") : "Dosen";
        String id = idDosen != null ? idDosen : "Unknown";
        
        JLabel lblTitle = new JLabel("Dashboard");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblName = new JLabel("Selamat datang, " + nama);
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblName.setForeground(new Color(80, 80, 80));
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblId = new JLabel("ID: " + id);
        lblId.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblId.setForeground(new Color(120, 120, 120));
        lblId.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(lblTitle);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(lblName);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(lblId);
        
        leftPanel.add(photoPanel);
        leftPanel.add(infoPanel);
        
        // Right: Refresh button
        JButton btnRefresh = new JButton("🔄 Refresh");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadDashboardData());
        
        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRefresh.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRefresh.setBackground(new Color(52, 152, 219));
            }
        });
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 18, 0));
        statsPanel.setBackground(new Color(236, 240, 245));
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.setPreferredSize(new Dimension(0, 128));
        
        statsPanel.add(createStatCard("Total Permintaan", "0", 
                                     new Color(193, 220, 230), new Color(68, 130, 152), true));
        
        statsPanel.add(createStatCard("Disetujui", "0", 
                                     new Color(190, 230, 201), new Color(69, 130, 75), false));
        
        return statsPanel;
    }
    
    private JPanel createStatCard(String title, String value, Color bg, Color textColor, boolean isPermintaan) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 12, 12);
            }
        };
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(3, 3, 3, 3),
                BorderFactory.createEmptyBorder(24, 28, 24, 28)
        ));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(textColor);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblValue.setForeground(textColor);
        
        if (isPermintaan) {
            lblTotalPermintaan = lblValue;
        } else {
            lblDisetujui = lblValue;
        }
        
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createJadwalSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(new Color(236, 240, 245));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Title - LEFT ALIGNED
        JLabel lblTitle = new JLabel("Jadwal Kuliah");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        
        section.add(lblTitle);
        
        // Day buttons - CONSISTENT SPACING
        JPanel daysPanel = new JPanel(new GridLayout(1, 5, 15, 0));
        daysPanel.setBackground(new Color(236, 240, 245));
        daysPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        daysPanel.setPreferredSize(new Dimension(0, 92));
        
        dayButtons = new JButton[5];
        String[] dayNames = {"Sen", "Sel", "Rab", "Kam", "Jum"};
        
        for (int i = 0; i < 5; i++) {
            final int index = i;
            JButton btn = createDayButton(dayNames[i], "", false);
            btn.addActionListener(e -> selectDay(index));
            dayButtons[i] = btn;
            daysPanel.add(btn);
        }
        
        section.add(daysPanel);
        section.add(Box.createVerticalStrut(24));
        
        // Jadwal list
        jadwalListPanel = new JPanel();
        jadwalListPanel.setLayout(new BoxLayout(jadwalListPanel, BoxLayout.Y_AXIS));
        jadwalListPanel.setBackground(new Color(236, 240, 245));
        jadwalListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        section.add(jadwalListPanel);
        
        return section;
    }
    
    private JButton createDayButton(String dayName, String date, boolean isSelected) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (isSelected) {
                    GradientPaint gp = new GradientPaint(
                        0, 0, new Color(39, 174, 96),
                        0, getHeight(), new Color(34, 153, 84)
                    );
                    g2.setPaint(gp);
                } else if (getModel().isPressed()) {
                    g2.setColor(new Color(230, 230, 230));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(248, 248, 248));
                } else {
                    g2.setColor(Color.WHITE);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                if (isSelected) {
                    g2.setColor(new Color(34, 153, 84));
                    g2.setStroke(new BasicStroke(2));
                } else {
                    g2.setColor(new Color(220, 220, 220));
                    g2.setStroke(new BasicStroke(1));
                }
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 15, 15);
            }
        };
        
        btn.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 8, 10, 8);
        
        JLabel lblDay = new JLabel(dayName);
        lblDay.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDay.setForeground(isSelected ? Color.WHITE : new Color(50, 50, 50));
        gbc.gridy = 0;
        btn.add(lblDay, gbc);
        
        if (!date.isEmpty()) {
            JLabel lblDate = new JLabel(date);
            lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblDate.setForeground(isSelected ? new Color(240, 255, 240) : new Color(120, 120, 120));
            gbc.gridy = 1;
            gbc.insets = new Insets(3, 8, 10, 8);
            btn.add(lblDate, gbc);
        }
        
        btn.setPreferredSize(new Dimension(0, 92));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return btn;
    }
    
    private JPanel createJadwalCard(Map<String, String> jadwal) {
        JPanel card = new JPanel(new BorderLayout(22, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 12, 12);
            }
        };
        
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(2, 2, 2, 2),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(22, 24, 22, 24)
            )
        ));
        card.setPreferredSize(new Dimension(0, 95));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Time panel - FIXED: Better icon sizing
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        timePanel.setBackground(Color.WHITE);
        timePanel.setPreferredSize(new Dimension(140, 70));
        
        JLabel lblTimeIcon = new JLabel("⏰");
        lblTimeIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        lblTimeIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTime = new JLabel(jadwal.get("jam_mulai") + " - " + jadwal.get("jam_selesai"));
        lblTime.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTime.setForeground(new Color(52, 152, 219));
        lblTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        timePanel.add(Box.createVerticalGlue());
        timePanel.add(lblTimeIcon);
        timePanel.add(Box.createVerticalStrut(6));
        timePanel.add(lblTime);
        timePanel.add(Box.createVerticalGlue());
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel lblJudul = new JLabel(jadwal.get("judul"));
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblJudul.setForeground(new Color(40, 40, 40));
        
        JLabel lblKeterangan = new JLabel(jadwal.get("keterangan"));
        lblKeterangan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblKeterangan.setForeground(new Color(120, 120, 120));
        
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(lblJudul);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(lblKeterangan);
        infoPanel.add(Box.createVerticalGlue());
        
        card.add(timePanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void loadDashboardData() {
        if (idDosen == null || jadwalService == null || requestService == null) return;
        
        updateDayButtons();
        loadStats();
        
        LocalDate today = LocalDate.now();
        int todayIndex = today.getDayOfWeek().getValue() - 1;
        
        if (todayIndex >= 0 && todayIndex < 5) {
            selectDay(todayIndex);
        } else {
            selectDay(0);
        }
    }
    
    private void updateDayButtons() {
        String[] dayNames = {"Sen", "Sel", "Rab", "Kam", "Jum"};
        
        for (int i = 0; i < 5; i++) {
            LocalDate date = currentWeekStart.plusDays(i);
            String dateStr = date.format(DateTimeFormatter.ofPattern("dd MMM", new Locale("id")));
            
            final int index = i;
            JButton btn = createDayButton(dayNames[i], dateStr, false);
            btn.addActionListener(e -> selectDay(index));
            
            Container parent = dayButtons[i].getParent();
            parent.remove(dayButtons[i]);
            dayButtons[i] = btn;
            parent.add(btn, i);
        }
        
        dayButtons[0].getParent().revalidate();
        dayButtons[0].getParent().repaint();
    }
    
    private void selectDay(int dayIndex) {
        selectedDayIndex = dayIndex;
        
        String[] dayNames = {"Sen", "Sel", "Rab", "Kam", "Jum"};
        for (int i = 0; i < 5; i++) {
            LocalDate date = currentWeekStart.plusDays(i);
            String dateStr = date.format(DateTimeFormatter.ofPattern("dd MMM", new Locale("id")));
            
            final int index = i;
            JButton btn = createDayButton(dayNames[i], dateStr, i == dayIndex);
            btn.addActionListener(e -> selectDay(index));
            
            Container parent = dayButtons[i].getParent();
            parent.remove(dayButtons[i]);
            dayButtons[i] = btn;
            parent.add(btn, i);
        }
        
        dayButtons[0].getParent().revalidate();
        dayButtons[0].getParent().repaint();
        
        loadJadwalForDay(dayIndex);
    }
    
    private void loadJadwalForDay(int dayIndex) {
        if (jadwalService == null || idDosen == null) return;
        
        LocalDate date = currentWeekStart.plusDays(dayIndex);
        
        SwingWorker<List<Map<String, String>>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Map<String, String>> doInBackground() {
                String tanggal = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return jadwalService.getJadwalLengkap(idDosen, tanggal);
            }
            
            @Override
            protected void done() {
                try {
                    List<Map<String, String>> jadwalList = get();
                    
                    jadwalListPanel.removeAll();
                    
                    if (jadwalList.isEmpty()) {
                        JPanel emptyPanel = new JPanel(new GridBagLayout());
                        emptyPanel.setBackground(Color.WHITE);
                        emptyPanel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                            BorderFactory.createEmptyBorder(35, 30, 35, 30)
                        ));
                        emptyPanel.setPreferredSize(new Dimension(0, 120));
                        emptyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
                        emptyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                        
                        JLabel lblEmpty = new JLabel("Tidak ada jadwal di hari ini");
                        lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 15));
                        lblEmpty.setForeground(new Color(150, 150, 150));
                        
                        emptyPanel.add(lblEmpty);
                        jadwalListPanel.add(emptyPanel);
                    } else {
                        for (Map<String, String> jadwal : jadwalList) {
                            jadwalListPanel.add(createJadwalCard(jadwal));
                            jadwalListPanel.add(Box.createVerticalStrut(14));
                        }
                    }
                    
                    jadwalListPanel.revalidate();
                    jadwalListPanel.repaint();
                } catch (Exception e) {
                    System.err.println("Error loading jadwal: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }
    
    private void loadStats() {
        if (requestService == null || idDosen == null) return;
        
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            int totalPermintaan = 0;
            int totalDisetujui = 0;
            
            @Override
            protected Void doInBackground() {
                try {
                    List<Map<String, String>> requests = requestService.getRequestDosen(idDosen);
                    totalPermintaan = requests.size();
                    totalDisetujui = requestService.countAcceptedRequests(idDosen);
                } catch (Exception e) {
                    System.err.println("Error loading stats: " + e.getMessage());
                }
                return null;
            }
            
            @Override
            protected void done() {
                if (lblTotalPermintaan != null) {
                    lblTotalPermintaan.setText(String.valueOf(totalPermintaan));
                }
                if (lblDisetujui != null) {
                    lblDisetujui.setText(String.valueOf(totalDisetujui));
                }
            }
        };
        worker.execute();
    }
    
    private LocalDate getWeekStart(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        int daysToSubtract = day.getValue() - DayOfWeek.MONDAY.getValue();
        return date.minusDays(daysToSubtract);
    }
    
    public void refresh() {
        loadDashboardData();
    }
}