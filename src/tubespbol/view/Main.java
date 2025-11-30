package tubespbol.view;

import tubespbol.controller.LoginController;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Buat Tampilan (View)
            LoginForm view = new LoginForm();
            
            // 2. Buat Otak (Controller) dan pasang ke View
            new LoginController(view);
            
            // 3. Tampilkan
            view.setVisible(true);
        });
    }
}