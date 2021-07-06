package database.data_objects;

public class Employee {
    String userId;
    String firstName;
    String lastName;
    String dept;
    Boolean isAdmin;

    public Employee(String userId, String firstName, String lastName, String dept, Boolean isAdmin) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dept = dept;
        this.isAdmin = isAdmin;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDept() {
        return dept;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }
}
