package tubespbol.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TimePicker extends JPanel {
    private JTextField timeField;
    private JButton clockButton;
    private int selectedHour = 8;
    private int selectedMinute = 0;
    
    public TimePicker() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 28));
        
        // Text field untuk menampilkan waktu
        timeField = new JTextField();
        timeField.setEditable(false);
        timeField.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
        timeField.setPreferredSize(new Dimension(200, 28));
        
        // Tombol jam
        clockButton = new JButton("🕐");
        clockButton.setPreferredSize(new Dimension(50, 28));
        clockButton.setFocusPainted(false);
        clockButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        clockButton.addActionListener(e -> showTimeDialog());
        
        add(timeField, BorderLayout.CENTER);
        add(clockButton, BorderLayout.EAST);
    }
    
    private void showTimeDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Pilih Waktu", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(320, 380);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(31, 78, 121));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel timeLabel = new JLabel(String.format("%02d:%02d", selectedHour, selectedMinute));
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(timeLabel);
        
        // Panel untuk jam dan menit
        JPanel timePickerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        timePickerPanel.setBackground(Color.WHITE);
        timePickerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel Jam
        JPanel hourPanel = new JPanel(new BorderLayout(5, 5));
        hourPanel.setBackground(Color.WHITE);
        
        JLabel hourLabel = new JLabel("Jam", SwingConstants.CENTER);
        hourLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        hourLabel.setForeground(new Color(31, 78, 121));
        
        JPanel hourGridPanel = new JPanel(new GridLayout(6, 4, 3, 3));
        hourGridPanel.setBackground(Color.WHITE);
        
        ButtonGroup hourGroup = new ButtonGroup();
        for (int i = 0; i < 24; i++) {
            final int hour = i;
            JToggleButton hourBtn = new JToggleButton(String.format("%02d", i));
            hourBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            hourBtn.setFocusPainted(false);
            hourBtn.setBackground(Color.WHITE);
            hourBtn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            
            if (i == selectedHour) {
                hourBtn.setSelected(true);
                hourBtn.setBackground(new Color(46, 134, 193));
                hourBtn.setForeground(Color.WHITE);
            }
            
            hourBtn.addActionListener(e -> {
                selectedHour = hour;
                timeLabel.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                
                // Update tampilan tombol
                for (Component c : hourGridPanel.getComponents()) {
                    if (c instanceof JToggleButton) {
                        JToggleButton btn = (JToggleButton) c;
                        if (btn.isSelected()) {
                            btn.setBackground(new Color(46, 134, 193));
                            btn.setForeground(Color.WHITE);
                        } else {
                            btn.setBackground(Color.WHITE);
                            btn.setForeground(Color.BLACK);
                        }
                    }
                }
            });
            
            // Hover effect
            hourBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!hourBtn.isSelected()) {
                        hourBtn.setBackground(new Color(230, 240, 250));
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    if (!hourBtn.isSelected()) {
                        hourBtn.setBackground(Color.WHITE);
                    }
                }
            });
            
            hourGroup.add(hourBtn);
            hourGridPanel.add(hourBtn);
        }
        
        JScrollPane hourScroll = new JScrollPane(hourGridPanel);
        hourScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        hourScroll.setPreferredSize(new Dimension(130, 250));
        
        hourPanel.add(hourLabel, BorderLayout.NORTH);
        hourPanel.add(hourScroll, BorderLayout.CENTER);
        
        // Panel Menit
        JPanel minutePanel = new JPanel(new BorderLayout(5, 5));
        minutePanel.setBackground(Color.WHITE);
        
        JLabel minuteLabel = new JLabel("Menit", SwingConstants.CENTER);
        minuteLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        minuteLabel.setForeground(new Color(31, 78, 121));
        
        JPanel minuteGridPanel = new JPanel(new GridLayout(20, 3, 3, 3));
        minuteGridPanel.setBackground(Color.WHITE);
        
        ButtonGroup minuteGroup = new ButtonGroup();
        for (int i = 0; i < 60; i++) {
            final int minute = i;
            JToggleButton minuteBtn = new JToggleButton(String.format("%02d", i));
            minuteBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            minuteBtn.setFocusPainted(false);
            minuteBtn.setBackground(Color.WHITE);
            minuteBtn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            
            if (i == selectedMinute) {
                minuteBtn.setSelected(true);
                minuteBtn.setBackground(new Color(46, 134, 193));
                minuteBtn.setForeground(Color.WHITE);
            }
            
            minuteBtn.addActionListener(e -> {
                selectedMinute = minute;
                timeLabel.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                
                // Update tampilan tombol
                for (Component c : minuteGridPanel.getComponents()) {
                    if (c instanceof JToggleButton) {
                        JToggleButton btn = (JToggleButton) c;
                        if (btn.isSelected()) {
                            btn.setBackground(new Color(46, 134, 193));
                            btn.setForeground(Color.WHITE);
                        } else {
                            btn.setBackground(Color.WHITE);
                            btn.setForeground(Color.BLACK);
                        }
                    }
                }
            });
            
            // Hover effect
            minuteBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!minuteBtn.isSelected()) {
                        minuteBtn.setBackground(new Color(230, 240, 250));
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    if (!minuteBtn.isSelected()) {
                        minuteBtn.setBackground(Color.WHITE);
                    }
                }
            });
            
            minuteGroup.add(minuteBtn);
            minuteGridPanel.add(minuteBtn);
        }
        
        JScrollPane minuteScroll = new JScrollPane(minuteGridPanel);
        minuteScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        minuteScroll.setPreferredSize(new Dimension(130, 250));
        
        minutePanel.add(minuteLabel, BorderLayout.NORTH);
        minutePanel.add(minuteScroll, BorderLayout.CENTER);
        
        timePickerPanel.add(hourPanel);
        timePickerPanel.add(minutePanel);
        
        // Panel tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton okButton = new JButton("OK");
        okButton.setBackground(new Color(46, 134, 193));
        okButton.setForeground(Color.WHITE);
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        okButton.setPreferredSize(new Dimension(100, 32));
        okButton.setFocusPainted(false);
        okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okButton.addActionListener(e -> {
            timeField.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
            dialog.dispose();
        });
        
        JButton cancelButton = new JButton("Batal");
        cancelButton.setBackground(new Color(220, 53, 69));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cancelButton.setPreferredSize(new Dimension(100, 32));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(timePickerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    public String getTime() {
        return timeField.getText();
    }
    
    public void setTime(int hour, int minute) {
        this.selectedHour = hour;
        this.selectedMinute = minute;
        timeField.setText(String.format("%02d:%02d", hour, minute));
    }
    
    public int getHour() {
        return selectedHour;
    }
    
    public int getMinute() {
        return selectedMinute;
    }
}
