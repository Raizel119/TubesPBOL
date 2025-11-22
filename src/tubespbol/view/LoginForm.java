package tubespbol.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginForm extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JComboBox<String> cmbRole;

    public LoginForm() {
        setTitle("Sistem Penjadwalan Pertemuan");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblTitle = new JLabel("Login Sistem");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(110, 10, 200, 30);
        add(lblTitle);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(30, 60, 100, 20);
        add(lblUser);

        txtUser = new JTextField();
        txtUser.setBounds(120, 60, 170, 22);
        add(txtUser);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(30, 90, 100, 20);
        add(lblPass);

        txtPass = new JPasswordField();
        txtPass.setBounds(120, 90, 170, 22);
        add(txtPass);

        JLabel lblRole = new JLabel("Login sebagai:");
        lblRole.setBounds(30, 120, 100, 20);
        add(lblRole);

        cmbRole = new JComboBox<>(new String[]{"Mahasiswa", "Dosen"});
        cmbRole.setBounds(120, 120, 170, 22);
        add(cmbRole);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(120, 160, 100, 28);
        add(btnLogin);

        // Aksi tombol login
        btnLogin.addActionListener(e -> loginAction());

        setVisible(true);
    }

    private void loginAction() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());
        String role = cmbRole.getSelectedItem().toString();

        // Sementara login hardcode (supaya aplikasi bisa jalan dulu)
        if (user.equals("mhs") && pass.equals("123") && role.equals("Mahasiswa")) {
            JOptionPane.showMessageDialog(this, "Login Mahasiswa Berhasil!");
            new DashboardMahasiswa();
            dispose();

        } else if (user.equals("dsn") && pass.equals("123") && role.equals("Dosen")) {
            JOptionPane.showMessageDialog(this, "Login Dosen Berhasil!");
            new DashboardDosen();
            dispose();

        } else {
            JOptionPane.showMessageDialog(this, "Login gagal!");
        }
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}
