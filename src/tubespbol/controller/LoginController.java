package tubespbol.controller;

/*
 * LoginController
 * 
 * Controller yang menangani proses login pada aplikasi penjadwalan
 * pertemuan dosen dan mahasiswa.
 * 
 * Peran utama:
 * - Mengambil input dari LoginForm (View)
 * - Melakukan validasi data login
 * - Memanggil UserService untuk autentikasi
 * - Menampilkan dashboard sesuai role user
 */

import tubespbol.service.UserService;
import tubespbol.view.LoginForm;
import tubespbol.view.DashboardDosen;
import tubespbol.view.DashboardMahasiswa;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class LoginController {

    // Objek View untuk form login
    private LoginForm view;

    // Service untuk menangani proses login dan akses database
    private UserService userService;

    /*
     * Constructor LoginController
     * 
     * @param view LoginForm yang akan dikontrol
     */
    public LoginController(LoginForm view) {
        this.view = view;
        this.userService = new UserService();

        // Menghubungkan tombol Login dengan ActionListener
        this.view.setLoginListener(new LoginListener());
    }

    /*
     * LoginListener
     * 
     * Inner class yang menangani event ketika tombol Login ditekan
     */
    class LoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Mengambil input dari form login
                String user = view.getID();
                String pass = view.getPassword();
                String role = view.getRole();

                // Validasi input pengguna
                if (user.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        view,
                        "ID User dan Password tidak boleh kosong!",
                        "Input Error",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                // Menampilkan status loading pada tombol login
                view.getBtnLogin().setText("Loading...");
                view.getBtnLogin().setEnabled(false);

                // Menjalankan proses login di background agar UI tidak freeze
                new SwingWorker<Map<String, String>, Void>() {

                    @Override
                    protected Map<String, String> doInBackground() {
                        // Memanggil service untuk proses login
                        return userService.login(user, pass, role);
                    }

                    @Override
                    protected void done() {
                        try {
                            Map<String, String> userData = get();

                            if (userData != null) {
                                // Login berhasil, ambil ID user
                                String userId = userData.get("id");

                                // Menampilkan dashboard sesuai role
                                if (role.equals("Mahasiswa")) {
                                    new DashboardMahasiswa(userId, userData).setVisible(true);
                                } else {
                                    new DashboardDosen(userId, userData).setVisible(true);
                                }

                                // Menutup form login
                                view.dispose();

                            } else {
                                // Login gagal, tampilkan pesan error
                                JOptionPane.showMessageDialog(
                                    view,
                                    "Username/Password salah atau role tidak sesuai!",
                                    "Login Gagal",
                                    JOptionPane.ERROR_MESSAGE
                                );
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(
                                view,
                                "Terjadi kesalahan: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                            );
                        } finally {
                            // Mengembalikan kondisi tombol login seperti semula
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
