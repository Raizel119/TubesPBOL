package tubespbol.view;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    public LoginForm() {

        // ========== FLATLAF (modern) ==========
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.out.println("Failed to init FlatLaf");
        }

        setTitle("Sistem Penjadwalan Pertemuan");
        setSize(500, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // ========== PANEL GRADIENT BACKGROUND ==========
        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient diagonal yang lebih modern
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(20, 60, 140),
                        getWidth(), getHeight(), new Color(46, 134, 193)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Tambahan efek circle di background
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillOval(-50, -50, 200, 200);
                g2.fillOval(getWidth() - 150, getHeight() - 150, 200, 200);
            }
        };
        bg.setLayout(new GridBagLayout());
        add(bg, BorderLayout.CENTER);

        // ========== INNER CARD (FORM CONTENT) ==========
        JPanel innerCard = new JPanel();
        innerCard.setPreferredSize(new Dimension(380, 480));
        innerCard.setBackground(Color.WHITE);
        innerCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        innerCard.setLayout(new GridBagLayout());
        innerCard.setOpaque(true);

        // ========== OUTER CARD (ROUNDED WRAPPER WITH SHADOW) ==========
        JPanel outerCard = wrapRoundedWithShadow(innerCard, 20);

        // Add outer card to background
        GridBagConstraints gbcCard = new GridBagConstraints();
        bg.add(outerCard, gbcCard);

        // ========== GRIDBAG FOR FORM INSIDE INNER CARD ==========
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // ========== LOGO/ICON ==========
        JLabel lblIcon = new JLabel("\uD83D\uDCC5"); // Calendar emoji
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        innerCard.add(lblIcon, gbc);

        // ========== TITLE ==========
        JLabel lblTitle = new JLabel("SIGMA");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(31, 78, 121));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 3, 5);
        innerCard.add(lblTitle, gbc);
        
        // ========== SUBTITLE ==========
        JLabel lblSubtitle = new JLabel("Sistem Informasi General Manajemen Agreement");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSubtitle.setForeground(new Color(120, 120, 120));
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 5, 15, 5);
        innerCard.add(lblSubtitle, gbc);
        
        gbc.insets = new Insets(8, 5, 8, 5);

        // ========== USERNAME ==========
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setForeground(new Color(60, 60, 60));
        JTextField txtUser = new JTextField();
        txtUser.putClientProperty("JTextField.placeholderText", "Masukkan Username");
        txtUser.setPreferredSize(new Dimension(0, 35));

        gbc.gridy = 3; innerCard.add(lblUser, gbc);
        gbc.gridy = 4; innerCard.add(txtUser, gbc);

        // ========== PASSWORD ==========
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPass.setForeground(new Color(60, 60, 60));
        JPasswordField txtPass = new JPasswordField();
        txtPass.putClientProperty("JTextField.placeholderText", "Masukkan Password");
        txtPass.setPreferredSize(new Dimension(0, 35));

        gbc.gridy = 5; innerCard.add(lblPass, gbc);
        gbc.gridy = 6; innerCard.add(txtPass, gbc);

        // ========== ROLE ==========
        JLabel lblRole = new JLabel("Login sebagai");
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRole.setForeground(new Color(60, 60, 60));
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"Mahasiswa", "Dosen"});
        cmbRole.setPreferredSize(new Dimension(0, 35));

        gbc.gridy = 7; innerCard.add(lblRole, gbc);
        gbc.gridy = 8; innerCard.add(cmbRole, gbc);

        // ========== LOGIN BUTTON ==========
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setPreferredSize(new Dimension(0, 42));
        btnLogin.setBackground(new Color(46, 134, 193));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(31, 97, 141));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(46, 134, 193));
            }
        });

        gbc.gridy = 9;
        gbc.insets = new Insets(15, 5, 8, 5);
        innerCard.add(btnLogin, gbc);

        // ========== LOGIN ACTION ==========
        btnLogin.addActionListener(e -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());
            String role = cmbRole.getSelectedItem().toString();

            if (user.equals("mhs") && pass.equals("123") && role.equals("Mahasiswa")) {
                new DashboardMahasiswa();
                dispose();
            } else if (user.equals("dsn") && pass.equals("123") && role.equals("Dosen")) {
                new DashboardDosen();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username/Password salah!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    // ===== WRAP PANEL MENJADI ROUNDED CARD WITH SHADOW =====
    private JPanel wrapRoundedWithShadow(JPanel panel, int radius) {
        return new JPanel(new BorderLayout()) {
            {
                setOpaque(false);
                setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                add(panel, BorderLayout.CENTER);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow effect
                g2.setColor(new Color(0, 0, 0, 30));
                for (int i = 0; i < 5; i++) {
                    g2.fillRoundRect(10 - i, 10 - i, getWidth() - 20 + (i * 2), 
                                   getHeight() - 20 + (i * 2), radius + i, radius + i);
                }
                
                // White card
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, radius, radius);
                super.paintComponent(g);
            }
        };
    }
}