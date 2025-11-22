package tubespbol.view;

import javax.swing.*;

public class DashboardDosen extends JFrame {

    public DashboardDosen() {
        setTitle("Dashboard Dosen");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel lbl = new JLabel("Halo Dosen!", SwingConstants.CENTER);
        add(lbl);

        setVisible(true);
    }
}