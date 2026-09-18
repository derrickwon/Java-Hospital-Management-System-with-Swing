public class Doctor extends Person implements Schedulable {
    private String specialization;
    private boolean availabile;

    public Doctor(String name, int age, String contact, String specialization, boolean availability) {
        super(name, age, contact);
        this.specialization = specialization;
        this.availabile = availability;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public int getAge() {
        return super.getAge();
    }

    @Override
    public String getContact() {
        return super.getContact();
    }

    public String getSpecialization() {
        return specialization;
    }

    public boolean isAvailabile() {
        return availabile;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public void setAge(int age) {
        super.setAge(age);
    }

    @Override
    public void setContact(String contact) {
        super.setContact(contact);
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setAvailabile(boolean availabile) {
        this.availabile = availabile;
    }

    @Override
    public void scheduleAppointment() {
        System.out.println("Doctor " + name + " appointment scheduled.");
    }

    @Override
    public void cancelAppointment() {
        System.out.println("Doctor " + name + " appointment canceled.");
    }

    @Override
    public void viewSchedule() {
        System.out.println("Viewing schedule for doctor: " + name);
    }


}
