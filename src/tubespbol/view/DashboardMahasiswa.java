package tubespbol.view;

import javax.swing.*;

public class DashboardMahasiswa extends JFrame {

    public DashboardMahasiswa() {
        setTitle("Dashboard Mahasiswa");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel lbl = new JLabel("Halo Mahasiswa!", SwingConstants.CENTER);
        add(lbl);

        setVisible(true);
    }
}
