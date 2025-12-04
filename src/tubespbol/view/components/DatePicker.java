// tubespbol.view.components.DatePicker
package tubespbol.view.components;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePicker extends JPanel {
    private JTextField dateField;
    private JButton calendarButton;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private Date minDate; // Tanggal minimum (hari ini)
    private Date maxDate; // Tanggal maksimum (30 hari dari sekarang)
    
    public DatePicker() {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format SQL
        
        // Set batasan tanggal: hari ini sampai 30 hari ke depan
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        minDate = today.getTime();
        
        Calendar maxCal = (Calendar) today.clone();
        maxCal.add(Calendar.DAY_OF_MONTH, 30);
        maxDate = maxCal.getTime();
        
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 28));
        
        // Text field untuk menampilkan tanggal
        dateField = new JTextField();
        dateField.setEditable(false);
        dateField.setText(dateFormat.format(calendar.getTime()));
        dateField.setPreferredSize(new Dimension(200, 28));
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Tombol kalender
        calendarButton = new JButton("📅");
        calendarButton.setPreferredSize(new Dimension(50, 28));
        calendarButton.setFocusPainted(false);
        calendarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        calendarButton.addActionListener(e -> showCalendarDialog());
        
        add(dateField, BorderLayout.CENTER);
        add(calendarButton, BorderLayout.EAST);
    }
    
    // Method untuk cek apakah tanggal dalam range yang diizinkan
    private boolean isDateInRange(Calendar cal) {
        Calendar checkCal = (Calendar) cal.clone();
        checkCal.set(Calendar.HOUR_OF_DAY, 0);
        checkCal.set(Calendar.MINUTE, 0);
        checkCal.set(Calendar.SECOND, 0);
        checkCal.set(Calendar.MILLISECOND, 0);
        
        Date checkDate = checkCal.getTime();
        return !checkDate.before(minDate) && !checkDate.after(maxDate);
    }
    
    private void showCalendarDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Pilih Tanggal", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(340, 420); // Ukuran diperbesar untuk info tambahan
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel info batasan tanggal
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setBackground(new Color(255, 243, 205));
        infoPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 193, 7), 1));
        JLabel infoLabel = new JLabel("📌 Hanya dapat memilih tanggal dalam 30 hari ke depan");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setForeground(new Color(133, 100, 4));
        infoPanel.add(infoLabel);
        
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
        prevMonth.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextMonth.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
        
        // Panel Header Hari (Min, Sen, ...)
        JPanel daysHeaderPanel = new JPanel(new GridLayout(1, 7, 2, 2)); 
        daysHeaderPanel.setBackground(Color.WHITE);
        String[] dayNames = {"Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab"};
        for (String day : dayNames) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(new Color(31, 78, 121));
            daysHeaderPanel.add(label);
        }
        
        // Panel Tanggal
        JPanel daysPanel = new JPanel(new GridLayout(0, 7, 2, 2));
        daysPanel.setBackground(Color.WHITE);
        
        // Fungsi untuk update kalender
        Runnable updateCalendar = () -> {
            daysPanel.removeAll();
            
            Calendar tempCal = (Calendar) calendar.clone();
            tempCal.set(Calendar.DAY_OF_MONTH, 1); // Set ke tanggal 1
            
            // Hitung offset hari (Minggu = 1, Sabtu = 7 di Java)
            int dayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK);
            int emptyCells = dayOfWeek - 1;
            
            int daysInMonth = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);
            
            // Isi sel kosong di awal
            for (int i = 0; i < emptyCells; i++) {
                daysPanel.add(new JLabel(""));
            }
            
            // Isi tombol tanggal
            for (int day = 1; day <= daysInMonth; day++) {
                JButton dayButton = new JButton(String.valueOf(day));
                dayButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                dayButton.setFocusPainted(false);
                dayButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
                dayButton.setBackground(Color.WHITE);
                
                // Cek apakah tanggal ini dalam range yang diizinkan
                Calendar checkCal = (Calendar) tempCal.clone();
                checkCal.set(Calendar.DAY_OF_MONTH, day);
                boolean inRange = isDateInRange(checkCal);
                
                if (inRange) {
                    dayButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    
                    // Highlight hari yang terpilih
                    if (day == calendar.get(Calendar.DAY_OF_MONTH) &&
                        tempCal.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                        tempCal.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                        dayButton.setBackground(new Color(46, 134, 193));
                        dayButton.setForeground(Color.WHITE);
                        dayButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    }
                    
                    final int selectedDay = day;
                    dayButton.addActionListener(e -> {
                        calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                        calendar.set(Calendar.MONTH, monthCombo.getSelectedIndex());
                        calendar.set(Calendar.YEAR, (Integer) yearCombo.getSelectedItem());
                        
                        dateField.setText(dateFormat.format(calendar.getTime()));
                        dialog.dispose();
                    });
                } else {
                    // Disable tanggal yang tidak dalam range
                    dayButton.setEnabled(false);
                    dayButton.setBackground(new Color(240, 240, 240));
                    dayButton.setForeground(new Color(180, 180, 180));
                    dayButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
                
                daysPanel.add(dayButton);
            }
            
            daysPanel.revalidate();
            daysPanel.repaint();
        };
        
        // Event listeners
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
        
        JPanel calendarPanel = new JPanel(new BorderLayout(0, 5));
        calendarPanel.setBackground(Color.WHITE);
        calendarPanel.add(daysHeaderPanel, BorderLayout.NORTH);
        calendarPanel.add(daysPanel, BorderLayout.CENTER);
        
        // Panel utama kalender dengan info
        JPanel calendarWithInfo = new JPanel(new BorderLayout(0, 5));
        calendarWithInfo.add(infoPanel, BorderLayout.NORTH);
        calendarWithInfo.add(calendarPanel, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(calendarWithInfo, BorderLayout.CENTER);
        
        // Tombol tutup
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        JButton closeButton = new JButton("Tutup");
        closeButton.setBackground(new Color(231, 76, 60));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        updateCalendar.run(); // Load awal
        dialog.setVisible(true);
    }
    
    public Date getDate() {
        return calendar.getTime();
    }
    
    public void setDate(Date date) {
        // Validasi tanggal yang di-set juga harus dalam range
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (isDateInRange(cal)) {
            calendar.setTime(date);
            dateField.setText(dateFormat.format(date));
        } else {
            // Jika diluar range, set ke hari ini
            calendar = Calendar.getInstance();
            dateField.setText(dateFormat.format(calendar.getTime()));
        }
    }
}