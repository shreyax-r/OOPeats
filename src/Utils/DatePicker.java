package Utils;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatePicker {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static LocalDate showDatePickerDialog(Component parent, String title, LocalDate initialDate) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), title, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Year spinner
        int currentYear = initialDate.getYear();
        SpinnerNumberModel yearModel = new SpinnerNumberModel(currentYear, 2020, 2030, 1);
        JSpinner yearSpinner = new JSpinner(yearModel);
        JSpinner.NumberEditor yearEditor = new JSpinner.NumberEditor(yearSpinner, "####");
        yearSpinner.setEditor(yearEditor);
        
        // Month spinner
        int currentMonth = initialDate.getMonthValue();
        SpinnerNumberModel monthModel = new SpinnerNumberModel(currentMonth, 1, 12, 1);
        JSpinner monthSpinner = new JSpinner(monthModel);
        
        // Day spinner
        int currentDay = initialDate.getDayOfMonth();
        SpinnerNumberModel dayModel = new SpinnerNumberModel(currentDay, 1, 31, 1);
        JSpinner daySpinner = new JSpinner(dayModel);
        
        // Update day max when month/year changes
        yearSpinner.addChangeListener(e -> updateDayMax(daySpinner, monthSpinner, yearSpinner));
        monthSpinner.addChangeListener(e -> updateDayMax(daySpinner, monthSpinner, yearSpinner));
        
        // Date selection panel
        JPanel datePanel = new JPanel(new FlowLayout());
        datePanel.add(new JLabel("Year:"));
        datePanel.add(yearSpinner);
        datePanel.add(new JLabel("Month:"));
        datePanel.add(monthSpinner);
        datePanel.add(new JLabel("Day:"));
        datePanel.add(daySpinner);
        
        panel.add(new JLabel("Select Date:"), BorderLayout.NORTH);
        panel.add(datePanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        
        final LocalDate[] selectedDate = {null};
        
        okButton.addActionListener(e -> {
            try {
                int year = (Integer) yearSpinner.getValue();
                int month = (Integer) monthSpinner.getValue();
                int day = (Integer) daySpinner.getValue();
                selectedDate[0] = LocalDate.of(year, month, day);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date selected!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> {
            selectedDate[0] = null;
            dialog.dispose();
        });
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
        
        return selectedDate[0];
    }
    
    private static void updateDayMax(JSpinner daySpinner, JSpinner monthSpinner, JSpinner yearSpinner) {
        try {
            int year = (Integer) yearSpinner.getValue();
            int month = (Integer) monthSpinner.getValue();
            LocalDate date = LocalDate.of(year, month, 1);
            int maxDays = date.lengthOfMonth();
            
            SpinnerNumberModel model = (SpinnerNumberModel) daySpinner.getModel();
            int currentDay = (Integer) daySpinner.getValue();
            model.setMaximum(maxDays);
            if (currentDay > maxDays) {
                daySpinner.setValue(maxDays);
            }
        } catch (Exception e) {
            // Ignore
        }
    }
}

