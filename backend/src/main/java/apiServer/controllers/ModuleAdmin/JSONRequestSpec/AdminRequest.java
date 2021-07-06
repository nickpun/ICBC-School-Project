package apiServer.controllers.ModuleAdmin.JSONRequestSpec;

public class AdminRequest {
    private String userId;
    private boolean isAdmin;

    public AdminRequest(String userId, boolean isAdmin) {
        this.userId = userId;
        this.isAdmin = isAdmin;
    }

    public String getUserId() {
        return userId;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }
}
