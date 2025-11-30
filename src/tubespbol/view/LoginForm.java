package tubespbol.view;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    
    // Komponen Input dijadikan Global agar bisa diakses Controller
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JComboBox<String> cmbRole;
    private JButton btnLogin;

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

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(20, 60, 140),
                        getWidth(), getHeight(), new Color(46, 134, 193)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
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

        JPanel outerCard = wrapRoundedWithShadow(innerCard, 20);
        GridBagConstraints gbcCard = new GridBagConstraints();
        bg.add(outerCard, gbcCard);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // ========== LOGO & TITLE ==========
        JLabel lblIcon = new JLabel("\uD83D\uDCC5"); 
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        innerCard.add(lblIcon, gbc);

        JLabel lblTitle = new JLabel("SIGMA");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(31, 78, 121));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 3, 5);
        innerCard.add(lblTitle, gbc);
        
        JLabel lblSubtitle = new JLabel("Sistem Informasi General Manajemen Agreement");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSubtitle.setForeground(new Color(120, 120, 120));
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 5, 15, 5);
        innerCard.add(lblSubtitle, gbc);
        
        gbc.insets = new Insets(8, 5, 8, 5);

        // ========== INPUT FIELDS ==========
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setForeground(new Color(60, 60, 60));
        txtUser = new JTextField();
        txtUser.putClientProperty("JTextField.placeholderText", "Masukkan Username");
        txtUser.setPreferredSize(new Dimension(0, 35));

        gbc.gridy = 3; innerCard.add(lblUser, gbc);
        gbc.gridy = 4; innerCard.add(txtUser, gbc);

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPass.setForeground(new Color(60, 60, 60));
        txtPass = new JPasswordField();
        txtPass.putClientProperty("JTextField.placeholderText", "Masukkan Password");
        txtPass.setPreferredSize(new Dimension(0, 35));

        gbc.gridy = 5; innerCard.add(lblPass, gbc);
        gbc.gridy = 6; innerCard.add(txtPass, gbc);

        JLabel lblRole = new JLabel("Login sebagai");
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRole.setForeground(new Color(60, 60, 60));
        cmbRole = new JComboBox<>(new String[]{"Mahasiswa", "Dosen"});
        cmbRole.setPreferredSize(new Dimension(0, 35));

        gbc.gridy = 7; innerCard.add(lblRole, gbc);
        gbc.gridy = 8; innerCard.add(cmbRole, gbc);

        // ========== LOGIN BUTTON ==========
        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setPreferredSize(new Dimension(0, 42));
        btnLogin.setBackground(new Color(46, 134, 193));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        gbc.gridy = 9;
        gbc.insets = new Insets(15, 5, 8, 5);
        innerCard.add(btnLogin, gbc);

        // HAPUS SEMUA LOGIKA ACTION LISTENER DARI SINI
        // Controller yang akan memasangnya nanti
    }

    // ===== METHOD PENTING UNTUK CONTROLLER =====
    
    public String getUsername() {
        return txtUser.getText().trim();
    }

    public String getPassword() {
        return new String(txtPass.getPassword());
    }

    public String getRole() {
        return cmbRole.getSelectedItem().toString();
    }
    
    public JButton getBtnLogin() {
        return btnLogin;
    }

    // Controller akan memanggil ini untuk memasang "telinga"
    public void setLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);
    }

    // ===== UI HELPER =====
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
                g2.setColor(new Color(0, 0, 0, 30));
                for (int i = 0; i < 5; i++) {
                    g2.fillRoundRect(10 - i, 10 - i, getWidth() - 20 + (i * 2), 
                                   getHeight() - 20 + (i * 2), radius + i, radius + i);
                }
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, radius, radius);
                super.paintComponent(g);
            }
        };
    }
}