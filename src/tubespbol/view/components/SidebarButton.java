package tubespbol.view.components;

import javax.swing.*;
import java.awt.*;

public class SidebarButton extends JPanel {

    private JLabel iconLabel;
    private JLabel textLabel;
    private boolean hovered = false;

    private final Color normalColor = new Color(31, 78, 121);
    private final Color hoverColor = new Color(42, 107, 181);

    public SidebarButton(String text, String iconPath) {

        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        setPreferredSize(new Dimension(180, 45));
        setBackground(normalColor);

        URLCheck(iconPath); // debug kalau file hilang

        iconLabel = new JLabel(new ImageIcon(getClass().getResource(iconPath)));
        textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textLabel.setForeground(Color.WHITE);

        add(iconLabel);
        add(textLabel);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    private void URLCheck(String path) {
        if (getClass().getResource(path) == null) {
            System.out.println("ICON NOT FOUND: " + path);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        setBackground(hovered ? hoverColor : normalColor);
        super.paintComponent(g);
    }
}
