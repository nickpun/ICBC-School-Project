package database.data_objects;

public class EmployeeName {
    String userId;
    String name;

    public EmployeeName (String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}
