package tubespbol.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

public class SidebarButton extends JPanel {

    private JLabel iconLabel;
    private JLabel textLabel;

    private boolean hovered = false;
    private boolean selected = false;

    // Warna dasar
    private Color baseColor = new Color(255, 255, 255, 20);
    private Color hoverColor = new Color(255, 255, 255, 50);
    private Color selectedColor = new Color(255, 255, 255, 125);

    // Warna transisi smooth
    private float transition = 0f;
    private Timer timer;

    public SidebarButton(String text, String iconPath) {
        setPreferredSize(new Dimension(220, 50));
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 14, 12));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon ukuran konsisten 22px
        ImageIcon icon = getIconResized(iconPath, 22, 22);

        iconLabel = new JLabel(icon);
        textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        textLabel.setForeground(Color.WHITE);

        add(iconLabel);
        add(Box.createHorizontalStrut(10));
        add(textLabel);

        timer = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float target = (hovered || selected) ? 1f : 0f;
                float speed = 0.08f;

                if (Math.abs(transition - target) > 0.01) {
                    transition += (target - transition) * speed;
                    repaint();
                }
            }
        });
        timer.start();

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                hovered = true;
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hovered = false;
            }
        });
    }

    private ImageIcon getIconResized(String path, int w, int h) {
        URL url = getClass().getResource(path);
        if (url == null) {
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

        Color targetColor = selected ? selectedColor : (hovered ? hoverColor : baseColor);

        int r = (int) (baseColor.getRed() + (targetColor.getRed() - baseColor.getRed()) * transition);
        int gC = (int) (baseColor.getGreen() + (targetColor.getGreen() - baseColor.getGreen()) * transition);
        int b = (int) (baseColor.getBlue() + (targetColor.getBlue() - baseColor.getBlue()) * transition);
        int a = (int) (baseColor.getAlpha() + (targetColor.getAlpha() - baseColor.getAlpha()) * transition);

        g2.setColor(new Color(r, gC, b, a));

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

        if (selected) {
            g2.setColor(new Color(255, 255, 255, 60));
            g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 18, 18);
        }

        super.paintComponent(g);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }
}
