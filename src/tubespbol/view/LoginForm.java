package tubespbol.view;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginForm extends JFrame {
    
    // Komponen input dijadikan global agar bisa diakses Controller
    private JTextField txtUser;          // Input ID / username
    private JPasswordField txtPass;      // Input password
    private JComboBox<String> cmbRole;   // ComboBox role (Mahasiswa / Dosen)
    private JButton btnLogin;            // Tombol login
    
    // Menyimpan karakter default '*' dari password field
    private char defaultEchoChar;

    public LoginForm() {

        // Mengatur tampilan aplikasi menjadi FlatLightLaf agar UI lebih modern
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

        JPanel bg = new JPanel() {

            // Melukis background gradient + dekorasi lingkaran blur
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient biru → biru terang
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(20, 60, 140),
                        getWidth(), getHeight(), new Color(46, 134, 193)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Lingkaran dekoratif soft blur
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillOval(-50, -50, 200, 200);
                g2.fillOval(getWidth() - 150, getHeight() - 150, 200, 200);
            }
        };
        bg.setLayout(new GridBagLayout());
        add(bg, BorderLayout.CENTER);

        JPanel innerCard = new JPanel();
        innerCard.setPreferredSize(new Dimension(380, 480));
        innerCard.setBackground(Color.WHITE);
        innerCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        innerCard.setLayout(new GridBagLayout());
        innerCard.setOpaque(true);

        // Dibungkus panel shadow agar terlihat melayang
        JPanel outerCard = wrapRoundedWithShadow(innerCard, 20);
        
        GridBagConstraints gbcCard = new GridBagConstraints();
        bg.add(outerCard, gbcCard);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel lblIcon = new JLabel("📅"); // Icon emoji sederhana
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

        JLabel lblUser = new JLabel("ID");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setForeground(new Color(60, 60, 60));

        txtUser = new JTextField();
        txtUser.putClientProperty("JTextField.placeholderText", "Masukkan ID User");
        txtUser.setPreferredSize(new Dimension(0, 35));

        gbc.gridy = 3; innerCard.add(lblUser, gbc);
        gbc.gridy = 4; innerCard.add(txtUser, gbc);

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPass.setForeground(new Color(60, 60, 60));

        txtPass = new JPasswordField();
        txtPass.putClientProperty("JTextField.placeholderText", "Masukkan Password");
        txtPass.setPreferredSize(new Dimension(0, 35));

        // Simpan karakter default '*' untuk nanti dipakai saat toggle
        defaultEchoChar = txtPass.getEchoChar();

        // Tambahkan tombol "show/hide password"
        addPasswordShowHideToggle(txtPass);

        gbc.gridy = 5; innerCard.add(lblPass, gbc);
        gbc.gridy = 6; innerCard.add(txtPass, gbc);

        JLabel lblRole = new JLabel("Login sebagai");
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRole.setForeground(new Color(60, 60, 60));

        // Role: Mahasiswa / Dosen
        cmbRole = new JComboBox<>(new String[]{"Mahasiswa", "Dosen"});
        cmbRole.setPreferredSize(new Dimension(0, 35));

        gbc.gridy = 7; innerCard.add(lblRole, gbc);
        gbc.gridy = 8; innerCard.add(cmbRole, gbc);

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
    }

    private void addPasswordShowHideToggle(JPasswordField passwordField) {

        // Toggle button: Klik → tampilkan password | klik lagi → sembunyikan
        JToggleButton showHideButton = new JToggleButton(new AbstractAction("👁️") {

            private final String SHOW_TEXT = "🙈";  // Saat password ditampilkan
            private final String HIDE_TEXT = "👁️";  // Saat password disembunyikan

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JToggleButton btn = (JToggleButton) e.getSource();

                if (btn.isSelected()) {
                    // Menampilkan password
                    passwordField.setEchoChar((char) 0);
                    btn.setText(SHOW_TEXT);
                    btn.setToolTipText("Sembunyikan password");
                } else {
                    // Menyembunyikan kembali
                    passwordField.setEchoChar(defaultEchoChar);
                    btn.setText(HIDE_TEXT);
                    btn.setToolTipText("Tampilkan password");
                }
            }
        });

        // Tampilan tombol mata
        showHideButton.setFocusPainted(false);
        showHideButton.setMargin(new Insets(2, 5, 2, 5));
        showHideButton.setOpaque(false);
        showHideButton.setBorderPainted(false);
        showHideButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showHideButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        showHideButton.setForeground(new Color(100, 100, 100));

        // Memasukkan tombol mata ke dalam field (fitur FlatLaf)
        passwordField.putClientProperty("JTextField.trailingComponent", showHideButton);
        passwordField.putClientProperty("JComponent.minimumWidth", 0);

        // Efek hover (karena tombol berada di dalam password field)
        showHideButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!showHideButton.isSelected()) {
                     showHideButton.setForeground(new Color(46, 134, 193));
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!showHideButton.isSelected()) {
                    showHideButton.setForeground(new Color(100, 100, 100));
                }
            }
        });
    }


    public String getID() {
        return txtUser.getText().trim();  // Ambil ID user
    }

    public String getPassword() {
        return new String(txtPass.getPassword()); // Ambil password
    }

    public String getRole() {
        return cmbRole.getSelectedItem().toString(); // Ambil role
    }

    public JButton getBtnLogin() {
        return btnLogin; // Biar controller bisa pasang listener
    }

    public void setLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);
    }

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

                // Shadow berlapis-lapis
                g2.setColor(new Color(0, 0, 0, 30));
                for (int i = 0; i < 5; i++) {
                    g2.fillRoundRect(
                        10 - i, 10 - i,
                        getWidth() - 20 + (i * 2),
                        getHeight() - 20 + (i * 2),
                        radius + i, radius + i
                    );
                }

                // Isi kartu putih
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, radius, radius);

                super.paintComponent(g);
            }
        };
    }
}
