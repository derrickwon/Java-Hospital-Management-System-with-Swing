public class Staff extends Person {
    private String role;
    private String department;

    public Staff(String name, int age, String contact, String role, String department) {
        super(name, age, contact);
        this.role = role;
        this.department = department;
    }
    public String getRole() {
        return role;
    }

    public String getDepartment() {
        return department;
    }
}