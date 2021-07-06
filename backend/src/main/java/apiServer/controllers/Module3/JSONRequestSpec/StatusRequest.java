package apiServer.controllers.Module3.JSONRequestSpec;

public class StatusRequest {
    private int requestId;
    private String userId;
    private String status;
    private String reqComment;

    public StatusRequest(int requestId, String userId, String status, String reqComment) {
        this.requestId = requestId;
        this.userId = userId;
        this.status = status;
        this.reqComment = reqComment;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public String getReqComment() {
        return reqComment;
    }
}
