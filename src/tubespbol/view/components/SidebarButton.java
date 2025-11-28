package tubespbol.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class SidebarButton extends JPanel {

    private JLabel iconLabel;
    private JLabel textLabel;
    private boolean hovered = false;
    private boolean selected = false;

    private final Color normalColor = new Color(255, 255, 255, 20);
    private final Color hoverColor = new Color(255, 255, 255, 40);
    private final Color selectedColor = new Color(255, 255, 255, 60);

    public SidebarButton(String text, String iconPath) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        setPreferredSize(new Dimension(220, 45));
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Load and resize icon, ALWAYS give fixed size (e.g. 24x24) for consistent look
        ImageIcon icon = getIconResized(iconPath, 24, 24);

        iconLabel = new JLabel(icon);
        textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        textLabel.setForeground(Color.WHITE);

        add(iconLabel);
        add(Box.createHorizontalStrut(8));
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
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw rounded background
        if (selected) {
            g2.setColor(selectedColor);
        } else if (hovered) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(normalColor);
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        
        super.paintComponent(g);
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }
}
