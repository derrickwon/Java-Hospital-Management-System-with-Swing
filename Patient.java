public class Patient extends Person implements Schedulable {
    private String medicalHistory;
    private String diagnosis;
    public String assignedDoctor = "Not Assigned";

    public Patient(String name, int age, String contactDetails, String medicalHistory, String diagnosis) {
        super(name, age, contactDetails);
        this.medicalHistory = medicalHistory;
        this.diagnosis = diagnosis;
    }

    @Override
    public void scheduleAppointment() {
        System.out.println(name + " appointment scheduled.");
    }

    @Override
    public void cancelAppointment() {
        System.out.println(name + " appointment canceled.");
    }

    @Override
    public void viewSchedule() {
        System.out.println("Viewing schedule for patient: " + name);
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
}
