/*
 * Click nbfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package TeamNC_codesbyKelsy;

import com.opencsv.CSVReader;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class MotorPH {
    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Welcome to MotorPH Payroll!");
        System.out.println("[1] Employee Details and Weekly Salary Calculations");
        System.out.println("[2] Add Employee");
        System.out.println("[3] Remove Employee"); 
        System.out.println("[4] Exit Program");
        System.out.print("Enter selection: ");
        int choice = inputScanner.nextInt();
        inputScanner.nextLine();
        
        switch (choice){
            case 1:
                searchEmployeeonTSV("src/main/employeedata.tsv");
                break;
            case 2:
                addEmployeeToTSV(inputScanner);
                break;
            case 3:
                removeEmployee(inputScanner);
                break;
            case 4:
                System.out.println("Exiting Program...");
                inputScanner.close();
                Thread.sleep(2000);
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void searchEmployeeonTSV(String TSVFilePath) throws IOException, InterruptedException {
        while (true){
            Scanner scanner = new Scanner(System.in);
            System.out.print("\nEnter Employee ID: ");
            String EmployeeID = scanner.nextLine();
            
            double monthlyBasicSalary = 0, HourlyRate = 0, riceSubsidy = 0, phoneAllowance = 0, clothingAllowance = 0;

            try(BufferedReader search = new BufferedReader(new FileReader(TSVFilePath))){
                String SearchEmployeeInformation;
                boolean EmployeeFound = false;

                while ((SearchEmployeeInformation = search.readLine()) != null){
                    String[] Information = SearchEmployeeInformation.split("\t");
                    if (SearchEmployeeInformation.trim().isEmpty()) continue;
                    if (Information[0].equals(EmployeeID)){
                        System.out.println("Employee ID #" + Information[0] + " found!");
                        System.out.println("Name: " + Information[1] + ", " + Information[2]);
                        String fullName = Information[1] + ", " + Information[2];
                        System.out.println("Birthday: " + Information[3]);
                        System.out.println("Address: " + Information[4]);
                        System.out.println("Phone Number: " + Information[5]);
                        System.out.println("SSS ID#: " + Information[6]);
                        System.out.println("Philhealth ID#: " + Information[7]);
                        System.out.println("TIN ID#: " + Information[8]);
                        System.out.println("Pag-Ibig ID#: " + Information[9]);
                        System.out.println("Status: " + Information[10]);
                        System.out.println("Position: " + Information[11]);
                        System.out.println("Immediate Supervisor: " + Information[12]);
                        System.out.println("Basic Salary: " + Information[13]);
                        try{
                            monthlyBasicSalary = Double.parseDouble(Information[13].replace(",", ""));
                            riceSubsidy = Double.parseDouble(Information[14].replace(",", ""));
                            phoneAllowance = Double.parseDouble(Information[15].replace(",", ""));
                            clothingAllowance = Double.parseDouble(Information[16].replace(",", ""));
                            HourlyRate = Double.parseDouble(Information[18].replace(",", ""));
                        } catch (NumberFormatException e){
                            System.out.println("Error parsing numeric values: " + e.getMessage());
                            }
                        System.out.println("Rice Subsidy: " + Information[14]);
                        System.out.println("Phone Allowance: " + Information[15]);
                        System.out.println("Clothing Allowance: " + Information[16]);
                        System.out.println("Gross Semi-Monthly Rate: " + Information[17]);
                        System.out.println("Hourly Rate: " + Information[18]);
                        EmployeeFound = true;
                        System.out.println("----------------------------------");
                        calculateWeeklyWorkHours(EmployeeID, fullName, HourlyRate, monthlyBasicSalary, riceSubsidy, phoneAllowance, clothingAllowance, TSVFilePath);
                        break;
                    }
                }
                if (!EmployeeFound) {
                    System.out.println("Employee ID not found.");
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
        System.out.print("Search another employee? (Y/N): ");
            if (!scanner.nextLine().equalsIgnoreCase("Y")){
            break;     
            }
        }
    }
    
    private static void calculateWeeklyWorkHours(String EmployeeID, String fullName, double HourlyRate, double monthlyBasicSalary, double riceSubsidy, double phoneAllowance, double clothingAllowance, String attendanceFilePath) {
        attendanceFilePath = "src/main/attendancerecord.csv";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

        LocalTime workStart = LocalTime.of(8, 0);
        LocalTime graceEnd = workStart.plusMinutes(10);
        LocalTime workEnd = LocalTime.of(17, 0);
        Duration breakTime = Duration.ofHours(1);

        TreeMap<LocalDate, Duration[]> weeklyRecords = new TreeMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(attendanceFilePath))) {
            reader.readNext(); // Skip header

            String[] data;
            while ((data = reader.readNext()) != null) {
                if (data.length >= 6) {
                    String empId = data[0].trim();
                    if (!empId.equals(EmployeeID)) {
                        continue;
                    }

                    String dateStr = data[3].trim();
                    String logInStr = data[4].trim();
                    String logOutStr = data[5].trim();

                    if (logInStr.isEmpty() || logOutStr.isEmpty()) {
                        continue;
                    }

                    try {
                        LocalDate date = LocalDate.parse(dateStr, dateFormatter);
                        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            continue;
                        }

                        LocalTime logIn = LocalTime.parse(logInStr, timeFormatter);
                        LocalTime logOut = LocalTime.parse(logOutStr, timeFormatter);

                        if (logOut.isBefore(logIn)) {
                            System.out.println("Error: Invalid time record for " + empId + " on " + dateStr);
                            continue;
                        }

                        boolean isLate = logIn.isAfter(graceEnd);
                        if (logIn.isAfter(workStart) && logIn.isBefore(graceEnd)) {
                            logIn = workStart;
                        }

                        LocalTime adjustedLogOut = logOut.isAfter(workEnd) ? workEnd : logOut;
                        Duration workDuration = Duration.between(logIn, adjustedLogOut).minus(breakTime);
                        workDuration = workDuration.isNegative() ? Duration.ZERO : workDuration;

                        Duration overtimeDuration = (!isLate && logOut.isAfter(workEnd))
                                ? Duration.between(workEnd, logOut)
                                : Duration.ZERO;

                        LocalDate weekStart = date.with(DayOfWeek.MONDAY);
                        weeklyRecords.putIfAbsent(weekStart, new Duration[]{Duration.ZERO, Duration.ZERO});
                        Duration[] durations = weeklyRecords.get(weekStart);
                        durations[0] = durations[0].plus(workDuration);
                        durations[1] = durations[1].plus(overtimeDuration);
                    } catch (DateTimeParseException e) {
                        System.out.println("Error parsing attendance date/time for " + empId + " on " + dateStr);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading attendance file: " + e.getMessage());
            return;
        }
        
            System.out.println("\nWeekly Salary Summary for " + fullName + ":");
            System.out.println("-------------------------------------------------");
            

        for (Map.Entry<LocalDate, Duration[]> entry : weeklyRecords.entrySet()) {
            LocalDate startOfWeek = entry.getKey();
            LocalDate endOfWeek = startOfWeek.plusDays(4);
            Duration workDuration = entry.getValue()[0];
            Duration overtimeDuration = entry.getValue()[1];
               
            
            double WorkHourstoMinutes = workDuration.toMinutes() / 60.0;
            double OTHourstoMinutes = overtimeDuration.toMinutes() / 60.0;

            double baseSalary = (WorkHourstoMinutes * HourlyRate);
            double overtimePay = (OTHourstoMinutes * HourlyRate * 1.25); //An additional 25%(hourly rate) pay for overtime hours
            double grossSalary = baseSalary + overtimePay;
            double totalAllowances = (riceSubsidy + phoneAllowance + clothingAllowance) / 4.0;

            // Weekly calculation of SSS contribution deduction
             double sssContribution = 0.0;
            if (monthlyBasicSalary < 3250) {
                sssContribution = 135.00 / 4;
            } else if (monthlyBasicSalary <= 3750) {
                sssContribution = 157.50 / 4;
            } else if (monthlyBasicSalary <= 4250) {
                sssContribution = 180.00 / 4;
            } else if (monthlyBasicSalary <= 4750) {
                sssContribution = 202.50 / 4;
            } else if (monthlyBasicSalary <= 5250) {
                sssContribution = 225.00 / 4;
            } else if (monthlyBasicSalary <= 5750) {
                sssContribution = 247.50 / 4;
            } else if (monthlyBasicSalary <= 6250) {
                sssContribution = 270.00 / 4;
            } else if (monthlyBasicSalary <= 6750) {
                sssContribution = 292.50 / 4;
            } else if (monthlyBasicSalary <= 7250) {
                sssContribution = 315.00 / 4;
            } else if (monthlyBasicSalary <= 7750) {
                sssContribution = 337.50 / 4;
            } else if (monthlyBasicSalary <= 8250) {
                sssContribution = 360.00 / 4;
            } else if (monthlyBasicSalary <= 8750) {
                sssContribution = 382.50 / 4;
            } else if (monthlyBasicSalary <= 9250) {
                sssContribution = 405.00 / 4;
            } else if (monthlyBasicSalary <= 9750) {
                sssContribution = 427.50 / 4;
            } else if (monthlyBasicSalary <= 10250) {
                sssContribution = 450.00 / 4;
            } else if (monthlyBasicSalary <= 10750) {
                sssContribution = 472.50 / 4;
            } else if (monthlyBasicSalary <= 11250) {
                sssContribution = 495.00 / 4;
            } else if (monthlyBasicSalary <= 11750) {
                sssContribution = 517.50 / 4;
            } else if (monthlyBasicSalary <= 12250) {
                sssContribution = 540.00 / 4;
            } else if (monthlyBasicSalary <= 12750) {
                sssContribution = 562.50 / 4;
            } else if (monthlyBasicSalary <= 13250) {
                sssContribution = 585.00 / 4;
            } else if (monthlyBasicSalary <= 13750) {
                sssContribution = 607.50 / 4;
            } else if (monthlyBasicSalary <= 14250) {
                sssContribution = 630.00 / 4;
            } else if (monthlyBasicSalary <= 14750) {
                sssContribution = 652.50 / 4;
            } else if (monthlyBasicSalary <= 15250) {
                sssContribution = 675.00 / 4;
            } else if (monthlyBasicSalary <= 15750) {
                sssContribution = 697.50 / 4;
            } else if (monthlyBasicSalary <= 16250) {
                sssContribution = 720.00 / 4;
            } else if (monthlyBasicSalary <= 16750) {
                sssContribution = 742.50 / 4;
            } else if (monthlyBasicSalary <= 17250) {
                sssContribution = 765.00 / 4;
            } else if (monthlyBasicSalary <= 17750) {
                sssContribution = 787.50 / 4;
            } else if (monthlyBasicSalary <= 18250) {
                sssContribution = 810.00 / 4;
            } else if (monthlyBasicSalary <= 18750) {
                sssContribution = 832.50 / 4;    
            } else if (monthlyBasicSalary <= 19250) {
                sssContribution = 855.00 / 4;
            } else if (monthlyBasicSalary <= 19750) {
                sssContribution = 877.50 / 4;
            } else if (monthlyBasicSalary <= 20250) {
                sssContribution = 900.00 / 4;
            } else if (monthlyBasicSalary <= 20750) {
                sssContribution = 922.50 / 4;
            } else if (monthlyBasicSalary <= 21250) {
                sssContribution = 945.00 / 4; 
            } else if (monthlyBasicSalary <= 21750) {
                sssContribution = 967.50 / 4;
            } else if (monthlyBasicSalary <= 22250) {
                sssContribution = 990.00 / 4; 
            } else if (monthlyBasicSalary <= 22750) {
                sssContribution = 1012.50 / 4;
            } else if (monthlyBasicSalary <= 23250) {
                sssContribution = 1035.00 / 4; 
            } else if (monthlyBasicSalary <= 23750) {
                sssContribution = 1057.50 / 4;
            } else if (monthlyBasicSalary <= 24250) {
                sssContribution = 1080.00 / 4; 
            } else if (monthlyBasicSalary <= 24750) {
                sssContribution = 1102.50 / 4;
            } else if (monthlyBasicSalary <= 24750) {
                sssContribution = 1102.50 / 4;       
            } else {
                sssContribution = 1125.00 / 4; // Maximum SSS Contribution
            }


            // Weekly Calculation of Philhealth Contribution (Deduction)
            double philHealthContribution;
 
            if (monthlyBasicSalary <= 10000){
                philHealthContribution = (300 / 2) / 4;
            } else if (monthlyBasicSalary <= 59999.99) {
                philHealthContribution = (Math.min(monthlyBasicSalary, 1800) / 2) / 4;
            } else {
                philHealthContribution = (1800 / 2) / 4; // Maximum Philhealth Contribution
            }

           // Weekly Calculation of Pag-ibig Contribution (Deduction)
            double pagIbigContribution = 0.0;
            if (monthlyBasicSalary <= 1500) {
                pagIbigContribution = monthlyBasicSalary * 0.01; // Employee's share (1%)
            } else {
                pagIbigContribution = monthlyBasicSalary * 0.02; // Employee's share (2%)
            }
            if (pagIbigContribution > 100) {
                pagIbigContribution = 100.00 / 4; // Maximum Pag-Ibig Contribution
            }

           // Weekly Calculation of Withholding Tax (Deduction), Tax Rates are divided by 4.
            double taxableIncome = grossSalary - sssContribution - philHealthContribution - pagIbigContribution;
            double withholdingTax = 0.0;
            if (taxableIncome <= 5208) {
                withholdingTax = 0.0;
            } else if (taxableIncome <= 8333.25) {
                withholdingTax = (taxableIncome - 5208.25) * 0.20;
            } else if (taxableIncome <= 16667.75) {
                withholdingTax = 625 + (taxableIncome - 8333.25) * 0.25;
            } else if (taxableIncome <= 41666.75) {
                withholdingTax = 2708.25 + (taxableIncome - 16666.75) * 0.30;
            } else if (taxableIncome <= 166666.75) {
                withholdingTax = 10208.33 + (taxableIncome - 41666.75) * 0.32;
            } else {
                withholdingTax = 50208.33 + (taxableIncome - 166666.75) * 0.35;
            }
            
            double totalDeduction = sssContribution + philHealthContribution + pagIbigContribution + withholdingTax;
            double netSalary = grossSalary - withholdingTax - sssContribution - philHealthContribution - pagIbigContribution;
            double finalPay = netSalary + totalAllowances;
            
            System.out.println("Week Period             : " + startOfWeek + " - " + endOfWeek);
            System.out.println("Total Hours Worked      : " + workDuration.toHours() + "h " + workDuration.toMinutesPart() + "m");
            System.out.println("Total Overtime          : " + overtimeDuration.toHours() + "h " + overtimeDuration.toMinutesPart() + "m");
            System.out.println("Base Salary             : PHP " + String.format("%.2f", baseSalary));
            System.out.println("Overtime Pay            : PHP " + String.format("%.2f", overtimePay));
            System.out.println("Gross Salary            : PHP " + String.format("%.2f", grossSalary));
            System.out.println("SSS Contribution        : PHP " + String.format("-%.2f", sssContribution));
            System.out.println("PhilHealth Contribution : PHP " + String.format("-%.2f", philHealthContribution));
            System.out.println("Pag-Ibig Contribution   : PHP " + String.format("-%.2f", pagIbigContribution));
            System.out.println("Withholding Tax         : PHP " + String.format("-%.2f", withholdingTax));
            System.out.println("Total Deductions        : PHP " + String.format("-%.2f", totalDeduction));
            System.out.println("Allowances              : PHP " + String.format("%.2f", totalAllowances));
            System.out.println("\nNet Salary            : PHP " + String.format("%.2f", finalPay));
            System.out.println("--------------------------------------------------");
        }
    }
    
    private static void addEmployeeToTSV(Scanner scanner) throws IOException, InterruptedException {
        try(FileWriter WriteFile = new FileWriter("src/main/employeedata.tsv", true);
            BufferedWriter WriteBuffered = new BufferedWriter(WriteFile);
            PrintWriter UpdatedInfo = new PrintWriter(WriteBuffered)){
                System.out.println("Enter Employee Details: ");
                System.out.print("Employee #: ");
                String EmployeeNumber = scanner.nextLine();
                System.out.print("Last Name: ");
                String LN = scanner.nextLine();
                System.out.print("First Name: ");
                String FN = scanner.nextLine();
                System.out.print("Birthday(MM/DD/YYYY): ");
                String Birthday = scanner.nextLine();
                System.out.print("Address: ");
                String Address = scanner.nextLine();
                System.out.print("Phone Number: ");
                String PhoneNumber = scanner.nextLine();
                System.out.print("SSS ID#: ");
                String SSSNumber = scanner.nextLine();
                System.out.print("Philhealth ID#: ");
                String PhilHealth = scanner.nextLine();
                System.out.print("TIN ID#: ");
                String TIN = scanner.nextLine();
                System.out.print("Pag-ibig ID#: ");
                String PagIbig = scanner.nextLine();
                System.out.print("Status (Regular/Probationary): ");
                String Status = scanner.nextLine();
                System.out.print("Position: ");
                String Position = scanner.nextLine();
                System.out.print("Immediate Supervisor (Last Name, First Name): ");
                String Supervisor = scanner.nextLine();
                System.out.print("Basic Monthly Salary: ");
                String MonthlySalary = scanner.nextLine();
                System.out.print("Rice Subsidy: ");
                String Rice = scanner.nextLine();
                System.out.print("Phone Allowance: ");
                String Phone = scanner.nextLine();
                System.out.print("Clothing Allowance: ");
                String Clothing = scanner.nextLine();
                System.out.print("Gross Semi-monthly Rate: ");
                String GSMR = scanner.nextLine();
                System.out.print("Hourly Rate: ");
                String HourlyRate = scanner.nextLine();
                WriteBuffered.newLine();
                UpdatedInfo.print(EmployeeNumber + "\t" + LN + "\t" + FN + "\t" + Birthday + "\t" + Address + "\t" + PhoneNumber + "\t" + SSSNumber + "\t" + PhilHealth + "\t" + TIN + "\t" + PagIbig + "\t" + Status + "\t" + Position + "\t" + Supervisor + "\t" + MonthlySalary + "\t" + Rice + "\t" + Phone + "\t" + Clothing + "\t" + GSMR + "\t" + HourlyRate);
                System.out.println("Employee details added successfully!");
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
              }
    }

    private static void removeEmployee(Scanner scanner) throws IOException, InterruptedException{
            System.out.print("\nEnter Employee ID: ");
            String RemoveEmployeeID = scanner.nextLine();
            
            File UpdatedFile = new File("src/main/employeedata.tsv");
            File TempFile = new File("temp.tsv");
            boolean RemoveEmployee = false;
            
            try (BufferedReader reader = new BufferedReader(new FileReader(UpdatedFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(TempFile));
                 PrintWriter UpdatedInfo = new PrintWriter(writer)){
                
                String EmployeeID;
                boolean isFirstLine = true; // Track if it's the first line being written
                while((EmployeeID = reader.readLine()) != null){
                    String[] employee = EmployeeID.split("\t");
                    if(!employee[0].equals(RemoveEmployeeID)){
                        if (!isFirstLine) {
                        UpdatedInfo.print(System.lineSeparator());
                    } else isFirstLine = false; // First line has no prepended newline
                        UpdatedInfo.print(EmployeeID);
                    } else RemoveEmployee = true;
                }
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
              }
            if (RemoveEmployee){
                if (!UpdatedFile.delete() || !TempFile.renameTo(UpdatedFile)){
                    System.err.println("Error updating file.");
                } else {
                    System.out.println("Employee has been removed from database.");
                }
            } else {
                System.out.println("Employee ID not found!");
                TempFile.delete();
            }
    }
}