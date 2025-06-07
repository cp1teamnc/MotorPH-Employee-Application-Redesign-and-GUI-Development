package GUI;

/**
 *
 * @author kayejoanneangelikaplaza
 */

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;

public class SpecificEmployeeFrame extends JFrame {

    private JTextField empIdField; // input field for employee ID

    private JTextArea empDetailsArea; // rectangle spaceee to display employee details

    //date pickers/dropdown
    private JComboBox<String> fromDay, fromMonth, fromYear, toDay, toMonth, toYear;
   
    private JButton computeButton;//inside payslip rectangle sa right side

    
    //window
    public SpecificEmployeeFrame() {
        setTitle("Payslip Generator"); //sa panel title
        setSize(820, 570);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

  
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 20));

        //area back btton n title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false); //pwede makita bg if everr

        
        //same button
        JButton backButton = new JButton("\u2190");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 28));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setToolTipText("Back");
        backButton.addActionListener(e -> {
            MotorPHFrame motorPH = new MotorPHFrame();
            motorPH.setVisible(true);
            dispose();
        });

        
        //title di ko macenter becz of arrow i think
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        JLabel titleLabel = new JLabel("Payslip Generator");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(Color.decode("#000080"));
        titlePanel.add(titleLabel);

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        
        //split area
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        //in line w txt field n confirm button
        JPanel empIdPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JLabel enterIdLabel = new JLabel("Employee ID:");
        empIdField = new JTextField();
        empIdField.setPreferredSize(new Dimension(100, 25));

        //try maglearn ng pag enter ng value with enter lang sa keyboard
        JButton verifyButton = new JButton("Confirm");
        verifyButton.setPreferredSize(new Dimension(60, 20)); //button size, font size
        verifyButton.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        empIdPanel.add(enterIdLabel);
        empIdPanel.add(empIdField);
        empIdPanel.add(verifyButton);

        contentPanel.add(empIdPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); //spacee

        // use null layout with explicit bounds for splitpanel
        JPanel splitPanel = new JPanel(null);
        splitPanel.setPreferredSize(new Dimension(300, 400)); //spacee

        
        //format ng emp details directly from csv, try formatting output from csv 4 emphasis
        empDetailsArea = new JTextArea();
        empDetailsArea.setEditable(false);
        empDetailsArea.setFont(new Font("Verdana", Font.PLAIN, 12));
        empDetailsArea.setLineWrap(true);
        empDetailsArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(empDetailsArea);
        scrollPane.setBounds(10, 30, 310, 310);

        
        //left rectangle emp deetss
        JPanel detailsPanel = new JPanel(new BorderLayout());
        JLabel detailsLabel = new JLabel("Details");
        detailsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        detailsLabel.setForeground(Color.decode("#002147"));
        detailsPanel.setBorder(BorderFactory.createTitledBorder(""));
        detailsPanel.setBounds(0, 0, 330, 350);
        detailsPanel.add(detailsLabel, BorderLayout.NORTH);
        detailsPanel.add(scrollPane, BorderLayout.CENTER);

        //right rectangle --payslip, the csv integration stopped hereeezzzzzzzzzz how to remember
        JPanel payslipPanel = new JPanel();
        payslipPanel.setLayout(new BoxLayout(payslipPanel, BoxLayout.Y_AXIS));

        JLabel payslipLabel = new JLabel("Payslip");
        payslipLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        payslipLabel.setForeground(Color.decode("#002147"));
        payslipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  

        payslipPanel.add(payslipLabel);
        payslipPanel.add(Box.createRigidArea(new Dimension(0, 10)));  // add vertical spacing

        JLabel coverageLabel = new JLabel("Select Pay Period:");
        coverageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        coverageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // center horizontally

         // panel for the whole date range
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4)); 

        datePanel.add(new JLabel("From:"));
        fromDay = new JComboBox<>(generateNumbers(1, 31));
        fromMonth = new JComboBox<>(new String[]{"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        fromYear = new JComboBox<>(generateNumbers(2024, 2025));
        datePanel.add(fromDay);
        datePanel.add(fromMonth);
        datePanel.add(fromYear);



        datePanel.add(new JLabel(" To:")); // Added some spaces before "To:" for better separation
        toDay = new JComboBox<>(generateNumbers(1, 31));
        toMonth = new JComboBox<>(new String[]{"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        toYear = new JComboBox<>(generateNumbers(2024, 2025));
        datePanel.add(toDay);
        datePanel.add(toMonth);
        datePanel.add(toYear);

        computeButton = new JButton("Generate Payslip"); //magpop up ang payslip breakdown

        payslipPanel.setBorder(BorderFactory.createTitledBorder(""));
        payslipPanel.setBounds(340, 0, 400, 350);
        payslipPanel.add(payslipLabel);
        payslipPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        payslipPanel.add(coverageLabel);
        payslipPanel.add(datePanel);
        payslipPanel.add(Box.createRigidArea(new Dimension(10, 20)));
        payslipPanel.add(computeButton);

        splitPanel.add(detailsPanel);
        splitPanel.add(payslipPanel);

        contentPanel.add(splitPanel);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);

        verifyButton.addActionListener(e -> verifyEmployee()); // "confirm"

        // disable muna compute button initially until employee is verified
        computeButton.setEnabled(false);

        // action listener for computeButton here
        computeButton.addActionListener(e -> generatePayslip());

        setVisible(true);//window visibility
    }
    
    // helper method to generate numbers as strings 4 day/year combo boxes
    private String[] generateNumbers(int start, int end) {
        String[] nums = new String[end - start + 1];
        for (int i = start; i <= end; i++) {
            nums[i - start] = String.valueOf(i);
        }
        return nums;
    }

   private void verifyEmployee() {
    try {
        String empId = empIdField.getText().trim();

        // blank input
        if (empId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Employee ID field cannot be blank.", "Input Error", JOptionPane.WARNING_MESSAGE);
            empDetailsArea.setText("");
            computeButton.setEnabled(false);
            return;
        }

        // check for invalid characters (only digits allowed)
        if (!empId.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Employee ID must contain only numbers (no letters or symbols).", "Input Error", JOptionPane.WARNING_MESSAGE);
            empDetailsArea.setText("");
            computeButton.setEnabled(false);
            return;
        }

        // 5-digit check
        if (!Pattern.matches("\\d{5}", empId)) {
            JOptionPane.showMessageDialog(this, "Employee ID must be a 5-digit number.", "Invalid ID", JOptionPane.ERROR_MESSAGE);
            empDetailsArea.setText("");
            computeButton.setEnabled(false);
            return;
        }

        //ito nagpagalit saken
        String employeeFilePath = "src/main/resources/EmployeeDetails.csv";
        File employeeFile = new File(employeeFilePath);
        if (!employeeFile.exists()) {
            JOptionPane.showMessageDialog(this, "EmployeeDetails.csv not found at: " + employeeFilePath, "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // load & parse csv data
        List<String[]> employees = CsvLoader.loadCsv(new FileInputStream(employeeFile), true, ",");

        boolean found = false;
        StringBuilder details = new StringBuilder();

        //emp ID matching
        for (String[] emp : employees) {
            if (emp.length > 0 && emp[0].equals(empId)) {
                found = true;
                details.append("Employee ID: ").append(emp[0]).append("\n");
                details.append("Name: ").append(emp[2]).append(" ").append(emp[1]).append("\n");
                details.append("Birthday: ").append(emp[3]).append("\n");
                details.append("Address: ").append(emp[4]).append("\n");
                details.append("Phone Number: ").append(emp[5]).append("\n");
                details.append("SSS: ").append(emp[6]).append("\n");
                details.append("Philhealth #: ").append(emp[7]).append("\n");
                details.append("TIN: ").append(emp[8]).append("\n");
                details.append("Pag-ibig: ").append(emp[9]).append("\n");
                details.append("Status: ").append(emp[10]).append("\n");
                details.append("Position: ").append(emp[11]).append("\n");
                details.append("Immediate Supervisor: ").append(emp[12]).append("\n");
                details.append("Basic Salary: ₱").append(emp[13]).append("\n");
                details.append("Rice Subsidy: ₱").append(emp[14]).append("\n");
                details.append("Phone Allowance: ₱").append(emp[15]).append("\n");
                details.append("Clothing Allowance: ₱").append(emp[16]).append("\n");
                details.append("Gross Semi-monthly Rate: ₱").append(emp[17]).append("\n");
                details.append("Hourly Rate: ₱").append(emp[18]).append("\n");
                break;
            }
        }

        //if not found!
        if (!found) {
            empDetailsArea.setText("Employee ID " + empId + " not found.");
            computeButton.setEnabled(false);
        } else {
            empDetailsArea.setText(details.toString());
            computeButton.setEnabled(true);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        empDetailsArea.setText("");
        computeButton.setEnabled(false);
    }
}



    // Add this new method for computeButton action
    private void generatePayslip() {
        try {
            String empId = empIdField.getText().trim();
            
            // Get selected dates
            String fromDate = fromDay.getSelectedItem() + " " + fromMonth.getSelectedItem() + " " + fromYear.getSelectedItem();
            String toDate = toDay.getSelectedItem() + " " + toMonth.getSelectedItem() + " " + toYear.getSelectedItem();
            
            // Load employee details
            String employeeFilePath = "src/main/resources/EmployeeDetails.csv";
            File employeeFile = new File(employeeFilePath);
            if (!employeeFile.exists()) {
                JOptionPane.showMessageDialog(this, "EmployeeDetails.csv not found at: " + employeeFilePath, "File Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<String[]> employees = CsvLoader.loadCsv(new FileInputStream(employeeFile), true, ",");
            String[] employeeData = null;
            
            // Find employee data
            for (String[] emp : employees) {
                if (emp.length > 0 && emp[0].equals(empId)) {
                    employeeData = emp;
                    break;
                }
            }
            
            if (employeeData == null) {
                JOptionPane.showMessageDialog(this, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Parse employee data
            double hourlyRate = Double.parseDouble(employeeData[18].trim().replaceAll("\"", "").replace(",", ""));
            double riceSubsidy = Double.parseDouble(employeeData[14].trim().replaceAll("\"", "").replace(",", ""));
            double phoneAllowance = Double.parseDouble(employeeData[15].trim().replaceAll("\"", "").replace(",", ""));
            double clothingAllowance = Double.parseDouble(employeeData[16].trim().replaceAll("\"", "").replace(",", ""));
            double monthlyBasicSalary = Double.parseDouble(employeeData[13].trim().replaceAll("\"", "").replace(",", ""));
            
            // Load attendance records for the employee and selected month
            String attendanceFilePath = "src/main/attendancerecord.csv";
            File attendanceFile = new File(attendanceFilePath);
            if (!attendanceFile.exists()) {
                JOptionPane.showMessageDialog(this, "Attendance record not found at: " + attendanceFilePath, "File Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<String[]> attendanceRecords = CsvLoader.loadCsv(new FileInputStream(attendanceFile), true, ",");

            // Parse the selected date range
            int fromDayVal = Integer.parseInt(fromDay.getSelectedItem().toString());
            int fromMonthVal = fromMonth.getSelectedIndex() + 6; // 'Jun' is month 6
            int fromYearVal = Integer.parseInt(fromYear.getSelectedItem().toString());
            int toDayVal = Integer.parseInt(toDay.getSelectedItem().toString());
            int toMonthVal = toMonth.getSelectedIndex() + 6;
            int toYearVal = Integer.parseInt(toYear.getSelectedItem().toString());

            java.time.LocalDate fromDateObj = java.time.LocalDate.of(fromYearVal, fromMonthVal, fromDayVal);
            java.time.LocalDate toDateObj = java.time.LocalDate.of(toYearVal, toMonthVal, toDayVal);

            double totalRegularHours = 0.0;
            double totalOvertimeHours = 0.0;
            for (String[] record : attendanceRecords) {
                if (record.length < 6) continue;
                String recordEmpId = record[0].trim();
                String dateStr = record[3].trim();
                if (!recordEmpId.equals(empId)) continue;
                String[] dateParts = dateStr.split("/");
                if (dateParts.length != 3) continue;
                int recMonth = Integer.parseInt(dateParts[0]);
                int recDay = Integer.parseInt(dateParts[1]);
                int recYear = Integer.parseInt(dateParts[2]);
                java.time.LocalDate recDate;
                try {
                    recDate = java.time.LocalDate.of(recYear, recMonth, recDay);
                } catch (Exception ex) {
                    continue;
                }
                if (recDate.isBefore(fromDateObj) || recDate.isAfter(toDateObj)) continue;
                // Parse log in/out
                String logIn = record[4].trim();
                String logOut = record[5].trim();
                try {
                    String[] inParts = logIn.split(":");
                    String[] outParts = logOut.split(":");
                    int inHour = Integer.parseInt(inParts[0]);
                    int inMin = Integer.parseInt(inParts[1]);
                    int outHour = Integer.parseInt(outParts[0]);
                    int outMin = Integer.parseInt(outParts[1]);
                    double hoursWorked = (outHour + outMin / 60.0) - (inHour + inMin / 60.0);
                    if (hoursWorked > 8) {
                        totalRegularHours += 8;
                        totalOvertimeHours += (hoursWorked - 8);
                    } else if (hoursWorked > 0) {
                        totalRegularHours += hoursWorked;
                    }
                } catch (Exception ex) {
                    // skip invalid time
                }
            }

            // Calculate pay based on attendance
            double baseSalary = hourlyRate * totalRegularHours;
            double overtimePay = hourlyRate * totalOvertimeHours * 1.25;
            double grossSalary = baseSalary + overtimePay;
            double totalAllowances = (riceSubsidy + phoneAllowance + clothingAllowance); // full monthly
            double sssContribution = calculateSSSContribution(monthlyBasicSalary);
            double philHealthContribution = calculatePhilHealthContribution(monthlyBasicSalary);
            double pagIbigContribution = calculatePagIbigContribution(monthlyBasicSalary);
            double taxableIncome = grossSalary - sssContribution - philHealthContribution - pagIbigContribution;
            double withholdingTax = calculateWithholdingTax(taxableIncome);
            double totalDeduction = sssContribution + philHealthContribution + pagIbigContribution + withholdingTax;
            double netSalary = grossSalary - totalDeduction;
            double finalPay = netSalary + totalAllowances;

            // Create payslip message
            StringBuilder payslipMessage = new StringBuilder();
            payslipMessage.append("PAYSLIP\n");
            payslipMessage.append("=================================================\n");
            payslipMessage.append("Employee ID: ").append(empId).append("\n");
            payslipMessage.append("Name: ").append(employeeData[2]).append(" ").append(employeeData[1]).append("\n");
            payslipMessage.append("Period: ").append(fromMonth.getSelectedItem()).append(" ").append(fromYear.getSelectedItem()).append(" (Monthly)\n");
            payslipMessage.append("-------------------------------------------------\n");
            payslipMessage.append(String.format("%-25s %8.2f\n", "Total Regular Hours:", totalRegularHours));
            payslipMessage.append(String.format("%-25s %8.2f\n", "Total Overtime Hours:", totalOvertimeHours));
            payslipMessage.append(String.format("%-25s %8.2f\n", "Total Hours Worked:", totalRegularHours + totalOvertimeHours));
            payslipMessage.append("-------------------------------------------------\n");
            payslipMessage.append("EARNINGS\n");
            payslipMessage.append(String.format("  %-22s ₱%10.2f\n", "Base Salary:", baseSalary));
            payslipMessage.append(String.format("  %-22s ₱%10.2f\n", "Overtime Pay:", overtimePay));
            payslipMessage.append(String.format("  %-22s ₱%10.2f\n", "Gross Salary:", grossSalary));
            payslipMessage.append("-------------------------------------------------\n");
            payslipMessage.append("DEDUCTIONS\n");
            payslipMessage.append(String.format("  %-22s -₱%9.2f\n", "SSS:", sssContribution));
            payslipMessage.append(String.format("  %-22s -₱%9.2f\n", "PhilHealth:", philHealthContribution));
            payslipMessage.append(String.format("  %-22s -₱%9.2f\n", "Pag-IBIG:", pagIbigContribution));
            payslipMessage.append(String.format("  %-22s -₱%9.2f\n", "Withholding Tax:", withholdingTax));
            payslipMessage.append(String.format("  %-22s -₱%9.2f\n", "Total Deductions:", totalDeduction));
            payslipMessage.append("-------------------------------------------------\n");
            payslipMessage.append("ALLOWANCES\n");
            payslipMessage.append(String.format("  %-22s ₱%10.2f\n", "Rice Subsidy:", riceSubsidy));
            payslipMessage.append(String.format("  %-22s ₱%10.2f\n", "Phone Allowance:", phoneAllowance));
            payslipMessage.append(String.format("  %-22s ₱%10.2f\n", "Clothing Allowance:", clothingAllowance));
            payslipMessage.append(String.format("  %-22s ₱%10.2f\n", "Total Allowances:", totalAllowances));
            payslipMessage.append("-------------------------------------------------\n");
            payslipMessage.append(String.format("%-25s ₱%10.2f\n", "Net Salary:", netSalary));
            payslipMessage.append(String.format("%-25s ₱%10.2f\n", "Final Pay (Net + Allowances):", finalPay));
            payslipMessage.append("=================================================\n");

            // Display payslip
            JTextArea textArea = new JTextArea(payslipMessage.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scrollPanePayslip = new JScrollPane(textArea);
            scrollPanePayslip.setPreferredSize(new Dimension(800, 700));
            JOptionPane.showMessageDialog(this, scrollPanePayslip, "Payslip", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating payslip: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private double calculateSSSContribution(double monthlySalary) {
        if (monthlySalary < 3250) {
            return 135.00 / 4;
        } else if (monthlySalary <= 3750) {
            return 157.50 / 4;
        } else if (monthlySalary <= 4250) {
            return 180.00 / 4;
        } else if (monthlySalary <= 4750) {
            return 202.50 / 4;
        } else if (monthlySalary <= 5250) {
            return 225.00 / 4;
        } else if (monthlySalary <= 5750) {
            return 247.50 / 4;
        } else if (monthlySalary <= 6250) {
            return 270.00 / 4;
        } else if (monthlySalary <= 6750) {
            return 292.50 / 4;
        } else if (monthlySalary <= 7250) {
            return 315.00 / 4;
        } else if (monthlySalary <= 7750) {
            return 337.50 / 4;
        } else if (monthlySalary <= 8250) {
            return 360.00 / 4;
        } else if (monthlySalary <= 8750) {
            return 382.50 / 4;
        } else if (monthlySalary <= 9250) {
            return 405.00 / 4;
        } else if (monthlySalary <= 9750) {
            return 427.50 / 4;
        } else if (monthlySalary <= 10250) {
            return 450.00 / 4;
        } else if (monthlySalary <= 10750) {
            return 472.50 / 4;
        } else if (monthlySalary <= 11250) {
            return 495.00 / 4;
        } else if (monthlySalary <= 11750) {
            return 517.50 / 4;
        } else if (monthlySalary <= 12250) {
            return 540.00 / 4;
        } else if (monthlySalary <= 12750) {
            return 562.50 / 4;
        } else if (monthlySalary <= 13250) {
            return 585.00 / 4;
        } else if (monthlySalary <= 13750) {
            return 607.50 / 4;
        } else if (monthlySalary <= 14250) {
            return 630.00 / 4;
        } else if (monthlySalary <= 14750) {
            return 652.50 / 4;
        } else if (monthlySalary <= 15250) {
            return 675.00 / 4;
        } else if (monthlySalary <= 15750) {
            return 697.50 / 4;
        } else if (monthlySalary <= 16250) {
            return 720.00 / 4;
        } else if (monthlySalary <= 16750) {
            return 742.50 / 4;
        } else if (monthlySalary <= 17250) {
            return 765.00 / 4;
        } else if (monthlySalary <= 17750) {
            return 787.50 / 4;
        } else if (monthlySalary <= 18250) {
            return 810.00 / 4;
        } else if (monthlySalary <= 18750) {
            return 832.50 / 4;
        } else if (monthlySalary <= 19250) {
            return 855.00 / 4;
        } else if (monthlySalary <= 19750) {
            return 877.50 / 4;
        } else if (monthlySalary <= 20250) {
            return 900.00 / 4;
        } else if (monthlySalary <= 20750) {
            return 922.50 / 4;
        } else if (monthlySalary <= 21250) {
            return 945.00 / 4;
        } else if (monthlySalary <= 21750) {
            return 967.50 / 4;
        } else if (monthlySalary <= 22250) {
            return 990.00 / 4;
        } else if (monthlySalary <= 22750) {
            return 1012.50 / 4;
        } else if (monthlySalary <= 23250) {
            return 1035.00 / 4;
        } else if (monthlySalary <= 23750) {
            return 1057.50 / 4;
        } else if (monthlySalary <= 24250) {
            return 1080.00 / 4;
        } else if (monthlySalary <= 24750) {
            return 1102.50 / 4;
        } else {
            return 1125.00 / 4;
        }
    }
    
    private double calculatePhilHealthContribution(double monthlySalary) {
        if (monthlySalary <= 10000) {
            return (300 / 2) / 4;
        } else if (monthlySalary <= 59999.99) {
            return (Math.min(monthlySalary * 0.03, 1800) / 2) / 4;
        } else {
            return (1800 / 2) / 4;
        }
    }
    
    private double calculatePagIbigContribution(double monthlySalary) {
        double contribution;
        if (monthlySalary <= 1500) {
            contribution = monthlySalary * 0.01;
        } else {
            contribution = monthlySalary * 0.02;
        }
        return Math.min(contribution, 100.00) / 4;
    }
    
    private double calculateWithholdingTax(double taxableIncome) {
        if (taxableIncome <= 5208) {
            return 0.0;
        } else if (taxableIncome <= 8333.25) {
            return (taxableIncome - 5208.25) * 0.20;
        } else if (taxableIncome <= 16667.75) {
            return 625 + (taxableIncome - 8333.25) * 0.25;
        } else if (taxableIncome <= 41666.75) {
            return 2708.25 + (taxableIncome - 16666.75) * 0.30;
        } else if (taxableIncome <= 166666.75) {
            return 10208.33 + (taxableIncome - 41666.75) * 0.32;
        } else {
            return 50208.33 + (taxableIncome - 166666.75) * 0.35;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SpecificEmployeeFrame::new); //yes
    }
}
