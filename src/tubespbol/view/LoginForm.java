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
        setSize(460, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // ========== PANEL GRADIENT BACKGROUND ==========
        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(32, 82, 149),
                        0, getHeight(), new Color(119, 204, 255)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());
        add(bg, BorderLayout.CENTER);

        // ========== INNER CARD (FORM CONTENT) ==========
        JPanel innerCard = new JPanel();
        innerCard.setPreferredSize(new Dimension(350, 420));
        innerCard.setBackground(Color.WHITE);
        innerCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        innerCard.setLayout(new GridBagLayout());
        innerCard.setOpaque(true);

        // ========== OUTER CARD (ROUNDED WRAPPER) ==========
        JPanel outerCard = wrapRounded(innerCard, 25);

        // Add outer card to background
        GridBagConstraints gbcCard = new GridBagConstraints();
        bg.add(outerCard, gbcCard);

        // ========== GRIDBAG FOR FORM INSIDE INNER CARD ==========
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 5, 12, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // ========== TITLE ==========
        JLabel lblTitle = new JLabel("Login Sistem");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 0;
        innerCard.add(lblTitle, gbc);  // ✅ Tambahkan ke innerCard

        // ========== USERNAME ==========
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField txtUser = new JTextField();
        txtUser.putClientProperty("JTextField.placeholderText", "Masukkan Username");

        gbc.gridy = 1; innerCard.add(lblUser, gbc);
        gbc.gridy = 2; innerCard.add(txtUser, gbc);

        // ========== PASSWORD ==========
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JPasswordField txtPass = new JPasswordField();
        txtPass.putClientProperty("JTextField.placeholderText", "Masukkan Password");

        gbc.gridy = 3; innerCard.add(lblPass, gbc);
        gbc.gridy = 4; innerCard.add(txtPass, gbc);

        // ========== ROLE ==========
        JLabel lblRole = new JLabel("Login sebagai");
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"Mahasiswa", "Dosen"});

        gbc.gridy = 5; innerCard.add(lblRole, gbc);
        gbc.gridy = 6; innerCard.add(cmbRole, gbc);

        // ========== LOGIN BUTTON ==========
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setPreferredSize(new Dimension(0, 42));
        btnLogin.setBackground(new Color(46, 134, 193));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        gbc.gridy = 7;
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

    // ===== WRAP PANEL MENJADI ROUNDED CARD =====
    private JPanel wrapRounded(JPanel panel, int radius) {
        return new JPanel(new BorderLayout()) {
            {
                setOpaque(false);
                add(panel, BorderLayout.CENTER);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                super.paintComponent(g);
            }
        };
    }
}