package tubespbol.controller;

import tubespbol.service.UserService;
import tubespbol.view.LoginForm;
import tubespbol.view.DashboardDosen;
import tubespbol.view.DashboardMahasiswa;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class LoginController {

    private LoginForm view;
    private UserService userService;

    public LoginController(LoginForm view) {
        this.view = view;
        this.userService = new UserService();
        
        // Pasang telinga (listener) ke tombol login di View
        this.view.setLoginListener(new LoginListener());
    }

    // Class dalam untuk menangani klik tombol
    class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // 1. Ambil data dari View
                String user = view.getUsername();
                String pass = view.getPassword();
                String role = view.getRole();

                // 2. Validasi input
                if (user.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(view, 
                        "Username dan Password tidak boleh kosong!", 
                        "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 3. UI Feedback (Loading)
                view.getBtnLogin().setText("Loading...");
                view.getBtnLogin().setEnabled(false);

                // 4. Proses Database di Background (SwingWorker)
                new SwingWorker<Map<String, String>, Void>() {
                    @Override
                    protected Map<String, String> doInBackground() {
                        return userService.login(user, pass, role);
                    }

                    @Override
                    protected void done() {
                        try {
                            Map<String, String> userData = get();
                            
                            if (userData != null) {
                                // === LOGIN SUKSES ===
                                String userId = userData.get("id");
                                
                                if (role.equals("Mahasiswa")) {
                                    new DashboardMahasiswa(userId, userData).setVisible(true);
                                } else {
                                    new DashboardDosen(userId, userData).setVisible(true);
                                }
                                view.dispose(); // Tutup jendela login
                            } else {
                                // === LOGIN GAGAL (PESAN ORIGINAL DIKEMBALIKAN) ===
                                JOptionPane.showMessageDialog(view, 
                                    "Username/Password salah atau role tidak sesuai!\n\n" +
                                    "Untuk testing, gunakan:\n" +
                                    "Dosen: 12345678 / password123\n" +
                                    "Mahasiswa: 2021110001 / password123",
                                    "Login Gagal", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
                        } finally {
                            // Kembalikan tombol seperti semula
                            view.getBtnLogin().setText("Login");
                            view.getBtnLogin().setEnabled(true);
                        }
                    }
                }.execute();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}