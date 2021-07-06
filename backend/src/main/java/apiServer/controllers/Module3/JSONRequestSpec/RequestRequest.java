package apiServer.controllers.Module3.JSONRequestSpec;

import java.sql.Timestamp;

public class RequestRequest {

    private int mailId;
    private String userId;
    private String contact;
    private String location;
    private Timestamp reqDate;
    private String reqType;
    private String reqComment;

    public RequestRequest(int mailId, String userId, String contact, String location, Timestamp reqDate, String reqType, String reqComment) {
        this.mailId = mailId;
        this.userId = userId;
        this.contact = contact;
        this.location = location;
        this.reqDate = reqDate;
        this.reqType = reqType;
        this.reqComment = reqComment;
    }

    public int getMailId() {
        return mailId;
    }

    public String getUserId() {
        return userId;
    }

    public String getContact() {
        return contact;
    }

    public String getLocation() {
        return location;
    }

    public Timestamp getReqDate() {
        return reqDate;
    }

    public String getReqType() {
        return reqType;
    }

    public String getReqComment() {
        return reqComment;
    }
}
