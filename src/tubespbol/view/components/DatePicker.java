package tubespbol.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePicker extends JPanel {
    private JTextField dateField;
    private JButton calendarButton;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    
    public DatePicker() {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 28));
        
        // Text field untuk menampilkan tanggal
        dateField = new JTextField();
        dateField.setEditable(false);
        dateField.setText(dateFormat.format(calendar.getTime()));
        dateField.setPreferredSize(new Dimension(200, 28));
        
        // Tombol kalender
        calendarButton = new JButton("📅");
        calendarButton.setPreferredSize(new Dimension(50, 28));
        calendarButton.setFocusPainted(false);
        calendarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        calendarButton.addActionListener(e -> showCalendarDialog());
        
        add(dateField, BorderLayout.CENTER);
        add(calendarButton, BorderLayout.EAST);
    }
    
    private void showCalendarDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Pilih Tanggal", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(320, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel atas untuk bulan dan tahun
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(31, 78, 121));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton prevMonth = new JButton("◀");
        JButton nextMonth = new JButton("▶");
        prevMonth.setFocusPainted(false);
        nextMonth.setFocusPainted(false);
        prevMonth.setBackground(new Color(46, 134, 193));
        nextMonth.setBackground(new Color(46, 134, 193));
        prevMonth.setForeground(Color.WHITE);
        nextMonth.setForeground(Color.WHITE);
        
        // ComboBox untuk bulan dan tahun
        String[] months = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", 
                          "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        JComboBox<String> monthCombo = new JComboBox<>(months);
        monthCombo.setSelectedIndex(calendar.get(Calendar.MONTH));
        
        JComboBox<Integer> yearCombo = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 5; i <= currentYear + 10; i++) {
            yearCombo.addItem(i);
        }
        yearCombo.setSelectedItem(calendar.get(Calendar.YEAR));
        
        JPanel monthYearPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        monthYearPanel.setOpaque(false);
        monthYearPanel.add(monthCombo);
        monthYearPanel.add(yearCombo);
        
        headerPanel.add(prevMonth, BorderLayout.WEST);
        headerPanel.add(monthYearPanel, BorderLayout.CENTER);
        headerPanel.add(nextMonth, BorderLayout.EAST);
        
        // Panel untuk hari dalam seminggu
        JPanel daysHeaderPanel = new JPanel(new GridLayout(1, 7, 2, 2));
        daysHeaderPanel.setBackground(Color.WHITE);
        String[] dayNames = {"Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab"};
        for (String day : dayNames) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 11));
            label.setForeground(new Color(31, 78, 121));
            daysHeaderPanel.add(label);
        }
        
        // Panel untuk tanggal
        JPanel daysPanel = new JPanel(new GridLayout(6, 7, 2, 2));
        daysPanel.setBackground(Color.WHITE);
        
        // Fungsi untuk update kalender
        Runnable updateCalendar = () -> {
            daysPanel.removeAll();
            
            Calendar tempCal = (Calendar) calendar.clone();
            tempCal.set(Calendar.DAY_OF_MONTH, 1);
            
            int firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 1;
            int daysInMonth = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);
            
            // Empty cells sebelum tanggal pertama
            for (int i = 0; i < firstDayOfWeek; i++) {
                daysPanel.add(new JLabel(""));
            }
            
            // Tombol untuk setiap tanggal
            for (int day = 1; day <= daysInMonth; day++) {
                JButton dayButton = new JButton(String.valueOf(day));
                dayButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                dayButton.setFocusPainted(false);
                dayButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
                dayButton.setBackground(Color.WHITE);
                dayButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                // Highlight hari ini
                Calendar today = Calendar.getInstance();
                if (day == calendar.get(Calendar.DAY_OF_MONTH) &&
                    calendar.get(Calendar.MONTH) == tempCal.get(Calendar.MONTH) &&
                    calendar.get(Calendar.YEAR) == tempCal.get(Calendar.YEAR)) {
                    dayButton.setBackground(new Color(46, 134, 193));
                    dayButton.setForeground(Color.WHITE);
                    dayButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
                }
                
                final int selectedDay = day;
                dayButton.addActionListener(e -> {
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                    dateField.setText(dateFormat.format(calendar.getTime()));
                    dialog.dispose();
                });
                
                // Hover effect
                dayButton.addMouseListener(new MouseAdapter() {
                    Color originalBg = dayButton.getBackground();
                    
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (originalBg.equals(Color.WHITE)) {
                            dayButton.setBackground(new Color(230, 240, 250));
                        }
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (originalBg.equals(Color.WHITE)) {
                            dayButton.setBackground(Color.WHITE);
                        }
                    }
                });
                
                daysPanel.add(dayButton);
            }
            
            daysPanel.revalidate();
            daysPanel.repaint();
        };
        
        // Event listeners untuk navigasi
        prevMonth.addActionListener(e -> {
            int month = monthCombo.getSelectedIndex();
            if (month == 0) {
                monthCombo.setSelectedIndex(11);
                yearCombo.setSelectedItem((Integer) yearCombo.getSelectedItem() - 1);
            } else {
                monthCombo.setSelectedIndex(month - 1);
            }
        });
        
        nextMonth.addActionListener(e -> {
            int month = monthCombo.getSelectedIndex();
            if (month == 11) {
                monthCombo.setSelectedIndex(0);
                yearCombo.setSelectedItem((Integer) yearCombo.getSelectedItem() + 1);
            } else {
                monthCombo.setSelectedIndex(month + 1);
            }
        });
        
        monthCombo.addActionListener(e -> {
            calendar.set(Calendar.MONTH, monthCombo.getSelectedIndex());
            updateCalendar.run();
        });
        
        yearCombo.addActionListener(e -> {
            calendar.set(Calendar.YEAR, (Integer) yearCombo.getSelectedItem());
            updateCalendar.run();
        });
        
        // Panel container untuk days header dan days
        JPanel calendarPanel = new JPanel(new BorderLayout(0, 5));
        calendarPanel.setBackground(Color.WHITE);
        calendarPanel.add(daysHeaderPanel, BorderLayout.NORTH);
        calendarPanel.add(daysPanel, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(calendarPanel, BorderLayout.CENTER);
        
        // Tombol tutup
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("Tutup");
        closeButton.setBackground(new Color(220, 53, 69));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        updateCalendar.run();
        dialog.setVisible(true);
    }
    
    public Date getDate() {
        return calendar.getTime();
    }
    
    public void setDate(Date date) {
        calendar.setTime(date);
        dateField.setText(dateFormat.format(date));
    }
    
    public String getDateString() {
        return dateField.getText();
    }
}
