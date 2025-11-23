package tubespbol.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class SidebarButton extends JPanel {

    private JLabel iconLabel;
    private JLabel textLabel;
    private boolean hovered = false;

    private final Color normalColor = new Color(31, 78, 121);
    private final Color hoverColor = new Color(42, 107, 181);

    public SidebarButton(String text, String iconPath) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 7)); // Padding bawah dikurangi biar centering lebih baik
        setPreferredSize(new Dimension(180, 44));
        setBackground(normalColor);

        // Load and resize icon, ALWAYS give fixed size (e.g. 28x28) for consistent look
        ImageIcon icon = getIconResized(iconPath, 28, 28);

        iconLabel = new JLabel(icon);
        textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        textLabel.setForeground(Color.WHITE);

        add(iconLabel);
        add(Box.createHorizontalStrut(6)); // Space antara ikon & teks
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

    // Helper: Load and resize icon safely
    private ImageIcon getIconResized(String path, int w, int h) {
        URL url = getClass().getResource(path);
        if (url == null) {
            System.out.println("ICON NOT FOUND: " + path);
            return new ImageIcon(new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB));
        }
        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    @Override
    protected void paintComponent(Graphics g) {
        setBackground(hovered ? hoverColor : normalColor);
        super.paintComponent(g);
    }
}
