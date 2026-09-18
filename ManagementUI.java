import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ManagementUI {
    public JPanel mainPanel;

    private JButton addPatientButton;
    private JButton addDoctorButton;
    private JButton scheduleAppointmentButton;
    private JButton cancelAppointmentButton;
    private JButton viewRecordsButton;
    private JButton ViewDoctorButton;
    private JButton addStaffButton;
    private JButton viewStaffButton;

    private ArrayList<Patient> patients = new ArrayList<>();
    private ArrayList<Doctor> doctors = new ArrayList<>();
    private ArrayList<Staff> staffs = new ArrayList<>();


    public ManagementUI()
    {

        // Add Patient Button
        addPatientButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatient();
            }
        });

        // Add Doctor Button
        addDoctorButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDoctor();
            }
        });
        //View all registered doctor button
        ViewDoctorButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAllDoctors();
            }
        });
        // Schedule Appointment Button
        scheduleAppointmentButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                scheduleAppointment();
            }
        });

        // Cancel Appointment Button
        cancelAppointmentButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelAppointment();
            }
        });

        // View Records Button
        viewRecordsButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewRecords();
            }
        });

        addStaffButton.addActionListener(e -> addStaff());
        viewStaffButton.addActionListener(e -> viewAllStaff());

    }

    // methods definition starts here
    //add patient method
    private void addPatient()
    {   //check name
        String name = JOptionPane.showInputDialog(mainPanel, "Enter patient name:");
        if (name == null || name.trim().isEmpty())
        {
            JOptionPane.showMessageDialog(mainPanel, "Patient name is required.");
            return;
        }
        //check age
        String ageStr = JOptionPane.showInputDialog(mainPanel, "Enter patient age:");
        if (ageStr == null || ageStr.trim().isEmpty())
        {
            JOptionPane.showMessageDialog(mainPanel, "Patient age is required.");
            return;
        }
        //check age is digit?
        boolean isNumeric = ageStr.chars().allMatch(Character::isDigit);
        if (!isNumeric)
        {
            JOptionPane.showMessageDialog(mainPanel, "Age must be a number.");
            return;
        }
        int age = Integer.parseInt(ageStr);
        //check contact details
        String contact = JOptionPane.showInputDialog(mainPanel, "Enter contact details:");
        if (contact == null || contact.trim().isEmpty())
        {
            JOptionPane.showMessageDialog(mainPanel, "Contact details are required.");
            return;
        }
        //check diagnosis
        String diagnosis = JOptionPane.showInputDialog(mainPanel, "Enter diagnosis:");
        if (diagnosis == null || diagnosis.trim().isEmpty())
        {
            JOptionPane.showMessageDialog(mainPanel, "Diagnosis is required.");
            return;
        }
        // if passed all checks, create a new object patient
        Patient p = new Patient(name, age, contact, "N/A", diagnosis);
        patients.add(p);
        //print status
        JOptionPane.showMessageDialog(mainPanel, "Success: Patient \"" + name + "\" added.");
    }

    //Add doctor method
    private void addDoctor() {

        String name = JOptionPane.showInputDialog(mainPanel, "Enter doctor name:");
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Doctor name is required.");
            return;
        }

        String ageStr = JOptionPane.showInputDialog(mainPanel, "Enter doctor age:");
        if (ageStr == null || ageStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Doctor age is required.");
            return;
        }

        boolean isNumeric = ageStr.chars().allMatch(Character::isDigit);
        if (!isNumeric) {
            JOptionPane.showMessageDialog(mainPanel, "Age must be a number.");
            return;
        }
        int age = Integer.parseInt(ageStr);

        String contact = JOptionPane.showInputDialog(mainPanel, "Enter contact details:");
        if (contact == null || contact.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Contact details are required.");
            return;
        }

        String specialization = JOptionPane.showInputDialog(mainPanel, "Enter specialization:");
        if (specialization == null || specialization.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Specialization is required.");
            return;
        }

        Doctor d = new Doctor(name, age, contact, specialization, true);
        doctors.add(d);


        JOptionPane.showMessageDialog(mainPanel, "Doctor \"" + name + "\" added.");
    }
    //schedule Appointment method
    private void scheduleAppointment() {
        String patientName = JOptionPane.showInputDialog(mainPanel, "Enter patient name:");
        if (patientName == null || patientName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Patient name is required.");
            return;
        }

        String doctorName = JOptionPane.showInputDialog(mainPanel, "Enter doctor name:");
        if (doctorName == null || doctorName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Doctor name is required.");
            return;
        }

        Patient p = findPatientByName(patientName);
        Doctor d = findDoctorByName(doctorName);

        if (p != null && d != null) {
            p.scheduleAppointment();
            d.scheduleAppointment();
            p.assignedDoctor = doctorName;
            d.setAvailabile(false);
            JOptionPane.showMessageDialog(mainPanel, "Appointment scheduled!");
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Patient or Doctor not found!");
        }
    }
    //cancel appointment method
    private void cancelAppointment()
    {
        String patientName = JOptionPane.showInputDialog(mainPanel, "Enter patient name to cancel appointment:");
        if (patientName == null || patientName.trim().isEmpty())
        {
            JOptionPane.showMessageDialog(mainPanel, "Patient name is required.");
            return;
        }

        Patient p = findPatientByName(patientName);

        if (p != null)
        {
            Doctor d = findDoctorByName(p.assignedDoctor);
            p.cancelAppointment();
            if (d != null)
            {
                d.setAvailabile(true); // Make doctor available again
            }
            JOptionPane.showMessageDialog(mainPanel, "Appointment cancelled.");
        }
        else
        {
            JOptionPane.showMessageDialog(mainPanel, "Patient not found!");
        }
    }
    //view records method for registered patient
    private void viewRecords() {
        String input = JOptionPane.showInputDialog(mainPanel, "Enter patient name to view record (to view all type 'all'):");

        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Input is required.");
            return;
        }

        if (input.equalsIgnoreCase("all")) {
            if (patients.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "No patients available.");
                return;
            }

            // Show all patients
            String[] columnNames = {"Name", "Age", "Diagnosis", "Treatment Plan", "Assigned Doctor"};
            String[][] data = new String[patients.size()][columnNames.length];

            for (int i = 0; i < patients.size(); i++) {
                Patient p = patients.get(i);
                OutpatientRecord r = new OutpatientRecord(p.getDiagnosis(), "Simple treatment plan");

                data[i][0] = p.name;
                data[i][1] = String.valueOf(p.age);
                data[i][2] = p.getDiagnosis();
                data[i][3] = r.getTreatmentPlan();
                data[i][4] = p.assignedDoctor;
            }

            JTable table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);

            JOptionPane.showMessageDialog(mainPanel, scrollPane, "All Patient Records", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Show search with name patient
            Patient p = findPatientByName(input);

            if (p != null) {
                OutpatientRecord r = new OutpatientRecord(p.getDiagnosis(), "Simple treatment plan");

                String[] columnNames = {"Name", "Age", "Diagnosis", "Treatment Plan", "Assigned Doctor"};

                String[][] data = {
                        {
                                p.name,
                                String.valueOf(p.age),
                                p.getDiagnosis(),
                                r.getTreatmentPlan(),
                                p.assignedDoctor
                        }
                };

                JTable table = new JTable(data, columnNames);
                JScrollPane scrollPane = new JScrollPane(table);

                JOptionPane.showMessageDialog(mainPanel, scrollPane, "Patient Record", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Patient not found!");
            }
        }
    }
    // methods for view all registered doctor
    private void viewAllDoctors() {
        if (doctors.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "No doctors registered.");
            return;
        }

        String[] columnNames = {"Name", "Age", "Specialization", "Availability"};
        String[][] data = new String[doctors.size()][columnNames.length];

        for (int i = 0; i < doctors.size(); i++) {
            Doctor d = doctors.get(i);

            data[i][0] = d.name;
            data[i][1] = String.valueOf(d.age);
            data[i][2] = d.getSpecialization();
            data[i][3] = d.isAvailabile() ? "Available" : "Unavailable";
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        JOptionPane.showMessageDialog(
                mainPanel,
                scrollPane,
                "All Registered Doctors",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    //methods to find patient name in array by enhanced looping
    private Patient findPatientByName(String name) {
        for (Patient p : patients) {
            if (p.name.equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }
    //methods to find doctor name in array by enhanced looping
    private Doctor findDoctorByName(String name) {
        for (Doctor d : doctors) {
            if (d.name.equalsIgnoreCase(name)) {
                return d;
            }
        }
        return null;
    }

    private void addStaff() {
        String name = JOptionPane.showInputDialog(mainPanel, "Enter staff name:");
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Staff name is required.");
            return;
        }

        String ageStr = JOptionPane.showInputDialog(mainPanel, "Enter staff age:");
        if (ageStr == null || ageStr.trim().isEmpty() || !ageStr.chars().allMatch(Character::isDigit)) {
            JOptionPane.showMessageDialog(mainPanel, "Age must be a number.");
            return;
        }
        int age = Integer.parseInt(ageStr);

        String contact = JOptionPane.showInputDialog(mainPanel, "Enter contact details:");
        if (contact == null || contact.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Contact details are required.");
            return;
        }

        String role = JOptionPane.showInputDialog(mainPanel, "Enter staff role:");
        if (role == null || role.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Role is required.");
            return;
        }

        String department = JOptionPane.showInputDialog(mainPanel, "Enter department:");
        if (department == null || department.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Department is required.");
            return;
        }

        Staff s = new Staff(name, age, contact, role, department);
        staffs.add(s);
        JOptionPane.showMessageDialog(mainPanel, "Staff \"" + name + "\" added.");
    }

    private void viewAllStaff() {
        if (staffs.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "No staff registered.");
            return;
        }

        String[] columnNames = {"Name", "Age", "Contact", "Role", "Department"};
        String[][] data = new String[staffs.size()][columnNames.length];

        for (int i = 0; i < staffs.size(); i++) {
            Staff s = staffs.get(i);
            data[i][0] = s.getName();
            data[i][1] = String.valueOf(s.getAge());
            data[i][2] = s.getContact();
            data[i][3] = s.getRole();
            data[i][4] = s.getDepartment();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        JOptionPane.showMessageDialog(
                mainPanel,
                scrollPane,
                "All Staff",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}

