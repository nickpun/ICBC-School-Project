package apiServer.controllers.ModuleAdmin.JSONRequestSpec;

public class EmployeeRequest {
    private String userId;
    private String firstName;
    private String lastName;
    private String dept;

    public EmployeeRequest(String userId, String firstName, String lastName, String dept) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dept = dept;
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
}
