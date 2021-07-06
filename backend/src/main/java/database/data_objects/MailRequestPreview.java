package database.data_objects;

import java.sql.Timestamp;

public class MailRequestPreview {
    int requestId;
    int mailId;
    String name;
    Timestamp requestDate;
    String requestType;

    public MailRequestPreview(int requestId, int mailId, String name, Timestamp requestDate, String requestType) {
        this.requestId = requestId;
        this.mailId = mailId;
        this.name = name;
        this.requestDate = requestDate;
        this.requestType = requestType;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getMailId() {
        return mailId;
    }

    public String getName() {
        return name;
    }

    public Timestamp getRequestDate() {
        return requestDate;
    }

    public String getRequestType() {
        return requestType;
    }
}
